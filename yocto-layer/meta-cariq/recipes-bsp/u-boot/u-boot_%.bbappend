FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Use the best available configuration for Raspberry Pi 5
UBOOT_MACHINE:rpi5 = "rpi_arm64_defconfig"

# Fetch U-Boot v2025.01 directly
SRC_URI = "git://source.denx.de/u-boot.git;branch=master;name=u-boot"
SRCREV = "refs/tags/v2025.01"
PV = "2025.01"

SRC_URI += "file://u-boot-initial-env"
SRC_URI += "file://boot.txt"
SRC_URI += "file://config.txt"


# Ensure build flags for BCM2712
EXTRA_OEMAKE:append:rpi5 = " CONFIG_SYS_SOC=bcm2712"

DEPENDS += "gnutls-native u-boot-tools-native"



do_configure:prepend() {
    oe_runmake -C ${S} mrproper
}


# Ensure BCM2712 SoC is explicitly set and enable ENV in FAT support
do_configure:append:rpi5 () {
    echo 'CONFIG_SYS_SOC="bcm2712"' >> ${B}/.config
    echo 'CONFIG_SYS_SOC="bcm2712"' >> ${B}/include/generated/autoconf.h
    echo 'CONFIG_ENV_IS_IN_FAT=y' >> ${B}/.config
}


do_compile:append() {
    # Generate uboot.env from u-boot-initial-env
    mkenvimage -s 16384 -o ${B}/uboot.env ${WORKDIR}/u-boot-initial-env

    # Convert boot.txt to boot.scr
    mkimage -A arm64 -O linux -T script -C none -a 0 -e 0 -n "Boot script" -d ${WORKDIR}/boot.txt ${B}/boot.scr
}


do_deploy:append() {
    install -d ${DEPLOYDIR}/bootfiles

    # Deploy u-boot.bin
    install -m 0644 ${B}/u-boot.bin ${DEPLOYDIR}/bootfiles/

    # Deploy uboot.env
    install -m 0644 ${B}/uboot.env ${DEPLOYDIR}/bootfiles/

    # Deploy config.txt from recipe's files folder
    install -m 0644 ${WORKDIR}/config.txt ${DEPLOYDIR}/bootfiles/

    # Deploy the generated boot.scr
    install -m 0644 ${B}/boot.scr ${DEPLOYDIR}/bootfiles/
}
