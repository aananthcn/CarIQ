EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

DEPENDS += "apd-cmake-modules-native"

LIC_FILES_CHKSUM = "file://apd/arxmls/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/apd/arxmls -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/../ara-api/apd/apd-cmake-modules/src \
        ${EXTRA_OECMAKE}
}

FILES:${PN} = " \
    /opt \
"
