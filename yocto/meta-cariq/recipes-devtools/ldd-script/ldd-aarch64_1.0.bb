SUMMARY = "Custom ldd script for finding shared library dependencies"
DESCRIPTION = "A script to replicate the functionality of ldd using the dynamic linker."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://ldd-aarch64"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"


S = "${WORKDIR}"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/ldd-aarch64 ${D}${bindir}/ldd
}
