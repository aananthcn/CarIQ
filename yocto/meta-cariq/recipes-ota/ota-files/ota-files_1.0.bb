DESCRIPTION = "OTA Updater Script and JSON Configuration"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=cc76c36d66f766d7bcdd525de37e4a65"

inherit allarch

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "file://ota-updater.sh"

S = "${WORKDIR}"

CARIQ_SW_VERSION ?= "0.0.0"

FILES_${PN} += "/usr/bin/ota-updater.sh /etc/ota/ota-config.json"

do_install() {
    # Copy the script to /usr/bin and make it executable
    install -d ${D}/usr/bin
    install -m 0755 ${S}/ota-updater.sh ${D}/usr/bin/ota-updater.sh

    # Create the /etc/ota directory and generate the JSON file
    install -d ${D}/etc/ota
    echo '{' > ${D}/etc/ota/ota-config.json
    echo '    "ota_version": "0.0",' >> ${D}/etc/ota/ota-config.json
    echo '    "sw_version": "'${CARIQ_SW_VERSION}'",' >> ${D}/etc/ota/ota-config.json
    echo '    "server_url": "'${CARIQ_OTASRV_URL}'",' >> ${D}/etc/ota/ota-config.json
    echo '    "cariq_node": "'${CARIQ_NODE}'"' >> ${D}/etc/ota/ota-config.json
    echo '}' >> ${D}/etc/ota/ota-config.json
}

RDEPENDS:${PN} = "bash"
