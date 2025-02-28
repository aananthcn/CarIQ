SUMMARY = "Copy ROS1 Noetic content from cariq-ccn-ros1-image build output"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = ""

R1ROOT = "${TOPDIR}/../build-ccn-ros1/tmp/work/khadas_vim3-poky-linux/ccn-ros1-bins/1.0/rootfs"
R1LIBD = "${R1ROOT}/usr/lib/"
R1OPTD = "${R1ROOT}/opt/ros/"

do_install() {
    if [ ! -d "${R1OPTD}" ]; then
        bberror "ROS1 Noetic content not found in ${R1OPTD}. Please do ccn-ros1-bins first."
        exit 1
    fi
    
    if [ -z "$(ls -A ${R1OPTD})" ]; then
        bberror "ROS1 Noetic content in ${R1OPTD} is empty. Please ensure ccn-ros1-bins contains the expected files."
        exit 1
    fi

    # Copy files from ccn-ros1-bins.bb's output
    install -d ${D}/opt/ros
    # install -d ${D}/opt/ros/noetic/lib
    cp -r ${R1OPTD}* ${D}/opt/ros/
    # cp -r ${R1LIBD}* ${D}/opt/ros/noetic/lib
}

do_install:append() {
    # Copy files from ccn-ros1-bins.bb's output
    install -d ${D}/opt/ros/noetic/lib
    cp -r ${R1LIBD}* ${D}/opt/ros/noetic/lib
}

# Adding these lines to fix multilib '' bins / libs issue
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
NO_GENERIC_PACKAGE = "1"


# For do_install:append
INSANE_SKIP:${PN} = "ldflags"
INSANE_SKIP:${PN} += "dev-so"


FILES:${PN} += "/opt/ros/noetic"

DEPENDS += "\
    boost \
    poco \
    console-bridge \
    libtinyxml2 \
    lz4 \
    log4cxx \
    bzip2 \
    openssl \
    gpgme \
    python3 \
"

RDEPENDS:${PN} += "\
    boost \
    poco \
    console-bridge \
    libtinyxml2 \
    lz4 \
    log4cxx \
    bzip2 \
    openssl \
    gpgme \
    python3 \
    ros1-compat \
"
