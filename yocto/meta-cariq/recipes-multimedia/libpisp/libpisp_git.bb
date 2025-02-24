SUMMARY = "Raspberry Pi PiSP library"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3417a46e992fdf62e5759fba9baef7a7"


SRC_URI = "git://github.com/raspberrypi/libpisp.git;protocol=https;branch=main"

SRCREV = "v1.0.7"


S = "${WORKDIR}/git"

inherit meson pkgconfig

DEPENDS = "nlohmann-json"

PROVIDES = "libpisp"
