#!/bin/sh

# Check if exactly one argument (port number) is provided
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <port-num>"
    exit 1
fi

# Assign the first argument to PORT variable
PORT=$1

# Validate if the argument is a valid number
if ! [[ "$PORT" =~ ^[0-9]+$ ]]; then
    echo "Error: Port number must be a valid number"
    exit 1
fi

# Wait for XFCE4 GDM to come up in Linux
while true; do
    if pgrep -x "xfwm4" > /dev/null; then
        echo "X11 is running."
        break
    else
        echo "Waiting for X11 to start..."
        # Sleep for 100 milliseconds (0.1 seconds)
        sleep 0.1
    fi
done

# Always use the first display
export DISPLAY=:0

# Start the camera player with the provided port number
echo "Starting camera player on port $PORT..."

# Your camera player logic here, for example:
gst-launch-1.0 udpsrc port=${PORT} ! application/x-rtp, payload=26 ! rtpjpegdepay ! jpegdec ! videoconvert ! ximagesink

# Example placeholder command for testing:
echo "Camera player running on port $PORT"

