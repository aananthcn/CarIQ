DESCRIPTION = "OTA Updater Script and JSON Configuration"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=cc76c36d66f766d7bcdd525de37e4a65"

inherit allarch

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI  = "file://ota-updater.sh"
SRC_URI += "file://ota-checker.py"

S = "${WORKDIR}"

CARIQ_SW_VERSION ?= "0.0.0"


FILES_${PN} += " \
    /usr/bin/ota-updater.sh \
    /usr/bin/ota-checker.py \
    /etc/ota/ota-dlconf.sh  \
    /etc/ota/ota-config.json"


do_install() {
    # Create /usr/bin directory and install the selected script
    install -d ${D}/usr/bin
    install -m 0755 ${S}/ota-updater.sh ${D}/usr/bin/ota-updater.sh
    install -m 0755 ${S}/ota-checker.py ${D}/usr/bin/ota-checker.py

    # Create /etc/ota directory and generate the JSON file
    install -d ${D}/etc/ota

    # Read MACHINE variable from the build system
    MACHINE_NAME="${MACHINE}"

    # Determine the build type
    if [ "${CARIQ_BUILD_TYPE}" = "nv" ]; then
        BUILD_TYPE="nirvana"
    elif [ "${CARIQ_BUILD_TYPE}" = "ss" ]; then
        BUILD_TYPE="samsara"
    else
        echo "Unknown CARIQ_BUILD_TYPE: ${CARIQ_BUILD_TYPE}, defaulting to 'nirvana'"
        BUILD_TYPE="nirvana"
    fi

    # Create the JSON file line by line
    echo '{' > ${D}/etc/ota/ota-config.json
    echo '    "ota_version": "0.0",' >> ${D}/etc/ota/ota-config.json
    echo '    "sw_version": "'${CARIQ_SW_VERSION}'",' >> ${D}/etc/ota/ota-config.json
    echo '    "server_url": "'${CARIQ_OTASRV_URL}'",' >> ${D}/etc/ota/ota-config.json
    echo '    "cariq_node": "'${CARIQ_NODE}'",' >> ${D}/etc/ota/ota-config.json
    echo '    "build_type": "'${BUILD_TYPE}'",' >> ${D}/etc/ota/ota-config.json
    echo '    "machine_nm": "'${MACHINE_NAME}'"' >> ${D}/etc/ota/ota-config.json
    echo '}' >> ${D}/etc/ota/ota-config.json

    # Generate the download configuration file based on CARIQ_NODE
    echo "# Auto-generated download configuration" > ${D}/etc/ota/ota-dlconf.sh

    case "${CARIQ_NODE}" in
        ccn)
            echo "ROOTFS_UPDATE=\"/update/downloads/cariq-ccn-${BUILD_TYPE}-khadas-vim3.tar.bz2\"" >> ${D}/etc/ota/ota-dlconf.sh
            echo "KERNEL_UPDATE=\"/update/downloads/fitImage\"" >> ${D}/etc/ota/ota-dlconf.sh
            ;;
        en1)
            echo "ROOTFS_UPDATE=\"/update/downloads/cariq-en1-${BUILD_TYPE}-raspberrypi5.rootfs.tar.bz2\"" >> ${D}/etc/ota/ota-dlconf.sh
            echo "KERNEL_UPDATE=\"/update/downloads/kernel_2712.img\"" >> ${D}/etc/ota/ota-dlconf.sh
            ;;
        en2)
            echo "ROOTFS_UPDATE=\"/update/downloads/cariq-en2-${BUILD_TYPE}-raspberrypi5.rootfs.tar.bz2\"" >> ${D}/etc/ota/ota-dlconf.sh
            echo "KERNEL_UPDATE=\"/update/downloads/kernel_2712.img\"" >> ${D}/etc/ota/ota-dlconf.sh
            ;;
        *)
            echo "echo 'Unknown CARIQ_NODE: ${CARIQ_NODE}. Exiting.'" >> ${D}/etc/ota/ota-dlconf.sh
            echo "exit 1" >> ${D}/etc/ota/ota-dlconf.sh
            ;;
    esac
}


RDEPENDS:${PN} = "bash python3"
