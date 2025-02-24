SUMMARY = "Linux libcamera framework"
SECTION = "libs"

LICENSE = "GPL-2.0-or-later & LGPL-2.1-or-later"

LIC_FILES_CHKSUM = "\
    file://LICENSES/GPL-2.0-or-later.txt;md5=fed54355545ffd980b814dab4a3b312c \
    file://LICENSES/LGPL-2.1-or-later.txt;md5=2a4f4fd2128ea2f65047ee63fbca9f68 \
"

# Use Raspberry Pi's fork with RP1 support
SRC_URI = "git://github.com/raspberrypi/libcamera.git;protocol=https;branch=main"

# Commit corresponding to libcamera 0.4.0
SRCREV = "29156679717bec7cc4784aeba3548807f2c27fca"

PE = "1"

S = "${WORKDIR}/git"

DEPENDS = "python3-pyyaml-native python3-jinja2-native python3-ply-native udev \
           gnutls chrpath-native libevent libyaml python3-pybind11-native libpisp \
           gstreamer1.0 gstreamer1.0-plugins-base \
"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'qt', 'qtbase qtbase-native', '', d)}"

PACKAGECONFIG = "gst"
PACKAGECONFIG[gst] = "-Dgstreamer=enabled,-Dgstreamer=disabled,gstreamer1.0 gstreamer1.0-plugins-base"

# MESON config as per README file in github page
EXTRA_OEMESON = " \
    -Dpipelines=rpi/pisp \
    -Dipas=rpi/pisp \
    -Dv4l2=true \
    -Dgstreamer=enabled \
    -Dtest=false \
    -Dlc-compliance=disabled \
    -Dcam=disabled \
    -Dqcam=disabled \
    -Ddocumentation=disabled \
    -Dpycamera=enabled \
"

RDEPENDS:${PN} = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland qt', 'qtwayland', '', d)}"

inherit meson pkgconfig python3native

# Remove the old `do_configure:prepend()` as `mojom` is no longer required

do_install:append() {
    chrpath -d ${D}${libdir}/libcamera.so
    chrpath -d ${D}${libexecdir}/libcamera/v4l2-compat.so
}

do_package:append() {
    bb.build.exec_func("do_package_recalculate_ipa_signatures", d)
}

do_package_recalculate_ipa_signatures() {
    local modules
    for module in $(find ${PKGD}/usr/lib/libcamera -name "*.so.sign"); do
        module="${module%.sign}"
        if [ -f "${module}" ] ; then
            modules="${modules} ${module}"
        fi
    done

    ${S}/src/ipa/ipa-sign-install.sh ${B}/src/ipa-priv-key.pem "${modules}"
}


PYTHON_VERSION_MAJOR = "3"
PYTHON_VERSION_MINOR = "12"

FILES:${PN} += " \
    ${libexecdir}/libcamera/v4l2-compat.so \
    ${libdir}/gstreamer-1.0/libgstlibcamera.so \
    ${libdir}/python${PYTHON_VERSION_MAJOR}.${PYTHON_VERSION_MINOR}/site-packages/libcamera/ \
"

# libcamera-v4l2 explicitly sets _FILE_OFFSET_BITS=32 to get access to
# both 32 and 64 bit file APIs.
GLIBC_64BIT_TIME_FLAGS = ""

# PACKAGES += "${PN}-py"

# FILES:${PN}-py = " \
#     ${libdir}/python${PYTHON_VERSION_MAJOR}.${PYTHON_VERSION_MINOR}/site-packages/libcamera/ \
# "