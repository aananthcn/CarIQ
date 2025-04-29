EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

LIC_FILES_CHKSUM = "file://ucm/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/ucm/pkgmgr -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/../ara-api/apd/apd-cmake-modules/src \
        -Dara-gen_DIR="${TMPDIR}/sysroots-components/x86_64/ara-gen-native/usr/lib/cmake/ara-gen" \
        ${EXTRA_OECMAKE}
}

FILES:${PN} += " \
    /opt \
"

INSANE_SKIP:${PN} += "staticdev"

RDEPENDS:${PN} += "bash"

# Ensure /opt is staged to sysroot
SYSROOT_DIRS += "/opt"

do_install:prepend() {
    install -d ${SYSROOT_DESTDIR}/opt
}
