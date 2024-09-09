DESCRIPTION = "Default configuration to enable WiFi and Ethernet on both Compute Node and Edge Node"
AUTHOR = "Aananth C N"
LICENSE = "CLOSED"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://NetworkManager.conf"
SRC_URI += "file://NetworkManager-CCN.conf"
SRC_URI += "file://NetworkManager-EN1.conf"
SRC_URI += "file://NetworkManager-EN2.conf"


python read_cariq_node() {
    import os

    # Define the shared directory where cariq_node.txt was written
    shared_dir = d.getVar('TMPDIR')
    file_path = os.path.join(shared_dir, 'cariq_node.txt')
    cariq_node = "None"

    # Check if the file exists and read its contents
    if os.path.exists(file_path):
        with open(file_path, 'r') as f:
            cariq_node = f.read().strip()
        bb.note("CARIQ_NODE read from file: %s" % cariq_node)
    else:
        bb.fatal("cariq_node.txt file not found at %s" % file_path)

    if cariq_node and cariq_node != "None":
        d.setVar('CARIQ_NODE', cariq_node)
    else:
        bb.fatal("CARIQ_NODE not defined (cariq_node = %s)" % cariq_node)
}

do_install[prefuncs] += "read_cariq_node"



do_install:append() {
    install -d ${D}${sysconfdir}/NetworkManager

    # Select the appropriate NetworkManager.conf based on the CARIQ_NODE
    if echo "${CARIQ_NODE}" | grep -q "en1"; then
        echo "Installing NetworkManager-EN1.conf for image with en1 feature"
        install -m 0644 ${WORKDIR}/sources-unpack/NetworkManager-EN1.conf ${D}${sysconfdir}/NetworkManager/NetworkManager.conf
    elif echo "${CARIQ_NODE}" | grep -q "ccn"; then
        echo "Installing NetworkManager-CCN.conf for image with ccn feature"
        install -m 0644 ${WORKDIR}/sources-unpack/NetworkManager-CCN.conf ${D}${sysconfdir}/NetworkManager/NetworkManager.conf
    elif echo "${CARIQ_NODE}" | grep -q "en2"; then
        echo "Installing NetworkManager-EN2.conf for image with en2 feature"
        install -m 0644 ${WORKDIR}/sources-unpack/NetworkManager-EN2.conf ${D}${sysconfdir}/NetworkManager/NetworkManager.conf
    else:
        echo "ERROR: No matching image feature found in ${CARIQ_NODE}"
        install -m 0644 ${WORKDIR}/sources-unpack/NetworkManager.conf ${D}${sysconfdir}/NetworkManager/NetworkManager.conf
    fi
}

FILES_${PN} += "${sysconfdir}/NetworkManager/NetworkManager.conf"
