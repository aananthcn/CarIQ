# libedgetpu_1.0.bb
SUMMARY = "Edge TPU runtime library for Google Coral"
DESCRIPTION = "Library to support inference on Coral Edge TPU devices."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=98d5c7e5b7c1f9c91e96c705a3b94f68"

# GitHub repository and branch information
SRC_URI = "git://github.com/google-coral/libedgetpu.git;branch=master;protocol=https"
SRCREV = "master"

# Recipe options and build directory
S = "${WORKDIR}/git"
DEPENDS = "cmake"

# Inherit cmake to use standard CMake build process
inherit cmake pkgconfig

# Define target installation path
do_install() {
    # Install library files and headers
    install -d ${D}${libdir}
    install -m 0755 ${B}/libedgetpu.so* ${D}${libdir}
    
    install -d ${D}${includedir}
    cp -r ${S}/include/edgetpu ${D}${includedir}/
}

# Restrict recipe compatibility to Khadas VIM3/VIM4 and Raspberry Pi 4/5
COMPATIBLE_MACHINE = "(khadas-vim3|khadas-vim4|raspberrypi4|raspberrypi5)"

# Include package settings
FILES_${PN} = "${libdir}/libedgetpu.so*"
FILES_${PN}-dev = "${includedir}/edgetpu"

# Additional configurations
CFLAGS += " -O2"

