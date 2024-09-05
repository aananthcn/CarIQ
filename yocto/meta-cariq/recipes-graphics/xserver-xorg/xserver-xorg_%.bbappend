DESCRIPTION = "Configuration to limit the HDMI display resolution to <= 1920x1080"
AUTHOR = "Aananth C N"
LICENSE = "CLOSED"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://xorg.conf"

do_install:append() {
    install -d ${D}${sysconfdir}/X11/xorg.conf.d
    install -m 0644 ${WORKDIR}/sources-unpack/xorg.conf ${D}${sysconfdir}/X11/xorg.conf.d/xorg.conf || die "Failed to install xorg.conf"
}

