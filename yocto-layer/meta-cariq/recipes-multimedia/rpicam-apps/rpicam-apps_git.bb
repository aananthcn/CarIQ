SUMMARY = "Raspberry Pi Camera Applications (rpicam-apps)"
HOMEPAGE = "https://github.com/raspberrypi/rpicam-apps"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-2-Clause;md5=cb641bc04cda31daea161b1bc15da69f"

SRC_URI = "git://github.com/raspberrypi/rpicam-apps.git;branch=main;protocol=https"
SRCREV = "025ca84648c9b9d74711477bf94b05bec349f53d"
PV = "1.6.0"

S = "${WORKDIR}/git"

# Dependencies
DEPENDS = "libcamera boost libexif jpeg tiff libpng libdrm"

inherit meson pkgconfig

EXTRA_OEMESON = "-Denable_drm=enabled"

do_install:append() {
    # Install binaries
    install -d ${D}${bindir}
    install -m 0755 ${B}/apps/rpicam-hello ${D}${bindir}/
    install -m 0755 ${B}/apps/rpicam-still ${D}${bindir}/
    install -m 0755 ${B}/apps/rpicam-vid ${D}${bindir}/
    install -m 0755 ${B}/apps/rpicam-raw ${D}${bindir}/
    install -m 0755 ${B}/apps/rpicam-jpeg ${D}${bindir}/

    # Install shared library
    install -d ${D}${libdir}
    install -m 0755 ${B}/rpicam_app.so.1.6.0 ${D}${libdir}/
    ln -sf rpicam_app.so.1.6.0 ${D}${libdir}/rpicam_app.so

    # Install post-processing plugins
    install -d ${D}${libdir}/rpicam-apps-postproc
    install -m 0755 ${B}/post_processing_stages/core-postproc.so ${D}${libdir}/rpicam-apps-postproc/
}

FILES:${PN} += " \
    ${bindir}/rpicam-hello \
    ${bindir}/rpicam-still \
    ${bindir}/rpicam-vid \
    ${bindir}/rpicam-raw \
    ${bindir}/rpicam-jpeg \
    ${libdir}/rpicam_app.so \
    ${libdir}/rpicam_app.so.1.6.0 \
    ${libdir}/rpicam-apps-postproc/core-postproc.so \
    ${datadir}/rpi-camera-assets \
    ${datadir}/rpi-camera-assets/*.json \
"

INSANE_SKIP:${PN} += "dev-so"

RDEPENDS:${PN} += "python3-core libdrm"