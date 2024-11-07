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
IMAGE_INSTALL += " pkgconf"

# Adding network manager for Edge Node
IMAGE_INSTALL += "networkmanager network-manager-applet"

# systemd is used as init manager for all nodes
IMAGE_INSTALL += " systemd systemd-analyze systemd-serialgetty"

# for camera streaming
IMAGE_INSTALL += " gstreamer1.0 gstreamer1.0-plugins-base \
        gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-plugins-ugly \
        gstreamer1.0-libav x264 gstreamer1.0-rtsp-server gstreamer1.0-vaapi gst-devtools \
        gstreamer1.0-python v4l-utils libsdl2"

# for image processing, display libraries
IMAGE_INSTALL += " jpeg opencv gtk+ gtk+3 libsm gobject-introspection"

# CarIQ Apps
IMAGE_INSTALL += " camera-streamer"

# Python stuffs
IMAGE_INSTALL += " python3 python3-pip python3-pygobject python3-numpy \
        python-is-python3 pip-is-pip3 \
        moviepy"


# Development tools or utils
IMAGE_INSTALL += " glibc cmake gcc gcc-symlinks g++ g++-symlinks make automake \
        nano tree opencv-dev opencv-staticdev"



# WiFi in RPis won't work if generic linux-firmware is used, hence MACHINE_EXTRA_RRECOMMENDS include special bins
IMAGE_INSTALL:remove = "linux-firmware"

# 4G Rootfs
IMAGE_ROOTFS_SIZE = "4194304"


inherit core-image
