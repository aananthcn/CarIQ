DESCRIPTION = "OTA Updater Script and JSON Configuration"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=cc76c36d66f766d7bcdd525de37e4a65"

inherit allarch

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI  = "file://ota-updater-ccn.sh"
SRC_URI += "file://ota-updater-en1.sh"
SRC_URI += "file://ota-updater-en2.sh"
SRC_URI += "file://ota-checker.py"

S = "${WORKDIR}"

CARIQ_SW_VERSION ?= "0.0.0"

FILES_${PN} += "/usr/bin/ota-updater.sh /usr/bin/ota-update-manager.py /etc/ota/ota-config.json"

do_install() {
    # Determine the correct ota-updater script based on CARIQ_NODE
    case "${CARIQ_NODE}" in
        "ccn")
            OTA_UPDATER_SCRIPT="ota-updater-ccn.sh"
            ;;
        "en1")
            OTA_UPDATER_SCRIPT="ota-updater-en1.sh"
            ;;
        "en2")
            OTA_UPDATER_SCRIPT="ota-updater-en2.sh"
            ;;
        *)
            bberror "Unsupported CARIQ_NODE value: ${CARIQ_NODE}. Must be 'ccn', 'en1', or 'en2'."
            exit 1
            ;;
    esac

    # Create /usr/bin directory and install the selected script
    install -d ${D}/usr/bin
    install -m 0755 ${S}/${OTA_UPDATER_SCRIPT} ${D}/usr/bin/ota-updater.sh
    install -m 0755 ${S}/ota-checker.py ${D}/usr/bin/ota-checker.py

    # Create /etc/ota directory and generate the JSON file
    install -d ${D}/etc/ota

    # Read MACHINE variable from the build system
    MACHINE_NAME="${MACHINE}"

    # Create the JSON file line by line
    echo '{' > ${D}/etc/ota/ota-config.json
    echo '    "ota_version": "0.0",' >> ${D}/etc/ota/ota-config.json
    echo '    "sw_version": "'${CARIQ_SW_VERSION}'",' >> ${D}/etc/ota/ota-config.json
    echo '    "server_url": "'${CARIQ_OTASRV_URL}'",' >> ${D}/etc/ota/ota-config.json
    echo '    "cariq_node": "'${CARIQ_NODE}'",' >> ${D}/etc/ota/ota-config.json
    echo '    "machine_nm": "'${MACHINE_NAME}'"' >> ${D}/etc/ota/ota-config.json
    echo '}' >> ${D}/etc/ota/ota-config.json
}

RDEPENDS:${PN} = "bash python3"
