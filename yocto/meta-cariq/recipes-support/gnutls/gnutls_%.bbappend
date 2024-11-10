FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://gnutls.pc"

do_install:append() {
    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${WORKDIR}/gnutls.pc ${D}${libdir}/pkgconfig/
}
