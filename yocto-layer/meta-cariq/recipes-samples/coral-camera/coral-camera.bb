SUMMARY = "Coral Camera Streaming and Inference Application"
DESCRIPTION = "A C++ application for streaming video and running inference using GStreamer and GLib."
HOMEPAGE = "https://github.com/aananthcn/coral-camera"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=88e504139fc7018fe7558dec2bb63933"

SRC_URI = "git://github.com/aananthcn/coral-camera.git;protocol=https;branch=master"
SRCREV = "master"

S = "${WORKDIR}/git"

DEPENDS = "glib-2.0 gstreamer1.0 gstreamer1.0-plugins-base libusb1 \
           pkgconfig-native tensorflow-lite libedgetpu flatbuffers \
"

inherit autotools

TENSORFLOW_LITE_VER = "2.16.1"

# Add TensorFlow Lite include and library paths
TF_CXXFLAGS = "-I${STAGING_DIR_TARGET}/usr/include/tensorflow"

TF_LDFLAGS = " -ltensorflow-lite"

DEBUG_PREFIX_MAP:remove = "-fcanon-prefix-map"

do_compile() {
    EXTRA_CXXFLAGS="${TF_CXXFLAGS} -I${STAGING_INCDIR}" \
    oe_runmake -C ${S}
}


do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/coral-camera ${D}${bindir}/coral-camera
}

FILES_${PN} = "${bindir}/*"
