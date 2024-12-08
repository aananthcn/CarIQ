SUMMARY = "Python3-pycoral packaged from local installation"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "file://LICENSE.txt \
           file://__init__.py \
           file://pycoral.py"

DEPENDS = "python3"

inherit setuptools3-base

S = "${WORKDIR}"

do_install() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}/pycoral
    cp -r ${S}/*.py ${D}${PYTHON_SITEPACKAGES_DIR}/pycoral
}

FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/pycoral"
