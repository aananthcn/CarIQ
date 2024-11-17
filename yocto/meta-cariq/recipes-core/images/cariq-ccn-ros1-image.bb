DESCRIPTION = "ROS1 Image for CCN of CarIQ"
COMPATIBLE_MACHINE = "^khadas-vim3$"

ROS1_DISTRO = "noetic"

LICENSE = "MIT"

IMAGE_INSTALL = "\
    ros-core \
    packagegroup-ros1-comm \
    std-msgs \
"

# DEPENDS += "std-msgs_0.5.13-1"


inherit core-image
