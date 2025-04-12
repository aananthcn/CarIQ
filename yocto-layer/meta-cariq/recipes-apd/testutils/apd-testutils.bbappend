EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

DEPENDS += "apd-cmake-modules-native boost python3-native python3-lxml-native ara-gen-native"

LIC_FILES_CHKSUM = "file://apd/testutils/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/apd/testutils -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src \
        ${EXTRA_OECMAKE}
}

do_install:append() {
    install -d ${D}/opt/lib/cmake/apd-testutils
    cat << EOF > ${D}/opt/lib/cmake/apd-testutils/apd-testutilsConfig.cmake
# apd-testutils CMake configuration file
if(NOT TARGET apd-testutils)
    add_library(apd-testutils INTERFACE IMPORTED)
    set_target_properties(apd-testutils PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "\${CMAKE_SYSROOT}/opt/include"
    )
endif()
if(NOT TARGET apd::testutils)
    add_library(apd::testutils ALIAS apd-testutils)
endif()
EOF
}

FILES:${PN} += " \
    /opt/lib/cmake/apd-testutils/* \
    /opt/lib/pkgconfig/* \
    /opt/include/apd/test/* \
"

FILES:${PN}-test = "/opt/bintest/*"

SYSROOT_DIRS += "/opt"

do_install:prepend() {
    install -d ${SYSROOT_DESTDIR}/opt
}