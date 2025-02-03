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

    # Install shared libraries
    for lib in ara_com ara_diag ara_log ara_sm ara_core ara_exec ara_phm arxml; do
        install -m 0644 ${B}/lib${lib}.so ${B}/lib${lib}.so.0.1
        install -m 0644 ${B}/lib${lib}.so.0.1 ${D}${libdir}/lib${lib}.so.0.1
        ln -sf lib${lib}.so.0.1 ${D}${libdir}/lib${lib}.so.0
        ln -sf lib${lib}.so.0.1 ${D}${libdir}/lib${lib}.so
        rm -rf ${B}/lib${lib}.so.0.1
    done

    # Install header files under ${includedir}/adaptive-autosar
    install -d ${D}${includedir}/adaptive-autosar
    find ${S}/src -type f -name "*.h" -exec install -m 0644 {} ${D}${includedir}/adaptive-autosar/ \;
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

# Ensure the .so files do not end up in the dev package
FILES_SOLIBSDEV = ""

# This disables dev-so QA checks for specific shared libraries without symlink
INSANE_SKIP:${PN} += "dev-so"
