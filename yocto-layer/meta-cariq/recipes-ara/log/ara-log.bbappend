EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

LIC_FILES_CHKSUM = "file://log/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure:prepend() {
    rm -rf ${B}/*
    export PKG_CONFIG_PATH="${WORKDIR}/recipe-sysroot-native/usr/lib/pkgconfig:${PKG_CONFIG_PATH}"
}