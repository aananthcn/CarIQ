EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

LIC_FILES_CHKSUM = "file://apd/manifestreader/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

DEPENDS += "ara-core-types rapidjson boost gtest"

do_configure() {
    cmake -S ${S}/apd/manifestreader -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src \
        ${EXTRA_OECMAKE}
}

FILES:${PN} += " \
    /opt/lib/cmake/apd-manifestreader/* \
    /opt/lib/lib*.so* \
    /opt/lib/lib*.a \
    /opt/lib/pkgconfig/* \
    /opt/include/apd/* \
    /opt/bin/* \
"

FILES:${PN}-test = "/opt/bintest/*"

# Skip staticdev QA check since .a files are used in production
INSANE_SKIP:${PN} += "staticdev"

# Ensure /opt is staged to sysroot for dependents
SYSROOT_DIRS += "/opt"

do_install:prepend() {
    install -d ${SYSROOT_DESTDIR}/opt
}