DEPENDS:append = " readline"
EXTRA_OECONF = "--with-readline --disable-silent-rules --disable-dependency-tracking \
	--with-libtool-sysroot=${STAGING_DIR_TARGET} --enable-nls"