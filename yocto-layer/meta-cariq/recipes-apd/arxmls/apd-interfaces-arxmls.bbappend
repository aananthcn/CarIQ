EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

LIC_FILES_CHKSUM = "file://apd/minimal-machine/interfaces/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

do_configure() {
    cmake -S ${S}/apd/minimal-machine/interfaces -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/apd/apd-cmake-modules/src \
        ${EXTRA_OECMAKE}
}

do_install:append() {
    # Install ARXML files
    install -d ${D}${datadir}/apd-interfaces-arxmls
    install -m 0644 ${EXTERNALSRC}/../sample-applications/interfaces/*.arxml ${D}${datadir}/apd-interfaces-arxmls/

    # Install CMake config file
    install -d ${D}${libdir}/cmake/apd-interfaces-arxmls
    cat > ${D}${libdir}/cmake/apd-interfaces-arxmls/apd-interfaces-arxmlsConfig.cmake << EOF
# apd-interfaces-arxmlsConfig.cmake
set(apd-interfaces-arxmls_FOUND TRUE)
set(APD_INTERFACES_ARXMLS_DIR "\${CMAKE_CURRENT_LIST_DIR}/../../../share/apd-interfaces-arxmls")
EOF
}

FILES:${PN} += " \
    /opt \
"

# Ensure /opt is staged to sysroot
SYSROOT_DIRS += "/opt"

do_install:prepend() {
    install -d ${SYSROOT_DESTDIR}/opt
}

INSANE_SKIP:${PN} += "staticdev"
