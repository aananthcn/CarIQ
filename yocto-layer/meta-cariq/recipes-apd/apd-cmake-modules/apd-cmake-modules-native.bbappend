EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"


do_configure() {
    cmake -S ${S} -B ${B} ${EXTRA_OECMAKE}
}

do_install() {
    install -d ${D}${libdir}/cmake/AUTOSAR
    install -m 0644 ${S}/apd/apd-cmake-modules/src/AUTOSAR/*.cmake ${D}${libdir}/cmake/AUTOSAR/
}