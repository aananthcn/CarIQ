#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <pthread.h>
#include <errno.h>


// List of UDP ports to listen to
static const int udp_ports[] = {5001, 5002};
#define PORT_COUNT (sizeof(udp_ports) / sizeof(udp_ports[0]))

// Data type definition
typedef struct {
    int port;
    int sockfd;
} ThreadArgs;


// Function to handle incoming packets
void spawn_cam_player(int port) {
    pid_t pid = fork();
    if (pid == -1) {
        perror("fork failed");
        exit(EXIT_FAILURE);
    }

    if (pid == 0) {
        // Child process - spawn camera-player app to handle the UDP stream
        char port_str[6];
        snprintf(port_str, sizeof(port_str), "%d", port);
        execlp("/usr/bin/camera-player", "camera-player", port_str, (char *)NULL);
        // If execlp fails
        perror("execlp failed");
        exit(EXIT_FAILURE);
    } else {
        // Parent process: wait for the child to terminate
        int status;
        waitpid(pid, &status, 0); // Wait for child to finish
    }
}


// Thread function to listen for incoming packets
void* listen_on_port(void* args) {
    ThreadArgs* thread_args = (ThreadArgs*)args;
    int sockfd = thread_args->sockfd;
    int port = thread_args->port;
    char buffer[1024];
    struct sockaddr_in cliaddr;
    socklen_t len = sizeof(cliaddr);

    while (1) {
        int n = recvfrom(sockfd, buffer, sizeof(buffer), 0, (struct sockaddr*)&cliaddr, &len);
        if (n < 0) {
            perror("recvfrom failed");
            close(sockfd);
            pthread_exit(NULL);
        }

        // Packet received, handle it
        close(sockfd);
        usleep(5000); // give control to OS so that it actually closes
        spawn_cam_player(port);
        pthread_exit(NULL);
    }
}


int main() {
    pthread_t threads[PORT_COUNT];
    ThreadArgs thread_args[PORT_COUNT];
    struct sockaddr_in servaddr;

    // Initialize sockets and create threads
    for (int i = 0; i < PORT_COUNT; i++) {
        int sockfd;
        if ((sockfd = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
            perror("socket creation failed");
            exit(EXIT_FAILURE);
        }

        memset(&servaddr, 0, sizeof(servaddr));
        servaddr.sin_family = AF_INET;
        servaddr.sin_addr.s_addr = INADDR_ANY;
        servaddr.sin_port = htons(udp_ports[i]);

        if (bind(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0) {
            perror("bind failed");
            close(sockfd);
            exit(EXIT_FAILURE);
        }

        thread_args[i].port = udp_ports[i];
        thread_args[i].sockfd = sockfd;

        if (pthread_create(&threads[i], NULL, listen_on_port, &thread_args[i]) != 0) {
            perror("pthread_create failed");
            close(sockfd);
            exit(EXIT_FAILURE);
        }
    }

    // Wait for all threads to terminate
    for (int i = 0; i < PORT_COUNT; i++) {
        pthread_join(threads[i], NULL);
    }

    return 0;
}
