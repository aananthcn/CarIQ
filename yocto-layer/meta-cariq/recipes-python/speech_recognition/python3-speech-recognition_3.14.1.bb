SUMMARY = "Library for performing speech recognition, with support for several engines and APIs, online and offline."

DESCRIPTION = "Refer https://pypi.org/project/SpeechRecognition/#description"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=068c05417c59b84b9d04a04586c00da1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI  = "https://files.pythonhosted.org/packages/09/47/5dcfcd8a2c8c2981986fc196e98fc57bc1ecb5233b2d54dac0c0d448b019/SpeechRecognition-${PV}-py3-none-any.whl"
SRC_URI += "file://LICENSE.txt"

SRC_URI[sha256sum] = "2b5d16a7dce2dbf5f90d9c4d5aefe96325518abdc963059ec16dad9e4f2c09d3"

DEPENDS = "python3 unzip-native flac"

inherit python3native

S = "${WORKDIR}"

do_install() {
    # Extract the .whl file directly to the correct location
    mkdir -p ${D}${PYTHON_SITEPACKAGES_DIR}
    unzip -q ${WORKDIR}/SpeechRecognition-${PV}-py3-none-any.whl -d ${D}${PYTHON_SITEPACKAGES_DIR}

    # Remove incompatible precompiled x86 binaries
    rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/speech_recognition/flac-linux-x86
    rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/speech_recognition/flac-linux-x86_64

    # Symlink to the system FLAC binary (installed via flac_1.4.3.bb)
    ln -s /usr/bin/flac ${D}${PYTHON_SITEPACKAGES_DIR}/speech_recognition/flac-linux-aarch64

	# Remove the test files
	rm -rf ${D}${PYTHON_SITEPACKAGES_DIR}/tests

	# Renamve dist info
	mv ${D}${PYTHON_SITEPACKAGES_DIR}/SpeechRecognition-${PV}.dist-info ${D}${PYTHON_SITEPACKAGES_DIR}/speech_recognition-${PV}.dist-info
}

FILES:${PN} += " \
    ${PYTHON_SITEPACKAGES_DIR}/speech_recognition \
    ${PYTHON_SITEPACKAGES_DIR}/speech_recognition-${PV}.dist-info \
"

INSANE_SKIP:${PN} += "already-stripped"