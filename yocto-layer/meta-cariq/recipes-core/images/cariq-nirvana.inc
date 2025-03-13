# cariq-nirvana.inc
# Common configuration for CarIQ Nirvana (Unbonded, free and open source)

ROS2_DISTRO = "humble"


IMAGE_INSTALL += "ros-core perception ros2launch ros2bag ros2topic ros-setup \
        demo-nodes-cpp"

# Adaptive AUOSAR - Open Source
IMAGE_INSTALL += " adaptive-autosar doip-lib obd2-emulator async-bsd-socket-lib \
"
