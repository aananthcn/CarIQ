DESCRIPTION = "Custom XFCE4 desktop configuration"
AUTHOR = "Aananth C N"
LICENSE = "CLOSED"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://cariq-background.jpg"
SRC_URI += "file://xfce4-desktop.xml"

FILES_${PN} += "${datadir}/backgrounds/*"

do_install:append() {
    # Install background image to a system-wide accessible directory
    install -d ${D}${datadir}/xfce4/desktop/
    install -m 0644 ${WORKDIR}/cariq-background.jpg ${D}${datadir}/xfce4/desktop/cariq-background.jpg

    # Create necessary directories if they do not exist
    install -d ${D}${sysconfdir}/xdg/xfce4/xfconf/xfce-perchannel-xml

    # Copy the configuration file to system-wide locations
    install -m 0644 ${WORKDIR}/xfce4-desktop.xml ${D}${sysconfdir}/xdg/xfce4/xfconf/xfce-perchannel-xml/xfce4-desktop.xml
}
