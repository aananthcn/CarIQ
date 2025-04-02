EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

LIC_FILES_CHKSUM = "file://apd/arxmls/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

DEPENDS += "apd-cmake-modules-native"

do_configure() {
    cmake -S ${S} -B ${B} -DCMAKE_INSTALL_PREFIX=${D}/usr ${EXTRA_OECMAKE}
}

do_install() {
    cmake --build ${B} --target install DESTDIR=${D}
}