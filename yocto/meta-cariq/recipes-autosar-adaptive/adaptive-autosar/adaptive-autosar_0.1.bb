SUMMARY = "Adaptive-AUTOSAR: The fork of Armin Kassemi Langroodi's work!"
HOMEPAGE = "https://github.com/aananthcn/Adaptive-AUTOSAR"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9faf1714c2d4423fdb0d0cce55269ae3"

SRC_URI = "git://github.com/aananthcn/Adaptive-AUTOSAR.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

# Version string incorporating the revision
PV = "0.1+git${SRCPV}"

# If the build requires CMake, we need the cmake-native tool
DEPENDS = "cmake-native googletest async-bsd-socket-lib jsoncpp curl doip-lib obd2-emulator"

# Inherit the CMake class to handle configuration and build steps
inherit cmake

# Point to the correct source directory (for a git repository, typically "git")
S = "${WORKDIR}/git"

# Pass C++14 standard flag to the compiler
EXTRA_OECMAKE += "-DCMAKE_CXX_STANDARD=14 -DCMAKE_CXX_STANDARD_REQUIRED=ON"
EXTRA_OECMAKE += "-Dbuild_tests=OFF -DBUILD_SHARED_LIBS=ON"
EXTRA_OECMAKE += "-DCMAKE_FIND_ROOT_PATH=${STAGING_DIR_TARGET}"



do_install() {
    # Create the directory where the binaries should be installed.
    install -d ${D}${bindir}
    # Install the binary. Adjust the path if your build directory is different.
    install -m 0755 ${B}/adaptive_autosar ${D}${bindir}

    # Create the directory where the library should be installed.
    install -d ${D}${libdir}

    # Install libara_com.so
    install -m 0644 ${B}/libara_com.so ${B}/libara_com.so.0.1
    install -m 0644 ${B}/libara_com.so.0.1 ${D}${libdir}/libara_com.so.0.1
    ln -sf libara_com.so.0.1 ${D}${libdir}/libara_com.so.0
    ln -sf libara_com.so.0.1 ${D}${libdir}/libara_com.so
    rm -rf ${B}/libara_com.so.0.1

    # Install libara_diag.so
    install -m 0644 ${B}/libara_diag.so ${B}/libara_diag.so.0.1
    install -m 0644 ${B}/libara_diag.so.0.1 ${D}${libdir}/libara_diag.so.0.1
    ln -sf libara_diag.so.0.1 ${D}${libdir}/libara_diag.so.0
    ln -sf libara_diag.so.0.1 ${D}${libdir}/libara_diag.so
    rm -rf ${B}/libara_diag.so.0.1

    # Install libara_log.so
    install -m 0644 ${B}/libara_log.so ${B}/libara_log.so.0.1
    install -m 0644 ${B}/libara_log.so.0.1 ${D}${libdir}/libara_log.so.0.1
    ln -sf libara_log.so.0.1 ${D}${libdir}/libara_log.so.0
    ln -sf libara_log.so.0.1 ${D}${libdir}/libara_log.so
    rm -rf ${B}/libara_log.so.0.1

    # Install libara_sm.so
    install -m 0644 ${B}/libara_sm.so ${B}/libara_sm.so.0.1
    install -m 0644 ${B}/libara_sm.so.0.1 ${D}${libdir}/libara_sm.so.0.1
    ln -sf libara_sm.so.0.1 ${D}${libdir}/libara_sm.so.0
    ln -sf libara_sm.so.0.1 ${D}${libdir}/libara_sm.so
    rm -rf ${B}/libara_sm.so.0.1

    # Install libara_core.so
    install -m 0644 ${B}/libara_core.so ${B}/libara_core.so.0.1
    install -m 0644 ${B}/libara_core.so.0.1 ${D}${libdir}/libara_core.so.0.1
    ln -sf libara_core.so.0.1 ${D}${libdir}/libara_core.so.0
    ln -sf libara_core.so.0.1 ${D}${libdir}/libara_core.so
    rm -rf ${B}/libara_core.so.0.1

    # Install libara_exec.so
    install -m 0644 ${B}/libara_exec.so ${B}/libara_exec.so.0.1
    install -m 0644 ${B}/libara_exec.so.0.1 ${D}${libdir}/libara_exec.so.0.1
    ln -sf libara_exec.so.0.1 ${D}${libdir}/libara_exec.so.0
    ln -sf libara_exec.so.0.1 ${D}${libdir}/libara_exec.so
    rm -rf ${B}/libara_exec.so.0.1

    # Install libara_phm.so
    install -m 0644 ${B}/libara_phm.so ${B}/libara_phm.so.0.1
    install -m 0644 ${B}/libara_phm.so.0.1 ${D}${libdir}/libara_phm.so.0.1
    ln -sf libara_phm.so.0.1 ${D}${libdir}/libara_phm.so.0
    ln -sf libara_phm.so.0.1 ${D}${libdir}/libara_phm.so
    rm -rf ${B}/libara_phm.so.0.1

    # Install libarxml.so
    install -m 0644 ${B}/libarxml.so ${B}/libarxml.so.0.1
    install -m 0644 ${B}/libarxml.so.0.1 ${D}${libdir}/libarxml.so.0.1
    ln -sf libarxml.so.0.1 ${D}${libdir}/libarxml.so.0
    ln -sf libarxml.so.0.1 ${D}${libdir}/libarxml.so
    rm -rf ${B}/libarxml.so.0.1
}


FILES:${PN} += "${libdir}/libara_com.so.0.1 \
	${libdir}/libara_com.so.0 \
	${libdir}/libara_com.so \
	${libdir}/libara_diag.so.0.1 \
	${libdir}/libara_diag.so.0 \
	${libdir}/libara_diag.so \
	${libdir}/libara_log.so.0.1 \
	${libdir}/libara_log.so.0 \
	${libdir}/libara_log.so \
	${libdir}/libara_sm.so.0.1 \
	${libdir}/libara_sm.so.0 \
	${libdir}/libara_sm.so \
	${libdir}/libara_core.so.0.1 \
	${libdir}/libara_core.so.0 \
	${libdir}/libara_core.so \
	${libdir}/libara_exec.so.0.1 \
	${libdir}/libara_exec.so.0 \
	${libdir}/libara_exec.so \
	${libdir}/libara_phm.so.0.1 \
	${libdir}/libara_phm.so.0 \
	${libdir}/libara_phm.so \
	${libdir}/libarxml.so.0.1 \
	${libdir}/libarxml.so.0 \
	${libdir}/libarxml.so \
"
