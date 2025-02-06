SUMMARY = "pydub is a text-to-speech conversion library in Python"

DESCRIPTION = "https://pypi.org/project/pydub/#description"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI  = "https://files.pythonhosted.org/packages/a6/53/d78dc063216e62fc55f6b2eebb447f6a4b0a59f55c8406376f76bf959b08/pydub-${PV}-py2.py3-none-any.whl"
SRC_URI += "file://LICENSE.txt"
SRC_URI[sha256sum] = "65617e33033874b59d87db603aa1ed450633288aefead953b30bded59cb599a6"

DEPENDS = "python3 unzip-native"

inherit python3native

S = "${WORKDIR}"

do_install() {
    # Extract the .whl file directly to the correct location
    mkdir -p ${D}${PYTHON_SITEPACKAGES_DIR}
    python3 -m zipfile -e ${WORKDIR}/pydub-${PV}-py2.py3-none-any.whl ${D}${PYTHON_SITEPACKAGES_DIR}
}

FILES:${PN} += " \
    ${PYTHON_SITEPACKAGES_DIR}/pydub \
    ${PYTHON_SITEPACKAGES_DIR}/pydub-${PV}.dist-info \
"

INSANE_SKIP:${PN} += "already-stripped"