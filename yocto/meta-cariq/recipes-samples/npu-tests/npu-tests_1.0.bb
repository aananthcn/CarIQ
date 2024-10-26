SUMMARY = "Test Application & libraries for Khadas NPU"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=da2c36761cf2bbb6290d8e76c3da2625"

SRC_URI = "git://github.com/aananthcn/aml_npu_app.git;branch=master;protocol=https"
SRCREV = "master"
S = "${WORKDIR}/git"

inherit pkgconfig

DEPENDS = "virtual/kernel virtual/libx11 opencv npu-sdk npu-bins vpcodec"

# Ensure runtime dependencies are correctly specified
RDEPENDS:${PN} += "glibc npu-bins vpcodec"


do_compile() {
    OPENCV_INCLUDE_DIR="${STAGING_INCDIR}/opencv4"
    export CFLAGS="-I${OPENCV_INCLUDE_DIR}"
    export CXXFLAGS="-I${OPENCV_INCLUDE_DIR} -std=c++11 -Wall"

    # build libnn_detect.so
    cd ${S}/detect_library/source_code
    oe_runmake -f makefile.linux CC="${CC}" CXX="${CXX}"

    # build libnn_yolo_v8n.so
    cd ${S}/DDK_6.3.3.4/detect_library/model_code/detect_yolov8n
    oe_runmake -f makefile.linux CC="${CC}" CXX="${CXX}"

    # build libnn_yoloface.so
    cd ${S}/DDK_6.3.3.4/detect_library/model_code/detect_yoloface
    oe_runmake -f makefile.linux CC="${CC}" CXX="${CXX}"

    # # build sample_demo_x11
    # cd ${S}/detect_library/sample_demo_x11
    # oe_runmake -f makefile.linux CC="${CC}" CXX="${CXX}" -I${OPENCV_INCLUDE_DIR}

    # build yolov8n_demo_x11_usb
    cd ${S}/detect_library/yolov8n_demo_x11_usb
    oe_runmake -f makefile.linux CC="${CC}" CXX="${CXX}" -I${OPENCV_INCLUDE_DIR}
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${bindir}

    # build libnn_detect.so
    cd ${S}/detect_library/source_code
    install -m 0755 bin_r/libnn_detect.so ${D}${libdir}/libnn_detect.so.1.0
    ln -sf libnn_detect.so.1.0 ${D}${libdir}/libnn_detect.so

    # build libnn_yolo_v8n.so
    cd ${S}/DDK_6.3.3.4/detect_library/model_code/detect_yolov8n
    install -m 0755 bin_r/libnn_yolo_v8n.so ${D}${libdir}/libnn_yolo_v8n.so.1.0
    ln -sf libnn_yolo_v8n.so.1.0 ${D}${libdir}/libnn_yolo_v8n.so

    # build libnn_yoloface.so
    cd ${S}/DDK_6.3.3.4/detect_library/model_code/detect_yoloface
    install -m 0755 bin_r/libnn_yoloface.so ${D}${libdir}/libnn_yoloface.so.1.0
    ln -sf libnn_yoloface.so.1.0 ${D}${libdir}/libnn_yoloface.so

    # cd ${S}/detect_library/sample_demo_x11
    # install -m 0755 bin_r/detect_demo_x11 ${D}${bindir}/detect_demo_x11

    cd ${S}/detect_library/yolov8n_demo_x11_usb
    install -m 0755 bin_r_cv4/detect_demo_x11_usb ${D}${bindir}/yolov8n_demo_x11_usb
}

INSANE_SKIP:${PN} += "ldflags"