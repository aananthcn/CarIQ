SUMMARY = "MoviePy video editing library for Python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/${PN}-${PV}/LICENCE.txt;md5=5884229b0d00bec1cf3e7a78eaba404d"

PYPI_PACKAGE = "moviepy"
SRC_URI = "https://files.pythonhosted.org/packages/source/m/moviepy/moviepy-${PV}.tar.gz"
SRC_URI[sha256sum] = "2884e35d1788077db3ff89e763c5ba7bfddbd7ae9108c9bc809e7ba58fa433f5"

inherit pypi setuptools3

# Add dependencies required for running moviepy
RDEPENDS_${PN} += " \
    python3-imageio \
    python3-imageio-ffmpeg \
    python3-numpy \
    python3-decorator \
    python3-tqdm \
    python3-pillow \
    python3-requests \
"