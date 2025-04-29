EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/sample-applications"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/sample-applications/:"

LIC_FILES_CHKSUM = "file://applications/UCM_update_adapter/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/applications/UCM_update_adapter -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/../ara-api/apd/apd-cmake-modules/src \
        -Dara-gen_DIR="${TMPDIR}/sysroots-components/x86_64/ara-gen-native/usr/lib/cmake/ara-gen" \
        ${EXTRA_OECMAKE}
}

FILES:${PN} = " \
    /opt \
"

SYSROOT_DIRS += "/opt"

do_install:prepend() {
    install -d ${SYSROOT_DESTDIR}/opt
}

INSANE_SKIP:${PN} += "staticdev"
