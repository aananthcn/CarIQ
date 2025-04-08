EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

DEPENDS += "apd-cmake-modules-native boost python3-lxml"

LIC_FILES_CHKSUM = "file://apd/crc/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/apd/crc -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/usr \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src \
        ${EXTRA_OECMAKE}
}

do_configure:class-native() {
    cmake -S ${S}/apd/crc -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/usr \
        -CMAKE_SYSROOT=${WORKDIR}/recipe-sysroot-native \
        -CMAKE_INCLUDE_PATH=${WORKDIR}/recipe-sysroot-native/usr/include \
        -CMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src \
        ${EXTRA_OECMAKE}
}

do_install() {
    echo "DEBUG: Running custom do_install for apd-crc" >&2
    install -d ${D}${libdir}
    install -m 0644 ${B}/src/libapd_crc.a ${D}${libdir}/libapd_crc.a || echo "ERROR: Failed to install libapd_crc.a" >&2
}

FILES:${PN} = " \
    ${libdir}/libapd_crc.a \
"