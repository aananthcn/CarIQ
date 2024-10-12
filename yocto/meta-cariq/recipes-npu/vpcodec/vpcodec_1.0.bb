DESCRIPTION = "C2 VP Codec - A Video Processing Codec for C2"
HOMEPAGE = "https://github.com/OtherCrashOverride/c2_vpcodec"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/OtherCrashOverride/c2_vpcodec.git;branch=master;protocol=https"
SRCREV = "master"
S = "${WORKDIR}/git"

# Remove problematic flags
CFLAGS:remove = "-fcanon-prefix-map"
CXXFLAGS:remove = "-fcanon-prefix-map"

# Define packages
PACKAGES = "${PN} ${PN}-dev"

# Main package contains libraries
FILES:${PN} = "${libdir}/libvpcodec.so*"

# Development package contains headers
FILES:${PN}-dev = "${includedir}"

do_compile() {
    # Build libvpcodec.so
    oe_runmake -f Makefile CC="${CC}" CXX="${CXX}"
}

do_install() {
    # Install the shared library
    install -d ${D}${libdir}
    install -m 0755 ${S}/libvpcodec.so ${D}${libdir}/

    # Install the headers if they exist
    if [ -d "${S}/include" ] && [ "$(ls -A ${S}/include)" ]; then
        install -d ${D}${includedir}
        cp -r ${S}/include/* ${D}${includedir}/
    fi
}

# Disable the generation of .debug files
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

# Skip QA checks
INSANE_SKIP:${PN} = "dev-so ldflags staticdev"