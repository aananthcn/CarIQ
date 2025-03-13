EXTERNALSRC := "${THISDIR}/../../../../yocto-shared/ara-api"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../../yocto-shared/ara-api/:"

# DEPENDS += "boost"
# EXTRA_OECMAKE += "\
#     -DBoost_NO_SYSTEM_PATHS=TRUE \
#     -DBOOST_ROOT=${STAGING_DIR_TARGET}/usr \
#     -DBoost_INCLUDE_DIR=${STAGING_DIR_TARGET}/usr/include \
# "
