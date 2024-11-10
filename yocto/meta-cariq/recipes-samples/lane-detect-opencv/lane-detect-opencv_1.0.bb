SUMMARY = "Lane detection OpenCV test application"
DESCRIPTION = "A test application that uses OpenCV and GStreamer to perform lane detection from an RTP video stream."
LICENSE = "CLOSED"

SRC_URI = "file://lane_detect_opencv.py"

inherit setuptools3-base


# Dependencies
DEPENDS += "opencv gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-good gstreamer1.0-libav"

RDEPENDS:${PN} += "python3-opencv python3-numpy gstreamer1.0"

# Install the Python script into the specified directory
do_install() {
    install -d ${D}/home/root/test-scripts
    install -m 0755 ${WORKDIR}/lane_detect_opencv.py ${D}/home/root/test-scripts/lane_detect_opencv.py
}

FILES:${PN} += "/home/root/test-scripts/lane_detect_opencv.py"
