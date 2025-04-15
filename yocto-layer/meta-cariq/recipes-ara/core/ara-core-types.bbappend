EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

DEPENDS += "apd-cmake-modules-native apd-testutils"

LIC_FILES_CHKSUM = "file://core/core-types/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/core/core-types -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src \
        -DCMAKE_PREFIX_PATH=${WORKDIR}/recipe-sysroot/opt \
        ${EXTRA_OECMAKE}
}

# do_install:append() {
#     install -d ${D}/opt/lib/cmake/ara-core-types
#     cat << EOF > ${D}/opt/lib/cmake/ara-core-types/ara-core-typesConfig.cmake
# # ara-core-types CMake configuration file
# if(NOT TARGET ara-core-types)
#     add_library(ara-core-types STATIC IMPORTED)
#     set_target_properties(ara-core-types PROPERTIES
#         IMPORTED_LOCATION "\${CMAKE_SYSROOT}/opt/lib/libara_core_types.a"
#         INTERFACE_INCLUDE_DIRECTORIES "\${CMAKE_SYSROOT}/opt/include"
#         INTERFACE_LINK_LIBRARIES "Threads::Threads"
#     )
# endif()
# if(NOT TARGET ara::core-types)
#     add_library(ara::core-types ALIAS ara-core-types)
# endif()
# find_package(Threads REQUIRED)
# EOF
# }

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

# Ensure /opt is staged to sysroot
SYSROOT_DIRS += "/opt"

do_install:prepend() {
    install -d ${SYSROOT_DESTDIR}/opt
}