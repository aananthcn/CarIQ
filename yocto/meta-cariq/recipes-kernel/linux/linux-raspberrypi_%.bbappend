FILESEXTRAPATHS:prepend := "${THISDIR}/files-enx:"

SRC_URI += "file://defconfig_enx"


do_configure:prepend() {
    # Ensure CarIQ's defconfig is used before kernel configuration starts
    cp ${WORKDIR}/defconfig_enx ${S}/arch/arm64/configs/defconfig
}

do_configure:append() {
    # Forcefully set .config before 'olddefconfig' overwrites it
    cp ${S}/arch/arm64/configs/defconfig ${B}/.config
}
