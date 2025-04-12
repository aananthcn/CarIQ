EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

LIC_FILES_CHKSUM = "file://log/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

# do_configure:prepend() {
#     rm -rf ${B}/*
#     export PKG_CONFIG_PATH="${WORKDIR}/recipe-sysroot-native/usr/lib/pkgconfig:${PKG_CONFIG_PATH}"
# }

do_configure() {
    cmake -S ${S}/log -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src \
        ${EXTRA_OECMAKE}
}

FILES:${PN} = " \
    /opt \
"
