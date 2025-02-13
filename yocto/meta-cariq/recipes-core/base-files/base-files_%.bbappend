do_install:append() {
    echo "/dev/mmcblk0p1                              /boot    vfat    defaults       0  2" >> ${D}${sysconfdir}/fstab
    echo "UUID=a234f914-257f-4ae7-8ebf-614ddf817799   /update  ext4    defaults       0  0" >> ${D}${sysconfdir}/fstab
}

