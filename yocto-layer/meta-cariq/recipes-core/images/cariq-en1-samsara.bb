DESCRIPTION = "CarIQ SD Card Image for Raspberry Pi Boards"

COMPATIBLE_MACHINE = "^rpi$"

require cariq-base.inc
require cariq-baserpi.inc
require cariq-samsara.inc

# Adaptive AUTOSAR packages
require ${TOPDIR}/../yocto-layer/meta-ara/recipes-core/images/core-image-apd-minimal-radar-fusion.bb
require ${TOPDIR}/../yocto-layer/meta-ara/recipes-core/images/core-image-apd-debug.inc
require ${TOPDIR}/../yocto-layer/meta-ara/recipes-core/images/core-image-apd-debug-radarfusion.inc
# require core-image-apd-picar.inc

IMAGE_INSTALL:append:picar = " \
    ara-ifplugd \
    i2c-tools \
    python3-smbus \
    wpa-supplicant \
"

IMAGE_OVERHEAD_FACTOR = "1.5"
DESCRIPTION = "The Adaptive Platform Demonstrator with SSH and remote debugging support"

IMAGE_INSTALL:append = " \
    libpcre2 \
    apd-radarfusionmachine-cluster-picar-actor-v1.0.0 \
    apd-radarfusionmachine-cluster-picar-controller-v1.0.0 \
    apd-radarfusionmachine-cluster-picar-controller-v2.0.0 \
    apd-radarfusionmachine-cluster-picar-linesensor-v1.0.0 \
    apd-radarfusionmachine-cluster-picar-ultrasonicsensor-v1.0.0 \
    apd-radarfusionmachine-statemanager-test \
    apd-radarfusionmachine-ucm-ota-client \
    apd-radarfusionmachine-ucm-pkgmgr-sample \
    apd-radarfusionmachine-ucm-vehicle-driver-app \
    apd-radarfusionmachine-ucm-vehicle-state-manager-app \
    ara-fc-firewall \
    firewall-staticdev \
    firewall-engine \
    load-kernel-modules \
    oem-app \
    tcpserver \
"

IMAGE_INSTALL:append:pican = " \
    ara-can \
"