EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:${THISDIR}/files/:"
LIC_FILES_CHKSUM = "file://ucm/LICENSE;md5=b64e97d3c7b53b1c5789d61baab7ee2e"


DEPENDS += "boost python3-lxml-native python3-jinja2-native dlt-daemon \
    rapidjson jansson poco pcre2 libpcre2 libcgroup vsomeip gperf-native  \
    opendds opendds-native gperf-native \
"

EXTRA_OECMAKE += "\
    -Wno-error \
    -DGTEST_FOUND=OFF \
    -DCMAKE_INSTALL_PREFIX=${D}/usr \
    -DCMAKE_INSTALL_BINDIR=bin \
    -DCMAKE_MODULE_PATH=${STAGING_DIR_TARGET}/usr/share/cmake-3.28/Modules \
    -DCMAKE_LIBRARY_PATH=${STAGING_LIBDIR} \
    -DBoost_NO_SYSTEM_PATHS=TRUE \
    -DBOOST_ROOT=${STAGING_DIR_TARGET}/usr \
    -DBoost_INCLUDE_DIR=${STAGING_DIR_TARGET}/usr/include \
    -DBoost_LIBRARY_DIR=${STAGING_DIR_TARGET}/usr/lib \
    -DBoost_NO_BOOST_CMAKE=TRUE \
    -DCMAKE_PREFIX_PATH=${STAGING_DIR_TARGET}/usr \
    -DCMAKE_SYSROOT_NATIVE=${STAGING_DIR_NATIVE} \
    -DPYTHON_EXECUTABLE=${STAGING_DIR_NATIVE}/usr/bin/python3-native/python3 \
    -DPYTHONPATH=${STAGING_DIR_NATIVE}/usr/lib/python3.12/site-packages \
    -DARAGEN_PATH=${STAGING_DIR_NATIVE}/usr/bin/ara-gen/aragen \
    -DPCRE2_LIBRARY=${STAGING_LIBDIR}/libpcre2-8.so \
    -DPCRE2_INCLUDE_DIR=${STAGING_INCDIR} \
    -Dvsomeip_DIR=${STAGING_LIBDIR}/cmake/vsomeip3 \
    -DACE_GPERF_EXECUTABLE=${STAGING_DIR_NATIVE}/usr/bin/ace_gperf \
    -DOpenDDS_DIR=${STAGING_DIR_TARGET}/usr/share/cmake/OpenDDS \
    -DACE_ROOT=${STAGING_DIR_TARGET}/usr/share/ace \
    -DOPENDDS_TAO_IDL=${STAGING_DIR_TARGET}/usr/bin/tao_idl \
"

do_configure:prepend() {
    mkdir -p ${STAGING_DIR_NATIVE}/usr/bin/ara-gen
    cp -r ${S}/apd/ara-gen/* ${STAGING_DIR_NATIVE}/usr/bin/ara-gen/
    chmod a+x ${STAGING_DIR_NATIVE}/usr/bin/ara-gen/aragen
    export PYTHONPATH=${STAGING_DIR_NATIVE}/usr/lib/python3.12/site-packages

    # for opendds
    ln -sf ${STAGING_DIR_NATIVE}/usr/bin/gperf ${STAGING_DIR_NATIVE}/usr/bin/ace_gperf
}

do_configure() {
    cmake -S ${S} -B ${B} ${EXTRA_OECMAKE}
}

do_compile:prepend() {
    export PYTHONPATH=${STAGING_DIR_NATIVE}/usr/lib/python3.12/site-packages
}

do_install:append() {
    install -d ${D}${libdir}/cmake/ara-gen
    install -m 0644 ${S}/apd/ara-gen/cmake/ara-gen.cmake ${D}${libdir}/cmake/ara-gen/ara-gen.cmake
}
