SUMMARY = "Python3-tflite-runtime packaged from local installation"

DESCRIPTION = "Google's tflite-runtime doesn't support Python version 3.12. It \
    the 'https://github.com/feranick/tflite-runtime' website that did the magic!"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"


SRC_URI  = "file://tflite_runtime-2.16.1-cp312-cp312-linux_aarch64.whl"
SRC_URI += "file://LICENSE.txt"


DEPENDS = "python3 unzip-native"

inherit python3native


S = "${WORKDIR}"

do_install() {
    # Create the target directory for the tflite_runtime module
    mkdir -p ${D}${PYTHON_SITEPACKAGES_DIR}/tflite_runtime

    # Extract the .whl file directly into the tflite_runtime directory
    unzip -q ${WORKDIR}/tflite_runtime-2.16.1-cp312-cp312-linux_aarch64.whl -d ${D}${PYTHON_SITEPACKAGES_DIR}/tflite_runtime
}


FILES:${PN} += " \
    ${PYTHON_SITEPACKAGES_DIR}/tflite_runtime \
"

INSANE_SKIP:${PN} += "already-stripped"
