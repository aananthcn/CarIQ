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
        printf("%s: camera-player for port %d is spawned!\n", __func__, port);
    }
}


// Thread function to listen for incoming packets
void* listen_on_port(void* arg) {
    char buffer[8192]; // Have large size buffer to handle camera stream
    struct sockaddr_in cliaddr;
    socklen_t len = sizeof(cliaddr);
    int port = *(int*)arg;

    // Create and bind socket here
    int sockfd;
    struct sockaddr_in servaddr;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }

    // Allow immediate reuse of the address and port
    int opt = 1;
    if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt)) < 0) {
        perror("setsockopt(SO_REUSEADDR) failed");
        close(sockfd);
        exit(EXIT_FAILURE);
    }

    // initialize addr, port and bind
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = INADDR_ANY;
    servaddr.sin_port = htons(port);

    if (bind(sockfd, (const struct sockaddr*)&servaddr, sizeof(servaddr)) < 0) {
        perror("Bind failed");
        printf("Bing failed for port: %d\n", port);
        exit(EXIT_FAILURE);
    }

    printf("%s: waiting for UDP message for port %d\n", __func__, port);
    while (1) {
        int n = recvfrom(sockfd, buffer, sizeof(buffer), 0, (struct sockaddr*)&cliaddr, &len);
        if (n < 0) {
            perror("recvfrom failed");
            close(sockfd);
            pthread_exit(NULL);
        }

        // Packet received, handle it
        close(sockfd);
        printf("Camera Listener: message received! Closing port %d\n", port);

        // Sleep to ensure socket closure propagates properly
        usleep(100000); // 100 ms

        // Spawn the camera-player process
        spawn_cam_player(port);

        // Execution will reach here only if child process is killed
        pthread_exit(NULL);  // Exit the thread after handling the first packet
    }
}


int main() {
    pthread_t threads[PORT_COUNT];

    // Initialize sockets and create threads
    for (int i = 0; i < PORT_COUNT; i++) {
        if (pthread_create(&threads[i], NULL, listen_on_port, (void*)&udp_ports[i]) != 0) {
            perror("pthread_create failed");
            exit(EXIT_FAILURE);
        }
    }

    // Wait for all threads to terminate
    for (int i = 0; i < PORT_COUNT; i++) {
        pthread_join(threads[i], NULL);
    }

    return 0;
}
