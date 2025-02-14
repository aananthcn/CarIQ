FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://tigervnc.service"
SRC_URI += "file://xstartup"
SRC_URI += "file://vncpasswd"

DEPENDS += "xserver-xorg"

do_install:append() {
    # Install systemd service file
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/tigervnc.service ${D}${systemd_system_unitdir}

    # Setup session and password
    install -d ${D}/home/root/.vnc
    install -m 0755 ${WORKDIR}/xstartup ${D}/home/root/.vnc/xstartup
    install -m 0600 ${WORKDIR}/vncpasswd ${D}/home/root/.vnc/passwd

    # Manually enable the service
    install -d ${D}/etc/systemd/system/multi-user.target.wants/
    ln -sf ${systemd_system_unitdir}/tigervnc.service ${D}/etc/systemd/system/multi-user.target.wants/tigervnc.service
}

FILES:${PN} += "/home/root/.vnc/passwd"
FILES:${PN} += "/home/root/.vnc/xstartup"
FILES:${PN} += "${systemd_system_unitdir}/tigervnc.service"

SYSTEMD_SERVICE:${PN} = "tigervnc.service"
