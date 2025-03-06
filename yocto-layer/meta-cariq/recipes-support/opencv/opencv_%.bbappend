LICENSE ??= "Apache-2.0"

PACKAGECONFIG:append = " dnn"

EXTRA_OECMAKE += "-DWITH_FRAMEBUFFER=ON"
EXTRA_OECMAKE += "-DWITH_FRAMEBUFFER_XVFB=ON"
EXTRA_OECMAKE += "-DWITH_OPENVX=ON"

# Modify the line below with: gtk+ for GTK 2.x
DEPENDS += "gtk+3"  
PACKAGECONFIG:append = " gtk"
