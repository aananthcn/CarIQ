SUMMARY = "Recipe to create update.ext4 image from core-image-minimal tarball"
LICENSE = "MIT"

# Dependencies
DEPENDS = "core-image-minimal e2fsprogs-native"

# Dynamically set the core-image-minimal tarball name based on the MACHINE
CORE_IMAGE_TARBALL = "core-image-minimal-${MACHINE}.tar.bz2"

# Name of the generated ext4 image
IMAGE_NAME = "update.ext4"

# Size of the ext4 image in MiB
EXT4_IMAGE_SIZE = "4032"

inherit deploy

do_compile() {
    # Create temporary directories
    mkdir -p ${WORKDIR}/update
    mkdir -p ${WORKDIR}/mnt

    # Extract the core-image-minimal tarball into the update directory
    tar -xjf ${DEPLOY_DIR_IMAGE}/${CORE_IMAGE_TARBALL} -C ${WORKDIR}/update

    # Create an empty ext4 image
    dd if=/dev/zero of=${WORKDIR}/${IMAGE_NAME} bs=1M count=${EXT4_IMAGE_SIZE}
    mkfs.ext4 -L update ${WORKDIR}/${IMAGE_NAME}

    # Set the specific UUID for the ext4 image (must be same as in *.wks file)
    tune2fs -U a234f914-257f-4ae7-8ebf-614ddf817799 ${WORKDIR}/${IMAGE_NAME}

    # Generate the debugfs script
    echo "cd /" > ${WORKDIR}/debugfs.script

    # Create all directories first
    find ${WORKDIR}/update -type d | sed "s#${WORKDIR}/update##" | while read DIR; do
        echo "mkdir $DIR" >> ${WORKDIR}/debugfs.script
    done

    # Add commands to write all files
    find ${WORKDIR}/update -type f | while read FILE; do
        DEST=$(echo $FILE | sed "s#${WORKDIR}/update##")
        echo "write $FILE $DEST" >> ${WORKDIR}/debugfs.script
    done

    # Debug: Output the debugfs script
    echo "Contents of the debugfs script:"
    cat ${WORKDIR}/debugfs.script

    # Force debugfs to write mode and populate the ext4 image
    debugfs -w ${WORKDIR}/${IMAGE_NAME} -f ${WORKDIR}/debugfs.script 2>&1 | tee ${WORKDIR}/debugfs.log
    # debugfs -w ${WORKDIR}/${IMAGE_NAME} -f ${WORKDIR}/debugfs.script
}

do_install() {
    # Skip staging into rootfs by leaving this empty
    :
}

do_deploy() {
    # Deploy the ext4 image into the deploy directory
    install -d ${DEPLOYDIR}
    install -m 0644 ${WORKDIR}/${IMAGE_NAME} ${DEPLOYDIR}/${IMAGE_NAME}
}

addtask deploy after do_compile before do_build