DESCRIPTION = "OpenCV"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/opencv/opencv.git;protocol=https;branch=4.x"
SRCREV = "5199850039ad23f1f0e6cccea5061a9fea5efca6"

# Define an empty do_unpack function
do_unpack() {
    # Intentionally left empty to prevent default unpack behavior
    :
}

do_fetch() {
    # Check if the source directory exists
    if [ ! -d "${S}" ]; then
        git clone https://github.com/opencv/opencv.git ${S} || {
            echo "ERROR: Failed to clone OpenCV repository." >&2
            exit 1
        }

        # Change to the source directory
        cd ${S} || {
            echo "ERROR: Source directory does not exist." >&2
            exit 1
        }

        # Fetch all tags
        git fetch --tags || {
            echo "ERROR: Failed to fetch tags." >&2
            exit 1
        }

        # Checkout the specific revision
        git checkout ${SRCREV} || {
            echo "ERROR: Failed to checkout the specific revision." >&2
            exit 1
        }
    else
        echo "Source already fetched."
    fi
}
