
# Append required kernel configuration options
do_configure:append() {
    echo "CONFIG_FIT=y" >> ${B}/.config
    echo "CONFIG_FIT_SIGNATURE=y" >> ${B}/.config
    echo "CONFIG_FIT_IMAGE_POST_PROCESS=y" >> ${B}/.config

    # Ensure configuration is updated
    oe_runmake olddefconfig
}

# Ensure kernel-fitimage class is used
KERNEL_CLASSES += "kernel-fitimage"
