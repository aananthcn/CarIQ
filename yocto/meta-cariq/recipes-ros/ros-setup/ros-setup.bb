SUMMARY = "Global ROS setup configuration"
LICENSE = "CLOSED"
PR = "r0"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "file://ros_setup.sh"

# Specify the installation path for the root user's home directory
do_install() {
    install -d ${D}/etc/profile.d
    install -m 0755 ${WORKDIR}/ros_setup.sh ${D}/etc/profile.d/ros_setup.sh
}

FILES:${PN} += "/etc/profile.d/ros_setup.sh"
