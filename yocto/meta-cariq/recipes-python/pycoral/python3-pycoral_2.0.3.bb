SUMMARY = "Python3-pycoral packaged from local installation"

DESCRIPTION = "Google's pycoral doesn't support Python version 3.12. It \
    the 'https://github.com/feranick/pycoral' website that did the magic!"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://adapters/ \
    file://learn/ \
    file://pipeline/ \
    file://pybind/ \
    file://utils/ \
    file://__init__.py \
    file://LICENSE.txt \
"

DEPENDS = "python3"

RDEPENDS:${PN} = "libedgetpu"


inherit setuptools3-base

S = "${WORKDIR}"

do_install() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}/pycoral

    # copy pycoral modules
    cp -r ${S}/*.py ${D}${PYTHON_SITEPACKAGES_DIR}/pycoral

    # copy pycoral submodules
    cp -r ${S}/adapters ${D}${PYTHON_SITEPACKAGES_DIR}/pycoral
    cp -r ${S}/learn ${D}${PYTHON_SITEPACKAGES_DIR}/pycoral
    cp -r ${S}/pipeline ${D}${PYTHON_SITEPACKAGES_DIR}/pycoral
    cp -r ${S}/pybind ${D}${PYTHON_SITEPACKAGES_DIR}/pycoral
    cp -r ${S}/utils ${D}${PYTHON_SITEPACKAGES_DIR}/pycoral
}

FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/pycoral"

INSANE_SKIP:${PN} += "already-stripped"
