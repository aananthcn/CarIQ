EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

DEPENDS += "apd-cmake-modules-native boost python3-native python3-lxml-native ara-gen-native"

LIC_FILES_CHKSUM = "file://apd/testutils/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S} -B ${B} -DCMAKE_INSTALL_PREFIX=${D}/usr ${EXTRA_OECMAKE}
}

do_install:append() {
    cmake --build ${B} --target install DESTDIR=${D}
    install -d ${D}${libdir}/python3.10/site-packages
    cp -r ${STAGING_DIR_NATIVE}${libdir}/python3.10/site-packages/lxml ${D}${libdir}/python3.10/site-packages/
}