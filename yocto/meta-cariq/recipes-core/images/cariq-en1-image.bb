DESCRIPTION = "CarIQ SD Card Image for Raspberry Pi Boards"

COMPATIBLE_MACHINE = "^rpi$"

IMAGE_FEATURES += "ssh-server-openssh package-management splash x11 hwcodecs"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    packagegroup-core-ssh-openssh \
    kernel-image \
    kernel-modules \
    kernel-devicetree \
    opkg \
    opkg-collateral \
    packagegroup-xfce-base \
    mesa \
    libgl \
    packagegroup-basic \
    packagegroup-base \
    lshw \
    ffmpeg \
    socat \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${MACHINE_EXTRA_RRECOMMENDS} \
"

# Infrastructure components
IMAGE_INSTALL += " pkgconf ldd-aarch64"

# Adding network manager for Edge Node
IMAGE_INSTALL += "networkmanager network-manager-applet ufw"

# systemd is used as init manager for all nodes
IMAGE_INSTALL += " systemd systemd-analyze systemd-serialgetty"

# for camera streaming
IMAGE_INSTALL += " gstreamer1.0 gstreamer1.0-plugins-base \
        gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-plugins-ugly \
        gstreamer1.0-libav x264 gstreamer1.0-rtsp-server gstreamer1.0-vaapi gst-devtools \
        gstreamer1.0-python v4l-utils libsdl2 \
"

# for image processing, display libraries
IMAGE_INSTALL += " jpeg opencv gtk+ gtk+3 libsm gobject-introspection"

# CarIQ Apps
IMAGE_INSTALL += " camera-streamer lane-detect-opencv"

# Python packages
IMAGE_INSTALL += " python3 python3-pip python3-pygobject python3-numpy \
        python-is-python3 pip-is-pip3 python3-defusedxml python3-rospkg \
        python3-pycryptodome python3-setuptools python3-catkin-pkg \
        python3-rosdistro python3-pycoral python3-tflite-runtime moviepy \
        python3-pillow python3-requests \
        python3-pyproj python3-fiona python3-pandas python3-attrs python3-certifi \
        python3-packaging python3-numpy python3-pytz python3-dateutil \
        python3-charset-normalizer python3-idna python3-six \
"

# ROS1 packages
IMAGE_INSTALL += "ros-core packagegroup-ros1-comm ros-environment roslaunch \
        std-msgs console-bridge boost poco openssl gpgme log4cxx lz4 bzip2 \
        roscpp-tutorials gdal \
"

# OTA packages
IMAGE_INSTALL += " pv ota-files lsof \
"

# NPU / TensorFlow packages
IMAGE_INSTALL += " libedgetpu"

# Development tools or utils
IMAGE_INSTALL += " glibc cmake gcc gcc-symlinks g++ g++-symlinks make automake \
        nano tree opencv-dev opencv-staticdev gtk+-dev \
        python3-dev \
"

# WiFi in RPis won't work if generic linux-firmware is used, hence MACHINE_EXTRA_RRECOMMENDS include special bins
IMAGE_INSTALL:remove = "linux-firmware"



# 4G Rootfs
IMAGE_ROOTFS_SIZE = "4194304"


inherit core-image