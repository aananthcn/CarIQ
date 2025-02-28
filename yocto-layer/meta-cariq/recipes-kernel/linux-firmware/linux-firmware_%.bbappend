DESCRIPTION = "A workaround for fixing the WiFi issue for Khadas VIM3"
AUTHOR = "Aananth C N"

COMPATIBLE_MACHINE = "^khadas-vim3$"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://reload-firmware.service"

inherit systemd


do_install:append() {
    install -d ${D}${systemd_system_unitdir}

    install -d ${D}${base_libdir}/firmware/brcm
    ln -sf brcmfmac4359-sdio.bin ${D}${base_libdir}/firmware/brcm/brcmfmac4359-sdio.khadas,vim3.bin

    install -m 0644 ${WORKDIR}/reload-firmware.service ${D}${systemd_system_unitdir}/reload-firmware.service
}


# To auto enable the services so that it goes inside /etc/systemd/system/multi-user.target.wants/
SYSTEMD_SERVICE:${PN} = "reload-firmware.service"

SYSTEMD_AUTO_ENABLE:${PN} = "enable"