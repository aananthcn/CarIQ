SUMMARY = "Python3-tflite-runtime packaged from local installation"

DESCRIPTION = "Google's tflite-runtime doesn't support Python version 3.12. It \
    the 'https://github.com/feranick/tflite-runtime' website that did the magic!"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://__init__.py \
    file://_pywrap_tensorflow_interpreter_wrapper.so \
    file://interpreter.py \
    file://LICENSE.txt \
    file://metrics_interface.py \
    file://metrics_portable.py \
"

DEPENDS = "python3"


inherit setuptools3-base

S = "${WORKDIR}"

do_install() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}/tflite_runtime

    # copy tflite-runtime modules
    cp -r ${S}/*.py ${D}${PYTHON_SITEPACKAGES_DIR}/tflite_runtime
    cp -r ${S}/*.so ${D}${PYTHON_SITEPACKAGES_DIR}/tflite_runtime

}

FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/tflite_runtime"

INSANE_SKIP:${PN} += "already-stripped"
