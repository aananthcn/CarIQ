# rpi-firmware-boot.bb
SUMMARY = "Raspberry Pi boot firmware files"
DESCRIPTION = "Recipe to install Raspberry Pi boot firmware files from GitHub"
LICENSE = "Broadcom-RPi"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=ab1fd397d46ab12382201522b38a6bdb"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "git://github.com/raspberrypi/firmware.git;protocol=https;branch=master;name=firmware;destsuffix=git"
SRCREV = "fe200a5b779d6369c05a845c650b62d5139d17ac"
SRC_URI += "file://LICENSE"
SRC_URI += "file://config.txt"
SRC_URI += "file://cmdline.txt"


GIT_SHALLOW = "1"
GIT_SHALLOW_DEPTH = "1"
GIT_SHALLOW_SINCE = ""

inherit deploy nopackages

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}/boot
    install -m 0644 ${S}/boot/*.bin ${D}/boot/
    install -m 0644 ${S}/boot/*.elf ${D}/boot/
    install -m 0644 ${S}/boot/*.dat ${D}/boot/
    install -m 0644 ${S}/boot/bcm2712*.dtb ${D}/boot/
    install -m 0644 ${S}/boot/LICEN* ${D}/boot/
    install -m 0644 ${WORKDIR}/c*.txt ${D}/boot/

    # Install overlays into /boot/overlays/
    install -d ${D}/boot/overlays
    for dtbo in ${S}/boot/overlays/*.dtbo; do
        install -m 0644 "$dtbo" ${D}/boot/overlays/
    done
}

do_deploy() {
    install -d ${DEPLOYDIR}/bootfiles
    for file in ${D}/boot/*; do
        if [ -f "$file" ]; then
            install -m 0644 "$file" ${DEPLOYDIR}/bootfiles/
        fi
    done

    # Preserve overlays directory structure
    install -d ${DEPLOYDIR}/bootfiles/overlays
    for dtbo in ${D}/boot/overlays/*.dtbo; do
        install -m 0644 "$dtbo" ${DEPLOYDIR}/bootfiles/overlays/
    done

    # Copy kernel image as kernel_2712.img
    install -m 0644 ${DEPLOY_DIR_IMAGE}/Image ${DEPLOYDIR}/bootfiles/kernel_2712.img
}


do_deploy[depends] += "virtual/kernel:do_deploy"

addtask do_deploy before do_build after do_install


FILES:${PN} = "/boot/* /boot/overlays/*.dtbo"

DEPENDS += "git-native virtual/kernel"

INSANE_SKIP:${PN} += "installed-vs-shipped"
