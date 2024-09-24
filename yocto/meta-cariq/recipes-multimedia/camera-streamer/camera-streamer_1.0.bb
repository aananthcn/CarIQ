SUMMARY = "Camera streamer for multimedia over network"
DESCRIPTION = "Provides camera-sender and camera-player scripts based on cariq_node"

LICENSE = "CLOSED"
SRC_URI = "file://camera-sender.sh \
           file://camera-player.sh \
           file://camera-sender-en1.service \
           file://camera-sender-en2.service \
           file://camera-player.service \
           file://camera-udp-listener.c \
"


inherit systemd


S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

# To fix the hashvalue mismatch error: https://stackoverflow.com/questions/48762459/bitbake-the-metadata-is-not-deterministic-and-this-needs-to-be-fixed
CARIQ_NODE[vardepsexclude]=""


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
            d.setVar('CARIQ_NODE[vardepsexclude]', cariq_node)
        else:
            bb.fatal("CARIQ_NODE not defined (cariq_node = %s)" % cariq_node)

    except IOError as e:
        bb.fatal("IOError while reading the file: %s" % e)
    except Exception as e:
        bb.fatal("Unexpected error: %s" % e)
}


# Compile the C file
do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -o camera-udp-listener camera-udp-listener.c
}

# Install the files
do_install() {
    install -d ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}

    # Read the CARIQ_NODE from file
    CARIQ_NODE=$(cat ${TMPDIR}/cariq_node.txt)
    
    # Based on the cariq_node value, install the correct script and service
    if echo "${CARIQ_NODE}" | grep -q "ccn"; then
        install -m 0755 ${UNPACKDIR}/camera-player.sh ${D}${bindir}/camera-player
        install -m 0644 ${UNPACKDIR}/camera-player.service ${D}${systemd_system_unitdir}/camera-player.service
        install -m 0755 ${UNPACKDIR}/camera-udp-listener ${D}${bindir}/camera-udp-listener
    elif echo "${CARIQ_NODE}" | grep -q "en1"; then
        install -m 0755 ${UNPACKDIR}/camera-sender.sh ${D}${bindir}/camera-sender.sh
        install -m 0644 ${UNPACKDIR}/camera-sender-en1.service ${D}${systemd_system_unitdir}/camera-sender.service
    elif echo "${CARIQ_NODE}" | grep -q "en2"; then
        install -m 0755 ${UNPACKDIR}/camera-sender.sh ${D}${bindir}/camera-sender.sh
        install -m 0644 ${UNPACKDIR}/camera-sender-en2.service ${D}${systemd_system_unitdir}/camera-sender.service
    else
        bbwarn "Error: Unknown CARIQ_NODE value: ${CARIQ_NODE}"
        exit 1
    fi
}


# To auto enable the services so that it goes inside /etc/systemd/system/multi-user.target.wants/
# SYSTEMD_SERVICE:${PN} = "${@'camera-sender-en1.service' if d.getVar('CARIQ_NODE[vardepsexclude]') == 'en1' else 'camera-sender-en2.service' if d.getVar('CARIQ_NODE[vardepsexclude]') == 'en2' else 'camera-player.service'}"
SYSTEMD_SERVICE:${PN} = "${@'camera-sender.service' if d.getVar('CARIQ_NODE[vardepsexclude]') in ['en1', 'en2'] else 'camera-player@5001.service camera-player@5002.service camera-player.socket'}"


SYSTEMD_AUTO_ENABLE:${PN} = "enable"

# Tell bitbake that you are using this, else you get QA errors
FILES:${PN} = "\
    ${bindir} \
    ${systemd_unitdir}/system \
"
