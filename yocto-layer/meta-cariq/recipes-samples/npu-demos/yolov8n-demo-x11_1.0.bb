SUMMARY = "Yolov8n Demo Application using X11 (GTK) for Khadas NPU"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d3b802d4e8ece349cfea0a843e1ba94a"

SRC_URI = "git://github.com/aananthcn/vim3_npu_applications.git;branch=master;protocol=https"
SRCREV = "master"
S = "${WORKDIR}/git"

inherit pkgconfig

DEPENDS = "virtual/libc virtual/kernel virtual/libx11 opencv npu-sdk npu-bins vpcodec"

# Ensure runtime dependencies are correctly specified
RDEPENDS:${PN} += "glibc npu-bins vpcodec"


# Define paths
export OPENCV4_INCLUDE_DIR = "${WORKDIR}/recipe-sysroot/usr/include/opencv4"

# Define build options as export variables
export BUILD_OPTION_DEBUG = "0"
export BUILD_OPTION_ABI = "0"
export BUILD_OPTION_LINUX_OABI = "0"
export BUILD_OPTION_NO_DMA_COHERENT = "0"
export BUILD_OPTION_USE_VDK = "1"
export BUILD_OPTION_EGL_API_FB ?= "1"
export BUILD_OPTION_EGL_API_DFB ?= "0"
export BUILD_OPTION_EGL_API_DRI ?= "0"
export BUILD_OPTION_X11_DRI3 ?= "0"
export BUILD_OPTION_EGL_API_WL ?= "0"
export BUILD_OPTION_EGL_API_NULLWS ?= "0"
export BUILD_OPTION_gcdSTATIC_LINK = "0"
export BUILD_OPTION_CUSTOM_PIXMAP = "0"
export BUILD_OPTION_USE_3D_VG = "1"
export BUILD_OPTION_USE_OPENCL ?= "0"
export BUILD_OPTION_USE_OPENVX ?= "1"
export BUILD_OPTION_USE_VULKAN ?= "0"
export BUILD_OPTION_USE_FB_DOUBLE_BUFFER = "0"
export BUILD_OPTION_USE_PLATFORM_DRIVER = "1"
export BUILD_OPTION_ENABLE_GPU_CLOCK_BY_DRIVER ?= "0"
export BUILD_YOCTO_DRI_BUILD ?= "0"
export BUILD_OPTION_CONFIG_DOVEXC5_BOARD = "0"
export BUILD_OPTION_FPGA_BUILD = "0"

# Set up the build options to pass to the compiler
EXTRA_OEMAKE = "\
    NO_DMA_COHERENT=${BUILD_OPTION_NO_DMA_COHERENT} \
    USE_VDK=${BUILD_OPTION_USE_VDK} \
    EGL_API_FB=${BUILD_OPTION_EGL_API_FB} \
    EGL_API_DFB=${BUILD_OPTION_EGL_API_DFB} \
    EGL_API_DRI=${BUILD_OPTION_EGL_API_DRI} \
    X11_DRI3=${BUILD_OPTION_X11_DRI3} \
    EGL_API_NULLWS=${BUILD_OPTION_EGL_API_NULLWS} \
    gcdSTATIC_LINK=${BUILD_OPTION_gcdSTATIC_LINK} \
    EGL_API_WL=${BUILD_OPTION_EGL_API_WL} \
    ABI=${BUILD_OPTION_ABI} \
    LINUX_OABI=${BUILD_OPTION_LINUX_OABI} \
    DEBUG=${BUILD_OPTION_DEBUG} \
    CUSTOM_PIXMAP=${BUILD_OPTION_CUSTOM_PIXMAP} \
    USE_3D_VG=${BUILD_OPTION_USE_3D_VG} \
    USE_OPENCL=${BUILD_OPTION_USE_OPENCL} \
    USE_OPENVX=${BUILD_OPTION_USE_OPENVX} \
    USE_VULKAN=${BUILD_OPTION_USE_VULKAN} \
    USE_FB_DOUBLE_BUFFER=${BUILD_OPTION_USE_FB_DOUBLE_BUFFER} \
    USE_PLATFORM_DRIVER=${BUILD_OPTION_USE_PLATFORM_DRIVER} \
    ENABLE_GPU_CLOCK_BY_DRIVER=${BUILD_OPTION_ENABLE_GPU_CLOCK_BY_DRIVER} \
    CONFIG_DOVEXC5_BOARD=${BUILD_OPTION_CONFIG_DOVEXC5_BOARD} \
    FPGA_BUILD=${BUILD_OPTION_FPGA_BUILD} \
    YOCTO_DRI_BUILD=${BUILD_YOCTO_DRI_BUILD} \
    _GLIBCXX_USE_CXX11_ABI=1 \
    OPENCV4_ROOT=${OPENCV4_INCLUDE_DIR} \
"


do_compile() {
    cd ${S}/yolov8n_demo_x11_usb
    oe_runmake -f makefile.linux CC="${CC}" CXX="${CXX}" LDFLAGS="${LDFLAGS}"
}

do_install() {
    install -d ${D}${bindir}

    cd ${S}/yolov8n_demo_x11_usb
    install -m 0755 bin_r_cv4/yolov8n_demo_x11_usb ${D}${bindir}/yolov8n_demo_x11
}

INSANE_SKIP:${PN} += "ldflags"
FILES:${PN} += "${bindir}/yolov8n_demo_x11"
