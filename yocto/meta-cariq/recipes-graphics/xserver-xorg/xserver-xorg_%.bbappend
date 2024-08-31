FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://xorg.conf"

do_install:append() {
    echo "Working directory is: ${WORKDIR}"
    echo "Sources unpack directory is: ${WORKDIR}/sources-unpack"
    ls ${WORKDIR}/sources-unpack
    echo "Installing xorg.conf..."
    install -d ${D}${sysconfdir}/X11/xorg.conf.d
    install -m 0644 ${WORKDIR}/sources-unpack/xorg.conf ${D}${sysconfdir}/X11/xorg.conf.d/xorg.conf || die "Failed to install xorg.conf"
}

