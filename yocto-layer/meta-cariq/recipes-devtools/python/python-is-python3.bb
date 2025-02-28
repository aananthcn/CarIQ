SUMMARY = "Debian-style symlink for python pointing to python3"
DESCRIPTION = "This package provides a symlink named 'python' that points to 'python3', \
               similar to the 'python-is-python3' package in Debian."
LICENSE = "MIT"
SECTION = "devel"

do_install() {
    install -d ${D}${bindir}
    ln -sf ${bindir}/python3 ${D}${bindir}/python
}

FILES:${PN} += "${bindir}/python"
