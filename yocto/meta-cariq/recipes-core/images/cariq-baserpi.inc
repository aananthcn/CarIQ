
# cariq-baserpi.inc
# Common configuration for CarIQ nodes using Raspberry Pi

IMAGE_INSTALL += "\
    ${MACHINE_EXTRA_RRECOMMENDS} \
    libcamera rpicam-apps userland rpi-eeprom \
"

# WiFi in RPis won't work if generic linux-firmware is used, hence MACHINE_EXTRA_RRECOMMENDS include special bins
IMAGE_INSTALL:remove = "rpi-config linux-firmware"


DEPENDS += "rpi-bootfiles"
WKS_FILES = "sdimage-cariq-en1.wks"