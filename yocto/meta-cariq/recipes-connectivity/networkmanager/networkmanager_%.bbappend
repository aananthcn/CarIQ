DESCRIPTION = "Default configuration to enable WiFi and Ethernet on both Compute Node and Edge Node"
AUTHOR = "Aananth C N"
LICENSE = "CLOSED"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://NetworkManager.conf"
SRC_URI += "file://ccn.eth0.nmconnection"
SRC_URI += "file://en1.eth0.nmconnection"
SRC_URI += "file://en2.eth0.nmconnection"


python read_cariq_node() {
    import os

    # Define the shared directory where cariq_node.txt was written
    shared_dir = d.getVar('TMPDIR')
    file_path = os.path.join(shared_dir, 'cariq_node.txt')
    cariq_node = "None"

    try:
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

    except IOError as e:
        bb.fatal("IOError while reading the file: %s" % e)
    except Exception as e:
        bb.fatal("Unexpected error: %s" % e)
}

do_install[prefuncs] += "read_cariq_node"



do_install:append() {
    install -d ${D}${sysconfdir}/NetworkManager
    install -d ${D}${sysconfdir}/NetworkManager/system-connections

    # Make all ethernet interfaces UP with pre-configured IPv4 address
    if echo "${CARIQ_NODE}" | grep -q "ccn"; then
        echo "Installing ccn.eth0.nmconnection for image with ccn feature"
        install -m 0600 ${WORKDIR}/sources-unpack/ccn.eth0.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/ccn.eth0.nmconnection
    elif echo "${CARIQ_NODE}" | grep -q "en1"; then
        echo "Installing en1.eth0.nmconnection for image with en1 feature"
        install -m 0600 ${WORKDIR}/sources-unpack/en1.eth0.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/en1.eth0.nmconnection
    elif echo "${CARIQ_NODE}" | grep -q "en2"; then
        echo "Installing en2.eth0.nmconnection for image with en2 feature"
        install -m 0600 ${WORKDIR}/sources-unpack/en2.eth0.nmconnection ${D}${sysconfdir}/NetworkManager/system-connections/en2.eth0.nmconnection
    else:
        echo "ERROR: No matching image feature found in ${CARIQ_NODE}"
    fi

    # Set all devices as managed
    install -m 0600 ${WORKDIR}/sources-unpack/NetworkManager.conf ${D}${sysconfdir}/NetworkManager/NetworkManager.conf
}

FILES_${PN} += "${sysconfdir}/NetworkManager/NetworkManager.conf"
