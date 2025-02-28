DESCRIPTION = "ROS2 Image for CCN of CarIQ"
COMPATIBLE_MACHINE = "^khadas-vim3$"

ROS2_DISTRO = "humble"

LICENSE = "MIT"

IMAGE_INSTALL += "ros-core perception ros2launch ros2bag ros2topic ros-setup \
        demo-nodes-cpp"

inherit core-image

