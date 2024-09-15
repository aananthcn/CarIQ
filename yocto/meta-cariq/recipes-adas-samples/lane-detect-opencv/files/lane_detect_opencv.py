import cv2
import numpy as np
import sys
import socket
import os


# Set the DISPLAY environment variable to :0
os.environ["DISPLAY"] = ":0"

# Function to check if UDP port is available
def check_udp_port_in_use(port):
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        # Try to bind the socket to the port to check if it's in use
        sock.bind(("0.0.0.0", int(port)))
    except OSError:
        return True  # Port is in use
    finally:
        sock.close()
    return False


# Check if port argument is passed
if len(sys.argv) != 2:
    print("Usage: python lane_detection.py <port>")
    sys.exit(1)

# Use 0.0.0.0 as the IP address and take the port from the command-line argument
port = sys.argv[1]


# Check if the port is already in use
if check_udp_port_in_use(port):
    print(f"ERROR: Port {port} in use. Please stop the other application and retry!")
    sys.exit(1)


# Create the GStreamer pipeline for receiving the RTP JPEG stream
gst_pipeline = f"udpsrc port={port} ! application/x-rtp, encoding-name=JPEG,payload=26 ! rtpjpegdepay ! jpegdec ! videoconvert ! appsink"

# Open the RTP stream using GStreamer pipeline
cap = cv2.VideoCapture(gst_pipeline, cv2.CAP_GSTREAMER)


def canny(image):
    gray_image = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)
    blur_image = cv2.GaussianBlur(gray_image, (5, 5), 0)
    canny_image = cv2.Canny(blur_image, 50, 150)
    return canny_image


def region_of_interest(image):
    ht = image.shape[0]
    triangle = np.array([
        [(200, ht), (1100, ht), (550, 250)]
    ])
    mask = np.zeros_like(image)
    cv2.fillPoly(mask, triangle, 255)
    masked_image = cv2.bitwise_and(image, mask)
    return masked_image


def display_lines(image, lines):
    line_image = np.zeros_like(image)
    if lines is not None:
        for line in lines:
            if line is not None:  # Ensure line is valid
                try:
                    # Convert coordinates to integers to avoid errors
                    x1, y1, x2, y2 = map(int, line)

                    # Check if coordinates are within the image dimensions
                    if (0 <= x1 < image.shape[1] and 0 <= y1 < image.shape[0] and
                        0 <= x2 < image.shape[1] and 0 <= y2 < image.shape[0]):
                        cv2.line(line_image, (x1, y1), (x2, y2), (255, 0, 0), 10)
                    else:
                        print(f"Line coordinates out of bounds: {(x1, y1, x2, y2)}")
                except ValueError as e:
                    print(f"Invalid line coordinates: {line}, Error: {e}")
                    continue  # Skip drawing if there's an issue with the line coordinates
    return line_image


def make_coordinates(image, line_parameters):
    try:
        slope, intercept = line_parameters
    except TypeError:
        return None  # Invalid line parameters

    y1 = image.shape[0]
    y2 = int(y1 * (3 / 5))

    try:
        x1 = int((y1 - intercept) / slope)
        x2 = int((y2 - intercept) / slope)
    except ZeroDivisionError:
        return None  # Slope was 0, leading to division error

    # Ensure coordinates are within the image dimensions
    x1 = max(0, min(x1, image.shape[1] - 1))
    x2 = max(0, min(x2, image.shape[1] - 1))
    y1 = max(0, min(y1, image.shape[0] - 1))
    y2 = max(0, min(y2, image.shape[0] - 1))

    return np.array([x1, y1, x2, y2])


def average_slope_intercept(image, lines):
    left_fit = []
    right_fit = []

    if lines is None:
        return None  # No lines detected

    for line in lines:
        x1, y1, x2, y2 = line.reshape(4)

        # Skip line segments where x1 and x2 are very close to avoid unstable polyfit
        if abs(x1 - x2) < 1e-2:
            continue

        parameters = np.polyfit((x1, x2), (y1, y2), 1)
        p_m = parameters[0]  # Slope
        p_c = parameters[1]  # Intercept

        if p_m < 0:
            left_fit.append((p_m, p_c))
        else:
            right_fit.append((p_m, p_c))

    left_line = None
    right_line = None

    if left_fit:
        left_fit_avg = np.average(left_fit, axis=0)
        left_line = make_coordinates(image, left_fit_avg)

    if right_fit:
        right_fit_avg = np.average(right_fit, axis=0)
        right_line = make_coordinates(image, right_fit_avg)

    return [line for line in [left_line, right_line] if line is not None]


# Open the RTP stream from UDP source using GStreamer pipeline
cap = cv2.VideoCapture(gst_pipeline, cv2.CAP_GSTREAMER)

while cap.isOpened():
    _, vframe = cap.read()
    if vframe is None:
        break  # End of the stream or error

    canny_image = canny(vframe)
    cropped_img = region_of_interest(canny_image)

    lines = cv2.HoughLinesP(cropped_img, 2, np.pi / 180, 100, np.array([]), minLineLength=40, maxLineGap=5)

    avgd_lines = average_slope_intercept(vframe, lines)
    if avgd_lines:
        line_image = display_lines(vframe, avgd_lines)
        comp_image = cv2.addWeighted(vframe, 0.8, line_image, 1, 1)
        cv2.imshow("result", comp_image)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break  # Exit on 'q' key press

cap.release()
cv2.destroyAllWindows()
