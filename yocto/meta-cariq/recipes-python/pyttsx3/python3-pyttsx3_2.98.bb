SUMMARY = "pyttsx3 is a text-to-speech conversion library in Python"

DESCRIPTION = "https://pypi.org/project/pyttsx3/#description"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI  = "https://files.pythonhosted.org/packages/94/df/e1584757c736c4fba09a3fb4f22fe625cc3367b06c6ece221e4b8c1e3023/pyttsx3-${PV}-py3-none-any.whl"
SRC_URI += "file://LICENSE.txt"
SRC_URI[sha256sum] = "b3fb4ca4d5ae4f8e6836d6b37bf5fee0fd51d157ffa27fb9064be6e7be3da37a"

DEPENDS = "python3 unzip-native"

inherit python3native

S = "${WORKDIR}"

do_install() {
    # Extract the .whl file directly to the correct location
    mkdir -p ${D}${PYTHON_SITEPACKAGES_DIR}
    unzip -q ${WORKDIR}/pyttsx3-${PV}-py3-none-any.whl -d ${D}${PYTHON_SITEPACKAGES_DIR}
}

FILES:${PN} += " \
    ${PYTHON_SITEPACKAGES_DIR}/pyttsx3 \
    ${PYTHON_SITEPACKAGES_DIR}/pyttsx3-${PV}.dist-info \
"

INSANE_SKIP:${PN} += "already-stripped"