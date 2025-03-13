EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"


do_configure() {
    cmake -S ${S} -B ${B} ${EXTRA_OECMAKE}
}