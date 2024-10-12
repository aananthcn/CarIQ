# File: ../meta-cariq/recipes-npu/npu_sdk_a311d_1.0.bb

DESCRIPTION = "NPU SDK for A311D - for development"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5d3ea8a85f5d129b7481fd47a4511dba"

SRC_URI = "git://github.com/aananthcn/npu_sdk_a311d.git;branch=main;protocol=https"
SRC_URI[sha256sum] = "3765c7dcd665c065515669149f2ca7ce1b43d0c52fd2bdd8780b8729dd92342b"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

# Do install task
do_install() {
    # Install the 'include' folder to /usr/include if it's not empty
    if [ -d "${S}/include" ] && [ "$(ls -A ${S}/include)" ]; then
        install -d ${D}${includedir}
        cp -r ${S}/include/* ${D}${includedir}/
    fi
}

# Header files go into the development package
FILES:${PN}-dev = "${includedir}"

BBCLASSEXTEND = "native nativesdk"


