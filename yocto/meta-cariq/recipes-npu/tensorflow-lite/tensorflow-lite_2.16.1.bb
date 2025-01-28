SUMMARY = "TensorFlow Lite shared library"
DESCRIPTION = "Recipe to install the TensorFlow Lite shared library to the target"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"

SRC_URI  = "file://libtensorflowlite.so.2.16.1"
SRC_URI += "file://LICENSE.txt"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${libdir}
    install -m 0755 ${WORKDIR}/libtensorflowlite.so.2.16.1 ${D}${libdir}/libtensorflowlite.so.2.16.1
    ln -sf libtensorflowlite.so.2.16.1 ${D}${libdir}/libtensorflowlite.so
}


FILES:${PN} = "${libdir}/libtensorflowlite.so ${libdir}/libtensorflowlite.so.2.16.1"

# Prevent the symlink from being moved to the -dev package
FILES_SOLIBSDEV = ""

INSANE_SKIP:${PN} += "already-stripped"
INSANE_SKIP:${PN} += "dev-so"
