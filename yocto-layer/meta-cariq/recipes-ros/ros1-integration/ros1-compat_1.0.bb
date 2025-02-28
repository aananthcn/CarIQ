SUMMARY = "Compatibility package for ROS1"
DESCRIPTION = "This package provides compatibility libraries needed by ROS1, such as libconsole_bridge.so.0.4."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


# SRC_URI = "file://libconsole_bridge.so.0.4"
SRC_DIR="${TOPDIR}/../build-ccn-ros1/tmp/work/khadas_vim3-poky-linux/ccn-ros1-bins/1.0/rootfs/usr/lib"


do_install() {
    install -d ${D}/etc/ld.so.conf.d/
    install -d ${D}/opt/ros/noetic/lib

    cp ${SRC_DIR}/libconsole_bridge.so.0.4 ${D}/opt/ros/noetic/lib
}


do_install:append() {
    echo "/opt/ros/noetic/lib" > ${D}/etc/ld.so.conf.d/ros-noetic.conf
}


FILES:${PN} = "/opt/ros/noetic/lib/libconsole_bridge.so.0.4 \
    /etc/ld.so.conf.d/ros-noetic.conf"


INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
NO_GENERIC_PACKAGE = "1"
