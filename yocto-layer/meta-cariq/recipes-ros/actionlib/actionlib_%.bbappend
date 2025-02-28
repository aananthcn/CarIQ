FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

ROS_BRANCH ?= "branch=release/noetic/actionlib"
SRC_URI = "git://github.com/ros-gbp/actionlib-release;${ROS_BRANCH};protocol=https"
SRC_URI += "file://0001-my-Fix-build-with-boost-1.73.0.patch"
