DESCRIPTION = "CarIQ SD Card Image for Raspberry Pi Boards"

COMPATIBLE_MACHINE = "^rpi$"

require cariq-base.inc

IMAGE_INSTALL += "\
    ${MACHINE_EXTRA_RRECOMMENDS} \
    libcamera rpicam-apps userland rpi-eeprom \
"
IMAGE_INSTALL:remove = "rpi-config linux-firmware"

# WiFi in RPis won't work if generic linux-firmware is used, hence MACHINE_EXTRA_RRECOMMENDS include special bins

DEPENDS += "rpi-bootfiles"
WKS_FILES = "sdimage-cariq-en1.wks"
