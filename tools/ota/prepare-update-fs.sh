#!/bin/bash

set -e

# Constants
IMAGE_FILE="update.ext4"
IMAGE_SIZE_MB=4032
ROOTFS_ARCHIVE="core-image-minimal-khadas-vim3.tar.bz2"

# Check and validate argument
if [[ $# -ne 1 ]]; then
    echo "Usage: $0 <ccn|en1|en2|CCN|EN1|EN2>"
    exit 1
fi

ARG=$(echo "$1" | tr '[:upper:]' '[:lower:]')
if [[ "$ARG" != "ccn" && "$ARG" != "en1" && "$ARG" != "en2" ]]; then
    echo "Invalid argument. Must be one of: ccn, en1, en2 (case-insensitive)"
    exit 1
fi

# Determine script directory
SCRIPT_DIR=$(dirname $(readlink -f "$0"))

# Set variables based on argument
case "$ARG" in
    "ccn")
        MACHINE="khadas-vim3"
        DEPLOY_DIR="${SCRIPT_DIR}/../../build-ccn/tmp/deploy/images/${MACHINE}"
        ROOTFS_ARCHIVE_PATH="${DEPLOY_DIR}/core-image-minimal-${MACHINE}.tar.bz2"
        ;;
    "en1")
        MACHINE="raspberrypi5"
        DEPLOY_DIR="${SCRIPT_DIR}/../../build-en1/tmp/deploy/images/${MACHINE}"
        ROOTFS_ARCHIVE_PATH="${DEPLOY_DIR}/core-image-minimal-${MACHINE}.rootfs.tar.bz2"
        ;;
    "en2")
        MACHINE="raspberrypi5"
        DEPLOY_DIR="${SCRIPT_DIR}/../../build-en2/tmp/deploy/images/${MACHINE}"
        ROOTFS_ARCHIVE_PATH="${DEPLOY_DIR}/core-image-minimal-${MACHINE}.rootfs.tar.bz2"
        ;;
esac

# Debugging output for verification
echo "Using settings:"
echo "  SCRIPT_DIR: $SCRIPT_DIR"
echo "  MACHINE: $MACHINE"
echo "  DEPLOY_DIR: $DEPLOY_DIR"
echo "  ROOTFS_ARCHIVE_PATH: $ROOTFS_ARCHIVE_PATH"
echo "  IMAGE_FILE: $IMAGE_FILE"

# Clean up existing loopback devices
LOOP_DEVICE=$(losetup -j "$IMAGE_FILE" | awk -F':' '{print $1}')
if [[ -n "$LOOP_DEVICE" ]]; then
    echo "Detaching existing loop device: $LOOP_DEVICE"
    sudo umount "$LOOP_DEVICE" || true
    sudo losetup -d "$LOOP_DEVICE"
fi

# Create the ext4 image
echo "Creating ext4 image at $IMAGE_FILE..."
dd if=/dev/zero of="$IMAGE_FILE" bs=1M count="$IMAGE_SIZE_MB"
mkfs.ext4 -F -U a234f914-257f-4ae7-8ebf-614ddf817799 "$IMAGE_FILE"

TEMP_MOUNT=$(mktemp -d)

echo "Mounting the image..."
sudo mount "$IMAGE_FILE" "$TEMP_MOUNT"

echo "Extracting rootfs..."
sudo tar -xf "$ROOTFS_ARCHIVE_PATH" -C "$TEMP_MOUNT"

echo "Unmounting the image..."
sudo umount "$TEMP_MOUNT"
rmdir "$TEMP_MOUNT"

# Move the image to DEPLOY_DIR
echo "Moving the image to $DEPLOY_DIR..."
mv "$IMAGE_FILE" "$DEPLOY_DIR"

echo "Image $IMAGE_FILE created and moved to $DEPLOY_DIR!"
echo ""
echo "update.ext4 is created successfully!"
