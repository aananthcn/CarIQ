DESCRIPTION = "CarIQ SD Card Image for Raspberry Pi Boards"

CARIQ_NODE_NAME = "EN2"

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
    opencv \
    ffmpeg \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${MACHINE_EXTRA_RRECOMMENDS} \
    "

# Adding network manager for Edge Node
IMAGE_INSTALL += "networkmanager network-manager-applet"

# systemd is used as init manager for all nodes
IMAGE_INSTALL:append = " systemd systemd-analyze systemd-serialgetty"

# for camera streaming
IMAGE_INSTALL:append = " gstreamer1.0 gstreamer1.0-plugins-base \
        gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-libav \
        v4l-utils "

# WiFi in RPis won't work if generic linux-firmware is used, hence MACHINE_EXTRA_RRECOMMENDS include special bins
IMAGE_INSTALL:remove = "linux-firmware"

# 4G Rootfs
IMAGE_ROOTFS_SIZE = "4194304"

# The text (Edge Node 2) used here will be used as id in NetworkManager.conf file
DISTRO_FEATURES:append = " en2"

inherit core-image
