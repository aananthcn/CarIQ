SUMMARY = "Async-BSD-Socket-Lib: a non-blocking POSIX nw socket lib over TCP/IP and IPC for Liunx"
HOMEPAGE = "https://github.com/aananthcn/Async-BSD-Socket-Lib"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9faf1714c2d4423fdb0d0cce55269ae3"

SRC_URI = "git://github.com/aananthcn/Async-BSD-Socket-Lib.git;protocol=https;branch=master"
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
    install -m 0644 ${B}/libasync_bsd_socket_lib.so.0.1 ${D}${libdir}
    ln -sf libasync_bsd_socket_lib.so.0.1 ${D}${libdir}/libasync_bsd_socket_lib.so.0
    ln -sf libasync_bsd_socket_lib.so.0.1 ${D}${libdir}/libasync_bsd_socket_lib.so
}


FILES:${PN} += "${libdir}/libasync_bsd_socket_lib.so.0.1 \
	${libdir}/libasync_bsd_socket_lib.so.0 \
	${libdir}/libasync_bsd_socket_lib.so \
"
