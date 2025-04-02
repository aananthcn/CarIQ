EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

DEPENDS += "apd-cmake-modules-native"

do_configure() {
    cmake -S ${S} -B ${B} -DCMAKE_INSTALL_PREFIX=${D}/usr ${EXTRA_OECMAKE}
}

do_install() {
    cmake --build ${B} --target install DESTDIR=${D}
}
