SUMMARY = "Edge TPU runtime library for Coral devices - Pre Built"
# This file is taken from the following website
# https://github.com/digi-embedded/meta-digi/blob/gatesgarth/meta-digi-arm/recipes-bsp/libedgetpu

HOMEPAGE = "https://coral.googlesource.com/edgetpu"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"


FILESEXTRAPATHS:prepend := "${THISDIR}/files:"


# SRC_URI = "https://dl.google.com/coral/edgetpu_api/edgetpu_runtime_20210119.zip"
SRC_URI = "https://github.com/google-coral/libedgetpu/releases/download/release-grouper/edgetpu_runtime_20221024.zip \
    file://LICENSE.txt"

SRC_URI[md5sum] = "0239aa3a4091dff542aaa6707b2b7933"
SRC_URI[sha256sum] = "7f17d85e6f21ec861af3f9168043a7ec351b80e08c9f168013ab68e99410aee2"


S = "${WORKDIR}"
UNZIP_DIR = "${S}/edgetpu_runtime"

RDEPENDS:${PN} = "libusb1"

# The library files in direct correspond to max frequency, those in throttled correspond to reduced frequency.
LIBEDGETPU_TYPE = "direct"
LIBEDGETPU_ARCH = "aarch64"

do_install() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNZIP_DIR}/libedgetpu/edgetpu-accelerator.rules \
                    ${D}${sysconfdir}/udev/rules.d/99-edgetpu-accelerator.rules

    install -d ${D}/${libdir}
    install -m 755 ${UNZIP_DIR}/libedgetpu/${LIBEDGETPU_TYPE}/${LIBEDGETPU_ARCH}/libedgetpu.so.1.0 \
                   ${D}/${libdir}/libedgetpu.so.1.0
    ln -sf ${libdir}/libedgetpu.so.1.0 ${D}/${libdir}/libedgetpu.so.1
    ln -sf ${libdir}/libedgetpu.so.1.0 ${D}/${libdir}/libedgetpu.so

    install -d ${D}/${includedir}
    install -m 755 ${UNZIP_DIR}/libedgetpu/edgetpu.h ${D}/${includedir}/edgetpu.h
}

FILES:${PN} += "${libdir}/libedgetpu.so \
                ${includedir}/edgetpu.h \
"

INSANE_SKIP:${PN} += "already-stripped"
