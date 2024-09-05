DESCRIPTION = "Default congiguration to enable WiFi and Ethernet on both Compute Node and Edge Node"
AUTHOR = "Aananth C N"
LICENSE = "CLOSED"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://NetworkManager.conf"

do_install:append() {
    install -d ${D}${sysconfdir}/NetworkManager
    install -m 0644 ${WORKDIR}/sources-unpack/NetworkManager.conf ${D}${sysconfdir}/NetworkManager/NetworkManager.conf
}
