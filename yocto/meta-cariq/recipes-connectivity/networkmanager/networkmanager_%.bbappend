DESCRIPTION = "Default configuration to enable WiFi and Ethernet on both Compute Node and Edge Node"
AUTHOR = "Aananth C N"
LICENSE = "CLOSED"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://NetworkManager.conf"
SRC_URI += "file://ccn.eth0.nmconnection"
SRC_URI += "file://en1.eth0.nmconnection"
SRC_URI += "file://en2.eth0.nmconnection"


do_install:append() {
    install -d ${D}${sysconfdir}/NetworkManager
    install -d ${D}${sysconfdir}/NetworkManager/system-connections

    # Make all ethernet interfaces UP with pre-configured IPv4 address
    if echo "${CARIQ_NODE}" | grep -q "ccn"; then
        echo "Installing ccn.eth0.nmconnection for image with ccn feature"
        install -m 0600 ${WORKDIR}/ccn.eth0.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/ccn.eth0.nmconnection
    elif echo "${CARIQ_NODE}" | grep -q "en1"; then
        echo "Installing en1.eth0.nmconnection for image with en1 feature"
        install -m 0600 ${WORKDIR}/en1.eth0.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/en1.eth0.nmconnection
    elif echo "${CARIQ_NODE}" | grep -q "en2"; then
        echo "Installing en2.eth0.nmconnection for image with en2 feature"
        install -m 0600 ${WORKDIR}/en2.eth0.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/en2.eth0.nmconnection
    else
        bbfatal "ERROR: No matching image feature found in ${CARIQ_NODE}"
        exit 1
    fi

    # Set all devices as managed
    install -m 0600 ${WORKDIR}/NetworkManager.conf ${D}${sysconfdir}/NetworkManager/NetworkManager.conf
}

FILES_${PN} += "${sysconfdir}/NetworkManager/NetworkManager.conf"
