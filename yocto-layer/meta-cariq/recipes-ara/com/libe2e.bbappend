EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

DEPENDS += "apd-cmake-modules-native apd-crc"

LIC_FILES_CHKSUM = "file://com/libe2e/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/com/libe2e -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src \
        -DCMAKE_PREFIX_PATH=${WORKDIR}/recipe-sysroot/usr
}

do_install:append() {
    install -d ${D}${libdir}/cmake/e2e
    cat << EOF > ${D}${libdir}/cmake/e2e/e2e-config.cmake
# e2e CMake configuration file
if(NOT TARGET e2e)
    add_library(e2e SHARED IMPORTED)
    set_target_properties(e2e PROPERTIES
        IMPORTED_LOCATION "\${CMAKE_SYSROOT}/usr/lib/libe2e.so"
        INTERFACE_INCLUDE_DIRECTORIES "\${CMAKE_SYSROOT}/usr/include"
    )
endif()
EOF
}

# FILES:${PN}-dev += "${libdir}/cmake/e2e/e2e-config.cmake"

# Override malformed EXTRA_OECMAKE
EXTRA_OECMAKE = ""


FILES:${PN} = " \
    /opt \
"
