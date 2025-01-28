SUMMARY = "TensorFlow Lite shared library and header files"
DESCRIPTION = "Recipe to install the TensorFlow Lite shared library and header files to the target"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"

SRC_URI  = "file://libtensorflowlite.so.${PV}"
SRC_URI += "file://LICENSE.txt"
SRC_URI += "git://github.com/feranick/tensorflow.git;protocol=https;branch=r2.16;tag=v${PV}"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

S = "${WORKDIR}"

do_compile() {
    # No compilation needed for the shared library as it's prebuilt.
    :
}

do_install() {
    # Install the shared library
    install -d ${D}${libdir}
    install -m 0755 ${WORKDIR}/libtensorflowlite.so.${PV} ${D}${libdir}/libtensorflowlite.so.${PV}
    ln -sf libtensorflowlite.so.${PV} ${D}${libdir}/libtensorflowlite.so

    # Install the header files
    install -d ${D}${includedir}/tensorflow/lite
    install -d ${D}${includedir}/tensorflow/lite/kernels
    install -d ${D}${includedir}/tensorflow/lite/tools
    install -d ${D}${includedir}/tensorflow/lite/delegates
    cp -r ${S}/git/tensorflow/lite/*.h ${D}${includedir}/tensorflow/lite/
    cp -r ${S}/git/tensorflow/lite/kernels/*.h ${D}${includedir}/tensorflow/lite/kernels/
    cp -r ${S}/git/tensorflow/lite/tools/*.h ${D}${includedir}/tensorflow/lite/tools/
    cp -r ${S}/git/tensorflow/lite/delegates/*.h ${D}${includedir}/tensorflow/lite/delegates/
}

FILES:${PN} = " \
    ${libdir}/libtensorflowlite.so \
    ${libdir}/libtensorflowlite.so.${PV} \
    ${includedir}/tensorflow/lite \
"

# Prevent the symlink from being moved to the -dev package
FILES_SOLIBSDEV = ""

INSANE_SKIP:${PN} += "already-stripped dev-so"
