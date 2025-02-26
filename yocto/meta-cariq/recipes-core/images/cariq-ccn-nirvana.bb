DESCRIPTION = "CarIQ SD Card Image for Amlogic Meson SoCs"

COMPATIBLE_MACHINE = "^khadas-vim3$"

require cariq-base.inc

IMAGE_INSTALL += "\
    linux-firmware \
"

# Development tools or utils (Amlogic-specific additions)
IMAGE_INSTALL += " npu-sdk-dev npu-bins \
"

WKS_FILES = "sdimage-cariq-ccn.wks"
