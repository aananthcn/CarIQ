EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

LIC_FILES_CHKSUM = "file://apd/ara-gen/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"

DEPENDS += "apd-cmake-modules-native boost python3-native python3-lxml rapidjson dlt-daemon"

PYTHON_PN = "python3.10"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://add_pythonpath_to_aragen.patch"

do_configure:class-native() {
    cmake -S ${S}/apd/ara-gen -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=${D}/usr \
        -DARAGEN=${B}/bin/aragen \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot-native \
        -DCMAKE_INCLUDE_PATH=${WORKDIR}/recipe-sysroot-native/usr/include \
        ${EXTRA_OECMAKE}
}

do_configure() {
    cmake -S ${S}/apd/ara-gen -B ${B} \
        -GNinja \
        -DCMAKE_INSTALL_PREFIX=${D}/usr \
        -DARAGEN=${B}/bin/aragen \
        -DCMAKE_SYSROOT=${WORKDIR}/recipe-sysroot \
        ${EXTRA_OECMAKE}
}

do_compile:prepend() {
    export PYTHONPATH="${S}/apd/ara-gen:${STAGING_DIR_NATIVE}/usr/lib/${PYTHON_PN}/site-packages:${PYTHONPATH}"
    echo "DEBUG: PYTHONPATH set to ${PYTHONPATH}" > ${WORKDIR}/ara-gen-compile-debug.log
}

do_install:class-native() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/apd/ara-gen/aragen ${D}${bindir}/aragen.real || echo "ERROR: Failed to install aragen.real" >&2
    install -d ${D}${libdir}/${PYTHON_PN}/site-packages
    cp -r ${STAGING_DIR_NATIVE}/usr/lib/${PYTHON_PN}/site-packages/lxml ${D}${libdir}/${PYTHON_PN}/site-packages/ || echo "ERROR: Failed to copy lxml" >&2
    cp -r ${S}/apd/ara-gen/generator ${D}${bindir}/ || echo "ERROR: Failed to copy generator" >&2
    echo '#!/bin/sh' > ${D}${bindir}/aragen
    echo "export PYTHONPATH=${STAGING_DIR_NATIVE}/usr/lib/${PYTHON_PN}/site-packages:${D}${libdir}/${PYTHON_PN}/site-packages:${D}${bindir}:\${PYTHONPATH}" >> ${D}${bindir}/aragen
    echo "exec ${D}${bindir}/aragen.real \"\$@\"" >> ${D}${bindir}/aragen
    chmod 0755 ${D}${bindir}/aragen
}


do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/apd/ara-gen/aragen ${D}${bindir}/aragen.real || echo "ERROR: Failed to install aragen.real" >&2
    install -d ${D}${libdir}/${PYTHON_PN}/site-packages
}


do_force_dlt_pc() {
    echo "DEBUG: Forcing automotive-dlt.pc and headers into sysroot" > ${WORKDIR}/force-dlt-debug.log
    # Copy .pc file
    install -d ${WORKDIR}/recipe-sysroot-native/usr/lib/pkgconfig
    SRC_PATH="${WORKDIR}/../../dlt-daemon-native/1.0-r0/sysroot-destdir/usr/lib/pkgconfig/automotive-dlt.pc"
    if [ -f "$SRC_PATH" ]; then
        install -m 0644 "$SRC_PATH" ${WORKDIR}/recipe-sysroot-native/usr/lib/pkgconfig/ || echo "WARNING: Failed to force copy automotive-dlt.pc from $SRC_PATH" >&2
        echo "DEBUG (COPY SUCCESS): Forced copy of automotive-dlt.pc from $SRC_PATH to recipe-sysroot-native/usr/lib/pkgconfig" >> ${WORKDIR}/force-dlt-debug.log
    else
        echo "DEBUG (COPY FAILURE): Source file $SRC_PATH not found" >&2
        echo "DEBUG: Source file $SRC_PATH not found" >> ${WORKDIR}/force-dlt-debug.log
    fi
    # Copy headers
    install -d ${WORKDIR}/recipe-sysroot-native/usr/include/dlt
    SRC_HEADERS="${WORKDIR}/../../dlt-daemon-native/1.0-r0/sysroot-destdir/usr/include/dlt/"
    if [ -d "$SRC_HEADERS" ] && ls "$SRC_HEADERS"/*.h >/dev/null 2>&1; then
        install -m 0644 "$SRC_HEADERS"/*.h ${WORKDIR}/recipe-sysroot-native/usr/include/dlt/ || echo "WARNING: Failed to copy dlt headers from $SRC_HEADERS" >&2
        echo "DEBUG (HEADERS SUCCESS): Copied dlt headers from $SRC_HEADERS" >> ${WORKDIR}/force-dlt-debug.log
    else
        echo "DEBUG (HEADERS FAILURE): No headers found in $SRC_HEADERS" >&2
        echo "DEBUG: No headers found in $SRC_HEADERS" >> ${WORKDIR}/force-dlt-debug.log
    fi
    echo "DEBUG: Contents of ${WORKDIR}/recipe-sysroot-native/usr/lib/pkgconfig: $(ls ${WORKDIR}/recipe-sysroot-native/usr/lib/pkgconfig/ || echo 'Empty')" >> ${WORKDIR}/force-dlt-debug.log
    echo "DEBUG: Contents of ${WORKDIR}/recipe-sysroot-native/usr/include/dlt: $(ls ${WORKDIR}/recipe-sysroot-native/usr/include/dlt/ || echo 'Empty')" >> ${WORKDIR}/force-dlt-debug.log
}

# as re-overriding STAGING_DIR_NATIVE is not working, this is the final approach
addtask force_dlt_pc after do_prepare_recipe_sysroot before do_configure


INSANE_SKIP:${PN} += "already-stripped"

FILES:${PN} += " \
    ${libdir}/${PYTHON_PN}/site-packages \
    ${bindir} \
"