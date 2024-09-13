#!/bin/sh

# Check if exactly one argument (port number) is provided
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <port-num>"
    exit 1
fi

# Assign the first argument to PORT variable
PORT=$1

# Always use the first display
sleep 5
export DISPLAY=:0

# Validate if the argument is a valid number
if ! [[ "$PORT" =~ ^[0-9]+$ ]]; then
    echo "Error: Port number must be a valid number"
    exit 1
fi

# Start the camera player with the provided port number
echo "Starting camera player on port $PORT..."

# Your camera player logic here, for example:
gst-launch-1.0 udpsrc port=${PORT} ! application/x-rtp, payload=26 ! rtpjpegdepay ! jpegdec ! videoconvert ! ximagesink

# Example placeholder command for testing:
echo "Camera player running on port $PORT"

