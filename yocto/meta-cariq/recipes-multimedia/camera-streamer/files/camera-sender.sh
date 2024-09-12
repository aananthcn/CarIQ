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

# Start the camera player with the provided port number
echo "Starting camera sender to stream camera via port $PORT..."

# Your camera player logic here, for example:
gst-launch-1.0 v4l2src ! videoconvert ! jpegenc ! rtpjpegpay ! udpsink host=192.168.10.100 port=${PORT}

# Example placeholder command for testing:
echo "Camera sender running on port $PORT"