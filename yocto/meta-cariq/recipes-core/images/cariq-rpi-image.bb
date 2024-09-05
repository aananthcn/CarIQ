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
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${MACHINE_EXTRA_RRECOMMENDS} \
    "

# Adding network manager for Edge Node
IMAGE_INSTALL += "networkmanager network-manager-applet"

# systemd is used as init manager for all nodes
IMAGE_INSTALL:append = " systemd systemd-analyze systemd-serialgetty"

# WiF in RPis won't work if generic linux-firmware is used, hence MACHINE_EXTRA_RRECOMMENDS include special bins
IMAGE_INSTALL:remove = "linux-firmware"

# 4G Rootfs
IMAGE_ROOTFS_SIZE = "4194304"

inherit core-image
