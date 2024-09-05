DESCRIPTION = "A workaround for the bin file not copied to rootfs even after agreeing to synaptics-killswitch license"
AUTHOR = "Aananth C N"

LICENSE = "GPL-2.0-only & Synaptics-rpidistro & binary-redist-Cypress-rpidistro"


FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://cyfmac43455-sdio.bin"

do_install:append() {
    install -d ${D}${base_libdir}/firmware/cypress
    install -m 0644 ${WORKDIR}/sources-unpack/cyfmac43455-sdio.bin ${D}${base_libdir}/firmware/cypress/cyfmac43455-sdio.bin
}
