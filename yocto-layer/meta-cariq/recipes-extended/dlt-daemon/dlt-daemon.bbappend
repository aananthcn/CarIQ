FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

do_install:prepend() {
    echo "DEBUG: Pre-install D resolves to ${D}" > ${WORKDIR}/dlt-install-debug.log
    echo "DEBUG: Checking if ${B}/automotive-dlt.pc exists: $(ls ${B}/automotive-dlt.pc || echo 'Not found')" >> ${WORKDIR}/dlt-install-debug.log
}

do_install:append() {
    echo "DEBUG: Starting do_install append" >> ${WORKDIR}/dlt-install-debug.log
    install -d ${D}/usr/lib/pkgconfig
    install -m 0644 ${B}/automotive-dlt.pc ${D}/usr/lib/pkgconfig/ || echo "ERROR: Failed to install to ${D}/usr/lib/pkgconfig" >&2
    install -d ${D}/usr/include/dlt
    install -m 0644 ${S}/include/dlt/*.h ${D}/usr/include/dlt/ || echo "ERROR: Failed to stage dlt headers from ${S}/include/dlt/" >&2
    echo "DEBUG: Installed to ${D}/usr/lib/pkgconfig and ${D}/usr/include/dlt" >> ${WORKDIR}/dlt-install-debug.log
    echo "DEBUG: Post-install D resolves to ${D}" >> ${WORKDIR}/dlt-install-debug.log
}

do_install:append:class-native() {
    echo "DEBUG: Starting do_install append:class-native" >> ${WORKDIR}/dlt-install-debug.log
    install -d ${D}/usr/lib/pkgconfig
    if [ -f ${B}/automotive-dlt.pc ]; then
        install -m 0644 ${B}/automotive-dlt.pc ${D}/usr/lib/pkgconfig/ || echo "ERROR: Failed to stage to ${D}/usr/lib/pkgconfig" >&2
        sed -i -e 's|includedir=${exec_prefix}/include|includedir=${prefix}/include|' \
               -e 's|-I/usr/include/dlt|-I${includedir}/dlt|' \
               ${D}/usr/lib/pkgconfig/automotive-dlt.pc
        echo "DEBUG: Staged ${B}/automotive-dlt.pc to ${D}/usr/lib/pkgconfig for native" >> ${WORKDIR}/dlt-install-debug.log
    else
        echo "ERROR: ${B}/automotive-dlt.pc not found" >&2
    fi
}

SYSROOT_DIRS:append:class-native = " /usr/lib/pkgconfig /usr/include/dlt"