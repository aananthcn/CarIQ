SUMMARY = "Copy ROS1 Noetic content from cariq-ccn-ros1-image build output"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = ""

do_install() {
    SRC_DIR="${TOPDIR}/../build-ccn-ros1/tmp/work/khadas_vim3-poky-linux/ccn-ros1-build/1.0/rootfs/opt/ros/"
    
    if [ ! -d "${SRC_DIR}" ]; then
        bberror "ROS1 Noetic content not found in ${SRC_DIR}. Please do ccn-ros1-build first."
        exit 1
    fi
    
    if [ -z "$(ls -A ${SRC_DIR})" ]; then
        bberror "ROS1 Noetic content in ${SRC_DIR} is empty. Please ensure ccn-ros1-build contains the expected files."
        exit 1
    fi

    install -d ${D}/opt/ros
    cp -r ${SRC_DIR}* ${D}/opt/ros/
}


# Adding these lines to fix multilib '' bins / libs issue
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
NO_GENERIC_PACKAGE = "1"


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
