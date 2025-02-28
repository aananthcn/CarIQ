# File: ../meta-cariq/recipes-npu/npu-bins_1.0.bb

DESCRIPTION = "NPU SDK for A311D"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5d3ea8a85f5d129b7481fd47a4511dba"

SRC_URI = "git://github.com/aananthcn/npu_sdk_a311d.git;branch=main;protocol=https"
SRCREV = "6.0.0"
SRC_URI[sha256sum] = "3765c7dcd665c065515669149f2ca7ce1b43d0c52fd2bdd8780b8729dd92342b"


S = "${WORKDIR}/git"

do_install() {
    # Install the 'lib' folder to /usr/lib if it's not empty
    if [ -d "${S}/lib" ] && [ "$(ls -A ${S}/lib)" ]; then
        install -d ${D}${libdir}
        # cp -r ${S}/lib/* ${D}${libdir}/
        find ${S}/lib/ -type f ! -name 'libOpenCL.so' -exec cp -t ${D}${libdir} {} +
    fi

    # Install the 'bin' folder to /usr/bin if it's not empty
    if [ -d "${S}/bin" ] && [ "$(ls -A ${S}/bin)" ]; then
        install -d ${D}${bindir}
        cp -r ${S}/bin/* ${D}${bindir}/
    fi
}

RDEPENDS:${PN} += "zlib"

# Skip certain QA checks
INSANE_SKIP:${PN} = "dev-so ldflags staticdev"

# Ensure the following libraries are assigned to the main package
FILES:${PN} += "${libdir}/libovxlib.so \
                ${libdir}/libCLC.so \
                ${libdir}/libVSC.so \
                ${libdir}/libVSC_Lite.so \
                ${libdir}/libNNArchPerf.so \
                ${libdir}/libOpenVX.so \
                ${libdir}/libGAL.so \
                ${libdir}/libOpenVXU.so \
                ${libdir}/libArchModelSw.so \
                ${libdir}/libnnsdk.so"


# Ensure the .so files do not end up in the dev package
FILES_SOLIBSDEV = ""

# This disables dev-so QA checks for specific shared libraries without symlink
INSANE_SKIP:${PN} += "dev-so"