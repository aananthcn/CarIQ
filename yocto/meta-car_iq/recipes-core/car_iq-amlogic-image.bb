DESCRIPTION = "A X11 SD/MMC Image for Amlogic Meson SoCs with embedded kernel on the filesystem"

IMAGE_FEATURES += "ssh-server-openssh package-management splash x11 xfce hwcodecs"

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
    xfce4 \
    xfce4-session \
    xfwm4 \
    xfdesktop \
    xfce4-panel \
    xfce4-terminal \
    thunar \
    xfce4-settings \
    xfce4-power-manager \
    xfce4-appfinder \
    xfce4-goodies \
    lightdm \
    lightdm-gtk-greeter \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

# 4G Rootfs
IMAGE_ROOTFS_SIZE = "4194304"

inherit core-image
