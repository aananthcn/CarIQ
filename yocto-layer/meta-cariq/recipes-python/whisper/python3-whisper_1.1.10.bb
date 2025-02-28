SUMMARY = "One of three components within the Graphite project"

DESCRIPTION = "Refer https://pypi.org/project/whisper/"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI  = "file://whisper-${PV}-py3-none-any.whl"
SRC_URI += "file://LICENSE.txt"

DEPENDS = "python3 unzip-native"

inherit python3native

S = "${WORKDIR}"

do_install() {
    # Extract the .whl file directly to the correct location
    mkdir -p ${D}${PYTHON_SITEPACKAGES_DIR}
    unzip -q ${WORKDIR}/whisper-${PV}-py3-none-any.whl -d ${D}${PYTHON_SITEPACKAGES_DIR}/whisper
}

FILES:${PN} += " \
    ${PYTHON_SITEPACKAGES_DIR}/whisper \
    ${PYTHON_SITEPACKAGES_DIR}/whisper-${PV}.dist-info \
"

INSANE_SKIP:${PN} += "already-stripped"