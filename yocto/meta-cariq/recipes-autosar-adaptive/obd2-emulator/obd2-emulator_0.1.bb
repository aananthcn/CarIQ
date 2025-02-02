SUMMARY = "OBD-II-Emulator: OBD-II emulator via CAN over Linux that handles diagnostics queries of OBD2 scanners"
HOMEPAGE = "https://github.com/aananthcn/OBD-II-Emulator"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f13a82791d153c9ef3cd4ed79c576c0"

SRC_URI = "git://github.com/aananthcn/OBD-II-Emulator.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

# Version string incorporating the revision
PV = "0.1+git${SRCPV}"

# If the build requires CMake, we need the cmake-native tool
DEPENDS = "cmake-native googletest"

# Inherit the CMake class to handle configuration and build steps
inherit cmake

# Point to the correct source directory (for a git repository, typically "git")
S = "${WORKDIR}/git"

# Pass C++14 standard flag to the compiler
EXTRA_OECMAKE += "-DCMAKE_CXX_STANDARD=14 -DCMAKE_CXX_STANDARD_REQUIRED=ON -Dbuild_tests=OFF -DBUILD_SHARED_LIBS=ON"

do_install() {
    # Create the directory where the library should be installed.
    install -d ${D}${libdir}
    # Install the library. Adjust the path if your build directory is different.
    install -m 0644 ${B}/libobd_ii_emulator.so.0.1 ${D}${libdir}
    ln -sf libobd_ii_emulator.so.0.1 ${D}${libdir}/libobd_ii_emulator.so.0
    ln -sf libobd_ii_emulator.so.0.1 ${D}${libdir}/libobd_ii_emulator.so
}

FILES:${PN} += "${libdir}/libobd_ii_emulator.so.0.1 \
	${libdir}/libobd_ii_emulator.so.0 \
	${libdir}/libobd_ii_emulator.so \
"
