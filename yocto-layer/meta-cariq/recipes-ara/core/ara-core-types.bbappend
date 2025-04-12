EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

DEPENDS += "apd-cmake-modules-native apd-testutils"

LIC_FILES_CHKSUM = "file://core/core-types/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    echo "DEBUG: CMAKE_PREFIX_PATH=${WORKDIR}/recipe-sysroot/opt" > ${WORKDIR}/cmake_debug.log
    ls -l ${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-testutils >> ${WORKDIR}/cmake_debug.log 2>&1
    cmake -S ${S}/core/core-types -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src \
        -DCMAKE_PREFIX_PATH=${WORKDIR}/recipe-sysroot/opt \
        ${EXTRA_OECMAKE}
}

FILES:${PN} += " \
    /opt/lib/cmake/ara-core-types/* \
    /opt/lib/lib*.so* \
    /opt/lib/lib*.a \
    /opt/lib/pkgconfig/* \
    /opt/include/ara/* \
"

FILES:${PN}-test = "/opt/bintest/*"

# Skip staticdev QA check since .a files are used in production
INSANE_SKIP:${PN} += "staticdev"