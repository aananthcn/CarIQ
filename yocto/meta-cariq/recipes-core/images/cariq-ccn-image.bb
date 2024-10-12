DESCRIPTION = "CarIQ SD Card Image for Amlogic Meson SoCs"

COMPATIBLE_MACHINE = "^khadas-vim3$"

IMAGE_FEATURES += "ssh-server-openssh package-management splash x11 hwcodecs"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    packagegroup-core-ssh-openssh \
    kernel-image \
    kernel-modules \
    kernel-devicetree \
    linux-firmware \
    opkg \
    opkg-collateral \
    packagegroup-xfce-base \
    mesa \
    libgl \
    packagegroup-basic \
    packagegroup-base \
    lshw \
    opencv \
    ffmpeg \
    socat \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "


# Development tools or utils
IMAGE_INSTALL += " glibc"

# Adding network manager for Compute Node
IMAGE_INSTALL += "networkmanager network-manager-applet"

# systemd is used as init manager for all nodes
IMAGE_INSTALL:append = " systemd systemd-analyze systemd-serialgetty"

# for camera streaming
IMAGE_INSTALL:append = " gstreamer1.0 gstreamer1.0-plugins-base \
        gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-plugins-ugly \
        gstreamer1.0-libav x264 gstreamer1.0-rtsp-server gstreamer1.0-vaapi gst-devtools \
        gstreamer1.0-python v4l-utils libsdl2"

# Python stuffs
IMAGE_INSTALL:append = " python3 python-is-python3"

# for image processing
IMAGE_INSTALL:append = " jpeg"

# CarIQ Apps
IMAGE_INSTALL += " camera-streamer lane-detect-opencv npu-tests"


# 4G Rootfs
IMAGE_ROOTFS_SIZE = "4194304"


inherit core-image
