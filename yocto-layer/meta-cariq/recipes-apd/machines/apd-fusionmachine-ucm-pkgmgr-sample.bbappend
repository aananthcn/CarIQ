# Previous lines remain unchanged
EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/sample-applications"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/sample-applications/:"

LICENSE = "AUTOSAR"
LIC_FILES_CHKSUM = "file://applications/UCM_pkgmgr_sample/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

# Debug the license variables using Python syntax
do_populate_lic:prepend() {
    bb.debug(1, "Applying .bbappend for apd-radarfusionmachine-ucm-pkgmgr-sample")
    bb.debug(1, "LICENSE: %s" % d.getVar("LICENSE"))
    bb.debug(1, "LIC_FILES_CHKSUM: %s" % d.getVar("LIC_FILES_CHKSUM"))
}

DEPENDS += "cmake-native ara-gen-native apd-applications-arxmls"

# Rest of the .bbappend remains unchanged
do_configure:prepend() {
    echo "EXTERNALSRC is set to: ${EXTERNALSRC}"
    ls ${EXTERNALSRC}/applications/UCM_pkgmgr_sample/
}

do_configure() {
    cmake -S ${S}/applications/UCM_pkgmgr_sample -B ${B} \
        --trace-expand \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -CMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -CMAKE_MODULE_PATH=${S}/../ara-api/apd/apd-cmake-modules/src \
        -CMAKE_PREFIX_PATH="${WORKDIR}/recipe-sysroot/usr;${WORKDIR}/recipe-sysroot/opt" \
        -Dara-gen_DIR="${TMPDIR}/sysroots-components/x86_64/ara-gen-native/usr/lib/cmake/ara-gen" \
        -Dapd-applications-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-applications-arxmls" \
        -Dapd-common-machine-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-common-machine-arxmls" \
        -Dapd-interfaces-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-interfaces-arxmls" \
        -Dapd-network-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-network-arxmls" \
        -Dapd-dlt-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-dlt-arxmls" \
        -Dapd-fusionmachine-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-fusionmachine-arxmls" \
        -Dara-adi-sensoritf-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-adi-sensoritf-arxmls" \
        -Dara-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-arxmls" \
        -Dara-com-lib_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-com-lib" \
        -Dara-core_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-core" \
        -Dara-exec_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-exec" \
        -Dara-log_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-log" \
        -DTARGET_MACHINE=FusionMachine \
        ${EXTRA_OECMAKE}
}

FILES:${PN} = " \
    /opt \
"

SYSROOT_DIRS += "/opt"

do_install:prepend() {
    install -d ${SYSROOT_DESTDIR}/opt
}

INSANE_SKIP:${PN} += "staticdev license-checksum"
INHIBIT_PACKAGE_LICENSE_CHECK = "1"