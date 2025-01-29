SUMMARY = "Edge TPU runtime library for Coral devices - Pre Built, see README.md"
HOMEPAGE = "https://github.com/feranick/libedgetpu"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"


FILESEXTRAPATHS:prepend := "${THISDIR}/files:"


SRC_URI  = "file://99-edgetpu-accelerator.rules"
SRC_URI += "file://edgetpu_c.h"
SRC_URI += "file://edgetpu.h"
SRC_URI += "file://libedgetpu.so.1.0"
SRC_URI += "file://LICENSE.txt"


RDEPENDS:${PN} = "libusb1"

S = "${WORKDIR}"


do_install() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 99-edgetpu-accelerator.rules ${D}${sysconfdir}/udev/rules.d/99-edgetpu-accelerator.rules

    install -d ${D}${libdir}
    install -m 755 libedgetpu.so.1.0 ${D}${libdir}/libedgetpu.so.1.0
    ln -sf libedgetpu.so.1.0 ${D}${libdir}/libedgetpu.so.1
    ln -sf libedgetpu.so.1.0 ${D}${libdir}/libedgetpu.so

    install -d ${D}${includedir}
    install -m 644 edgetpu.h ${D}${includedir}/edgetpu.h
    install -m 644 edgetpu_c.h ${D}${includedir}/edgetpu_c.h
}


FILES:${PN} += "${libdir}/libedgetpu.so \
                ${libdir}/libedgetpu.so.1 \
                ${libdir}/libedgetpu.so.1.0 \
                ${includedir}/edgetpu.h \
                ${includedir}/edgetpu_c.h \
"


INSANE_SKIP:${PN} += "already-stripped"
