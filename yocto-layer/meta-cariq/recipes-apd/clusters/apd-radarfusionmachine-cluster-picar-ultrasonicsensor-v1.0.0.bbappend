EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/sample-applications"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/sample-applications/:"

LIC_FILES_CHKSUM = "file://clusters/Ultrasonicsensor/v1.0.0/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

DEPENDS += "apd-cmake-modules-native ara-gen-native apd-interfaces-arxmls \
    ara-core ara-core-types apd-manifestreader ara-log ara-arxmls ara-ucm \
    ara-com-lib ara-exec vsomeip apd-wrsomeip libe2e libe2exf apd-crc \
    apd-applications-arxmls apd-common-machine-arxmls ara-adi-sensoritf-arxmls \
    apd-network-arxmls apd-dlt-arxmls apd-cluster-arxmls lib-apd-platform \
    apd-radarfusionmachine-arxmls \
"


do_configure() {
    echo "DEBUG: Configuring apd-radarfusionmachine-cluster-picar-ultrasonicsensor-v1.0.0 with sysroot=${WORKDIR}/recipe-sysroot" > ${WORKDIR}/cmake_debug.log
    echo "DEBUG: ara-core_DIR=${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-core" >> ${WORKDIR}/cmake_debug.log
    ls -l ${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-core >> ${WORKDIR}/cmake_debug.log 2>&1 || true
    # Clear CMake cache
    rm -rf ${B}/CMakeCache.txt ${B}/CMakeFiles
    cmake -S ${S}/clusters/Ultrasonicsensor/v1.0.0 -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=/opt \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        -DCMAKE_MODULE_PATH=${S}/../ara-api/apd/apd-cmake-modules/src \
        -DCMAKE_PREFIX_PATH="${WORKDIR}/recipe-sysroot/opt:${WORKDIR}/recipe-sysroot/usr" \
        -Dara-gen_DIR="${TMPDIR}/sysroots-components/x86_64/ara-gen-native/usr/lib/cmake/ara-gen" \
        -DAPD_INTERFACES_ARXMLS_DIR="${WORKDIR}/recipe-sysroot/opt/share/apd-interfaces-arxmls" \
        -Dara-com_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-com" \
        -Dvsomeip_DIR="${WORKDIR}/recipe-sysroot/usr/lib/cmake/vsomeip" \
        -De2e_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/e2e" \
        -De2exf_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/e2exf" \
        -Dapd-wrsomeip_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-wrsomeip" \
        -Dapd-crc_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-crc" \
        -Dara-core_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-core" \
        -Dara-core-types_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-core-types" \
        -Dara-log_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-log" \
        -Dara-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-arxmls" \
        -Dara-ucm_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-ucm" \
        -Dara-exec-execution-client_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-exec-execution-client" \
        -Dara-adi-sensoritf-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ara-adi-sensoritf-arxmls" \
        -Dapd-manifestreader_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-manifestreader" \
        -Dapd-applications-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-applications-arxmls" \
        -Dapd-common-machine-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-common-machine-arxmls" \
        -Dapd-network-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-network-arxmls" \
        -Dapd-dlt-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-dlt-arxmls" \
        -Dapd-cluster-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-cluster-arxmls" \
        -DApdPlatform_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/ApdPlatform" \
        -Dapd-radarfusionmachine-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-radarfusionmachine-arxmls" \
        -Dapd-interfaces-arxmls_DIR="${WORKDIR}/recipe-sysroot/opt/lib/cmake/apd-interfaces-arxmls" \
        -DBoost_INCLUDE_DIR="${WORKDIR}/recipe-sysroot/usr/include" \
        ${EXTRA_OECMAKE}
}

FILES:${PN} += " \
    /opt \
"

SYSROOT_DIRS += "/opt"

do_install:prepend() {
    install -d ${SYSROOT_DESTDIR}/opt
}

INSANE_SKIP:${PN} += "staticdev"

EXTRA_OECMAKE = ""