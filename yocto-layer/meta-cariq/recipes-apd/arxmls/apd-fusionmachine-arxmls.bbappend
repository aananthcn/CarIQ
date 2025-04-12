EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/sample-applications"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/sample-applications/:"

LIC_FILES_CHKSUM = "file://machines/FusionMachine/model/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/machines/FusionMachine/model -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        ${EXTRA_OECMAKE}
}

FILES:${PN} = " \
    /opt \
"
