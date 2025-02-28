# OTA scripts will be using core-image-minimal on its /update partition.
# But if wic asked to run, then it creates a circular dependency, hence the following line.
do_image_wic[noexec] = "1"


IMAGE_INSTALL += " e2fsprogs-mke2fs e2fsprogs-e2fsck e2fsprogs-tune2fs \
	pv util-linux lsof \
	ota-files \
"
