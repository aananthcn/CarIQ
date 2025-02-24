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


GIT_SHALLOW = "1"
GIT_SHALLOW_DEPTH = "1"
GIT_SHALLOW_SINCE = ""

inherit deploy nopackages

S = "${WORKDIR}/git"

do_install() {
    :
}

do_deploy() {
    install -d ${DEPLOYDIR}/bootfiles
    # Copy essential boot files (non-.img, non-.dtb, or bcm2712-specific)
    for file in ${S}/boot/*; do
        if [ -f "$file" ]; then
            base_name=$(basename "$file")
            case "$base_name" in
                *.img|*.dtb)
                    if echo "$base_name" | grep -q "bcm2712"; then
                        cp -a "$file" ${DEPLOYDIR}/bootfiles/
                    fi
                    ;;
                *)
                    cp -a "$file" ${DEPLOYDIR}/bootfiles/
                    ;;
            esac
        fi
    done
    # Copy all .dtbo files from overlays/
    for file in ${S}/boot/overlays/*; do
        if [ -f "$file" ]; then
            base_name=$(basename "$file")
            case "$base_name" in
                *.dtbo)
                    cp -a "$file" ${DEPLOYDIR}/bootfiles/
                    ;;
            esac
        fi
    done

    # install the custome config.txt file to the boot partition
    install -m 0644 ${WORKDIR}/config.txt ${DEPLOYDIR}/bootfiles/config.txt
}

addtask deploy before do_build after do_install

DEPENDS += "git-native"

INSANE_SKIP:${PN} += "installed-vs-shipped"

SPDXLICENSEMAP[LicenseRef-Broadcom] = "LicenseRef-Broadcom"