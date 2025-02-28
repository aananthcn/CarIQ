# meta-cariq/recipes-multimedia/ffmpeg/ffmpeg_%.bbappend
PACKAGECONFIG:append_pn-ffmpeg = " sdl2"

#IMAGE_INSTALL:append = " gnutls"
DEPENDS += " gnutls aom libass dav1d libsdl2 libva \
	libvdpau libxcb xcb-util xcb-proto libx11 \
	libsdl2-ttf libsdl-gfx libsdl2-net libsdl libsdl2-mixer libsdl2-image \
"


# Ensure that ffplay is included in the build
EXTRA_OECONF += " \
     --pkg-config-flags="--static" \
     --extra-libs="-lpthread -lm" \
     --enable-gpl \
     --enable-gnutls \
     --enable-libaom \
     --enable-libass \
     --enable-libfdk-aac \
     --enable-libfreetype \
     --enable-libmp3lame \
     --enable-libopus \
     --enable-libdav1d \
     --enable-libvorbis \
     --enable-libvpx \
     --enable-libx264 \
     --enable-libx265 \
     --enable-postproc \
     --enable-nonfree \
     --enable-ffplay \
"

#
#     --enable-libsvtav1 \
#