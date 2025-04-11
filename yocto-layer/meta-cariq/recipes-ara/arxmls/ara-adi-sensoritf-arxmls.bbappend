EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

LIC_FILES_CHKSUM = "file://adi/sensoritf/arxmls/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

DEPENDS += "apd-cmake-modules-native python3-native python3-lxml-native python3 python3-lxml"

do_configure() {
    cmake -S ${S}/adi/sensoritf/arxmls -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/usr \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/../ara-api/apd/apd-cmake-modules/src \
        ${EXTRA_OECMAKE}
}