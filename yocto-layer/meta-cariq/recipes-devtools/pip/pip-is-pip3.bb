SUMMARY = "Debian-style symlink for pip pointing to pip3"
DESCRIPTION = "This package provides a symlink named 'pip' that points to 'pip3', \
               similar to the 'python-is-python3' package in Debian."
LICENSE = "MIT"
SECTION = "devel"

do_install() {
    install -d ${D}${bindir}
    ln -sf ${bindir}/pip3 ${D}${bindir}/pip
}

FILES:${PN} += "${bindir}/pip"
