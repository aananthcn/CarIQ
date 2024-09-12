SUMMARY = "Camera streamer for multimedia over network"
DESCRIPTION = "Provides camera-sender and camera-player scripts based on cariq_node"

LICENSE = "CLOSED"
SRC_URI = "file://camera-sender.sh \
           file://camera-player.sh \
           file://camera-sender-en1.service \
           file://camera-sender-en2.service \
           file://camera-player@.service \
           file://camera-player.socket"

inherit systemd

python () {
    import os

    shared_dir = d.getVar('TMPDIR')
    file_path = os.path.join(shared_dir, 'cariq_node.txt')
    cariq_node = "None"

    try:
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


do_install() {
    install -d ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}
    
    # Based on the cariq_node value, install the correct script and service
    if echo "${CARIQ_NODE}" | grep -q "ccn"; then
        install -m 0755 ${WORKDIR}/sources-unpack/camera-player.sh ${D}${bindir}/camera-player.sh
        install -m 0644 ${WORKDIR}/sources-unpack/camera-player@.service ${D}${systemd_system_unitdir}/camera-player@.service
        install -m 0644 ${WORKDIR}/sources-unpack/camera-player.socket ${D}${systemd_system_unitdir}/camera-player.socket
    elif echo "${CARIQ_NODE}" | grep -q "en1"; then
        install -m 0755 ${WORKDIR}/sources-unpack/camera-sender.sh ${D}${bindir}/camera-sender.sh
        install -m 0644 ${WORKDIR}/sources-unpack/camera-sender-en1.service ${D}${systemd_system_unitdir}/camera-sender.service
    elif echo "${CARIQ_NODE}" | grep -q "en2"; then
        install -m 0755 ${WORKDIR}/sources-unpack/camera-sender.sh ${D}${bindir}/camera-sender.sh
        install -m 0644 ${WORKDIR}/sources-unpack/camera-sender-en2.service ${D}${systemd_system_unitdir}/camera-sender.service
    fi
}


# To auto enable the services so that it goes inside /etc/systemd/system/multi-user.target.wants/
# SYSTEMD_SERVICE:${PN} = "${@'camera-sender.service' if d.getVar('CARIQ_NODE') in ['en1', 'en2'] else 'camera-player@.service camera-player.socket'}"
SYSTEMD_SERVICE:${PN} = "${@'camera-sender.service' if d.getVar('CARIQ_NODE') in ['en1', 'en2'] else 'camera-player@5001.service camera-player@5002.service camera-player.socket'}"

SYSTEMD_AUTO_ENABLE:${PN} = "enable"

# Tell bitbake that you are using this, else you get QA errors
FILES:${PN} = "\
    ${bindir} \
    ${systemd_unitdir}/system \
"
