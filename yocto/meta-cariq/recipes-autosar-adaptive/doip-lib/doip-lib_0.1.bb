SUMMARY = "DoIP-Lib: Library for DoIP communications"
HOMEPAGE = "https://github.com/aananthcn/DoIP-Lib"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f13a82791d153c9ef3cd4ed79c576c0"

SRC_URI = "git://github.com/aananthcn/DoIP-Lib.git;protocol=https;branch=master"
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
    install -m 0644 ${B}/libdoip_lib.so.0.1 ${D}${libdir}
    ln -sf libdoip_lib.so.0.1 ${D}${libdir}/libdoip_lib.so.0
    ln -sf libdoip_lib.so.0.1 ${D}${libdir}/libdoip_lib.so

	# Install header files
    install -d ${D}${includedir}/doiplib
    install -m 0644 ${S}/include/doiplib/* ${D}${includedir}/doiplib/
}

FILES:${PN} += "${libdir}/libdoip_lib.so.0.1 \
	${libdir}/libdoip_lib.so.0 \
	${libdir}/libdoip_lib.so \
"

FILES:${PN}-dev += "${includedir}/doiplib"

# Ensure the .so files do not end up in the dev package
FILES_SOLIBSDEV = ""

# This disables dev-so QA checks for specific shared libraries without symlink
INSANE_SKIP:${PN} += "dev-so"
