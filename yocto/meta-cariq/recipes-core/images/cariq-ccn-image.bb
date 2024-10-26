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
    ffmpeg \
    socat \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "


# Adding network manager for Compute Node
IMAGE_INSTALL += "networkmanager network-manager-applet"

# systemd is used as init manager for all nodes
IMAGE_INSTALL += " systemd systemd-analyze systemd-serialgetty"

# for camera streaming
IMAGE_INSTALL += " gstreamer1.0 gstreamer1.0-plugins-base \
        gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-plugins-ugly \
        gstreamer1.0-libav x264 gstreamer1.0-rtsp-server gstreamer1.0-vaapi gst-devtools \
        gstreamer1.0-python v4l-utils libsdl2"

# for image processing, display libraries
IMAGE_INSTALL += " jpeg opencv gtk+ libsm gobject-introspection"

# CarIQ Apps
IMAGE_INSTALL += " camera-streamer lane-detect-opencv npu-tests"

# Python stuffs
IMAGE_INSTALL += " python3 python-is-python3 python3-pygobject python3-numpy"


# Development tools or utils
IMAGE_INSTALL += " glibc cmake gcc gcc-symlinks g++ g++-symlinks make automake \
        nano tree opencv-dev opencv-staticdev gtk+-dev \
        python3-dev"



# 4G Rootfs
IMAGE_ROOTFS_SIZE = "4194304"


inherit core-image
