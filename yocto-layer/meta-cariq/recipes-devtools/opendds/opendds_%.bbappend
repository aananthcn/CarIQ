DEPENDS += "gperf-native"

SRC_URI += "file://add-ace-gperf.patch"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

do_install:append:${MACHINE}() {
    CONFIG_FILE="${D}${datadir}/cmake/OpenDDS/OpenDDSConfig.cmake"

    echo "Checking for: $CONFIG_FILE"
    if [ -f "$CONFIG_FILE" ]; then
        if ! grep -q "_OPENDDS_ACE_ACE_GPERF" "$CONFIG_FILE"; then
            echo "set(_OPENDDS_ACE_ACE_GPERF TRUE)" >> "$CONFIG_FILE"
        fi
    else
        echo "Error: $CONFIG_FILE not found"
        find "${D}" -name OpenDDSConfig.cmake || echo "No OpenDDSConfig.cmake in ${D}"
        exit 1
    fi
    # Ensure the modified file is in sysroot
    install -d ${D}${datadir}/cmake/OpenDDS
    install -m 0644 ${S}/cmake/OpenDDSConfig.cmake ${D}${datadir}/cmake/OpenDDS/
}