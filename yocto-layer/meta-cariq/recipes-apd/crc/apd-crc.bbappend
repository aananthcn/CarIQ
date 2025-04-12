EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

DEPENDS += "apd-cmake-modules-native boost python3-lxml"

LIC_FILES_CHKSUM = "file://apd/crc/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/apd/crc -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src
}

do_configure:class-native() {
    cmake -S ${S}/apd/crc -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot-native \
        -DCMAKE_INCLUDE_PATH=${WORKDIR}/recipe-sysroot-native/usr/include \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src
}

do_install() {
    echo "DEBUG: Running custom do_install for apd-crc" >&2
    install -d ${D}${libdir}
    install -m 0644 ${B}/src/libapd_crc.a ${D}${libdir}/libapd_crc.a || echo "ERROR: Failed to install libapd_crc.a" >&2

    install -d ${D}${includedir}/apd/crc
    install -m 0644 ${S}/apd/crc/include/public/apd/crc/*.h ${D}${includedir}/apd/crc/ || echo "ERROR: Failed to install headers" >&2

    install -d ${D}${libdir}/cmake/apd-crc
    cat << EOF > ${D}${libdir}/cmake/apd-crc/apd-crcConfig.cmake
# apd-crc CMake configuration file
if(NOT TARGET apd_crc)
    add_library(apd_crc STATIC IMPORTED)
    set_target_properties(apd_crc PROPERTIES
        IMPORTED_LOCATION "\${CMAKE_SYSROOT}/usr/lib/libapd_crc.a"
        INTERFACE_INCLUDE_DIRECTORIES "\${CMAKE_SYSROOT}/usr/include"
    )
endif()
if(NOT TARGET apd::crc)
    add_library(apd::crc ALIAS apd_crc)
endif()
EOF
}

# FILES:${PN} = " \
#     ${libdir}/libapd_crc.a \
#     ${libdir}/cmake/apd-crc/apd-crcConfig.cmake \
#     ${includedir}/apd/crc/*.h \
# "


FILES:${PN} = " \
    /opt \
"
