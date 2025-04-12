EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

LIC_FILES_CHKSUM = "file://iam/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/iam -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src \
        ${EXTRA_OECMAKE}
}

FILES:${PN} = " \
    /opt \
"
