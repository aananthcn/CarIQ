DESCRIPTION = "ROS1 Image for CCN of CarIQ"
COMPATIBLE_MACHINE = "^khadas-vim3$"

ROS1_DISTRO = "noetic"

LICENSE = "MIT"

IMAGE_INSTALL = "\
    ros-core \
    packagegroup-ros1-comm \
    std-msgs \
    console-bridge \
    boost \
    poco \
    openssl \
    gpgme \
    log4cxx \
    lz4 \
    bzip2 \
    python3 \
    roscpp-tutorials \
"


inherit core-image
