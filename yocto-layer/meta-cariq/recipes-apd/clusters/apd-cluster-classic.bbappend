EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/sample-applications"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/sample-applications/:"

DEPENDS += "apd-cmake-modules-native ara-gen-native"

LIC_FILES_CHKSUM = "file://clusters/Classic/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    echo "DEBUG: ara-gen_DIR=${TMPDIR}/sysroots-components/x86_64/ara-gen-native/usr/lib/cmake/ara-gen" >> ${WORKDIR}/cmake_debug.log

    cmake -S ${S}/clusters/Classic -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/../ara-api/apd/apd-cmake-modules/src \
        -DCMAKE_PREFIX_PATH="${WORKDIR}/recipe-sysroot/opt:${WORKDIR}/recipe-sysroot-native/usr" \
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
