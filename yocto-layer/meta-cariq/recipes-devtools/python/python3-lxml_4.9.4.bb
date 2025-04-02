SUMMARY = "Powerful and Pythonic XML processing library combining libxml2/libxslt with the ElementTree API."
DESCRIPTION = "lxml is a Pythonic, mature binding for the libxml2 and \
libxslt libraries. It provides safe and convenient access to these \
libraries using the ElementTree API. It extends the ElementTree API \
significantly to offer support for XPath, RelaxNG, XML Schema, XSLT, \
C14N and much more."
HOMEPAGE = "https://lxml.de/"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause & GPL-2.0-only & MIT & PSF-2.0"
LIC_FILES_CHKSUM = "file://LICENSES.txt;md5=e4c045ebad958ead4b48008f70838403 \
                    file://doc/licenses/elementtree.txt;md5=eb34d036a6e3d56314ee49a6852ac891 \
                    file://doc/licenses/BSD.txt;md5=700a1fc17f4797d4f2d34970c8ee694b \
                    file://doc/licenses/GPL.txt;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/lxml/isoschematron/resources/rng/iso-schematron.rng;beginline=2;endline=7;md5=fc85684a8dd5fa272c086bceb0d99e10 \
                    file://src/lxml/isoschematron/resources/xsl/iso-schematron-xslt1/iso_schematron_message.xsl;beginline=2;endline=24;md5=cc86b7b2bbc678e13f58ea403eb9929b \
                    file://src/lxml/isoschematron/resources/xsl/RNG2Schtrn.xsl;beginline=2;endline=7;md5=5b03236d293dc3784205542b409d2f53"

DEPENDS += "libxml2 libxslt python3-native"

SRC_URI = "https://files.pythonhosted.org/packages/0c/df/733e3a364f4d47b0eb2e8c4253ba1fbed7cc5c0caddc2c64816f8be2db86/lxml-4.9.4.tar.gz"
SRC_URI[sha256sum] = "b1541e50b78e15fa06a2670157a1962ef06591d4c998b998047fff5e3236880e"

S = "${WORKDIR}/lxml-4.9.4"

inherit pkgconfig setuptools3


RDEPENDS:${PN} += "libxml2 libxslt python3-compression"

CLEANBROKEN = "1"

BUILD_CFLAGS += "-Wno-error=incompatible-pointer-types"

BBCLASSEXTEND = "native nativesdk"