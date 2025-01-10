#!/bin/bash

# Define paths
ROOTFS_UPDATE="/update/downloads/cariq-en2-image-raspberrypi5.rootfs.tar.bz2"
KERNEL_UPDATE="/update/downloads/kernel_2712.img"
NEW_ROOT="/update"
OLD_ROOT="/update/mnt"
UPDATE_FLAG="/update/reboot_required.flag"
LOG_FILE="/update/ota-update.log"


# Ensure the script is run as root
if [ "$EUID" -ne 0 ]; then
    echo "Please run as root." | tee -a "$LOG_FILE"
    exit 1
fi


# Remove any stale update flag
rm -f "$UPDATE_FLAG" | tee -a "$LOG_FILE"


# Check if the RootFS update file exists
if [ -f "$ROOTFS_UPDATE" ]; then
    echo "RootFS update file found: $ROOTFS_UPDATE" | tee -a "$LOG_FILE"

    # Bind-mount critical filesystems into /update
    echo "Mounting /proc, /sys, and /dev into $NEW_ROOT..." | tee -a "$LOG_FILE"
    mount --bind /proc "$NEW_ROOT/proc" || { echo "Failed to mount /proc. Exiting." | tee -a "$LOG_FILE"; exit 1; }
    mount --bind /sys "$NEW_ROOT/sys" || { echo "Failed to mount /sys. Exiting." | tee -a "$LOG_FILE"; exit 1; }
    mount --bind /dev "$NEW_ROOT/dev" || { echo "Failed to mount /dev. Exiting." | tee -a "$LOG_FILE"; exit 1; }

    # Bind-mount the current root to /update/mnt
    echo "Binding old root to $OLD_ROOT..." | tee -a "$LOG_FILE"
    mkdir -p "$OLD_ROOT"
    mount --bind / "$OLD_ROOT" || { echo "Failed to bind old root. Exiting." | tee -a "$LOG_FILE"; exit 1; }

    # Switch to the new root using chroot
    echo "Switching to the new root environment using chroot..." | tee -a "$LOG_FILE"
    chroot "$NEW_ROOT" /bin/sh <<'EOF'
        LOG_FILE="/ota-update.log" # Inside chroot, /update becomes /
        echo "Entered the new root environment." | tee -a "$LOG_FILE"

        # Delete old root filesystem contents
        echo "Deleting old root filesystem..." | tee -a "$LOG_FILE"
        rm -rf /mnt/bin /mnt/etc /mnt/home /mnt/lib /mnt/media /mnt/mnt /mnt/opt \
               /mnt/root /mnt/sbin /mnt/srv /mnt/usr || {
            echo "Failed to delete old files. Exiting." | tee -a "$LOG_FILE"
            exit 1
        }

        # Extract the new RootFS
        echo "Extracting new RootFS..." | tee -a "$LOG_FILE"
        pv "/downloads/cariq-en2-image-raspberrypi5.rootfs.tar.bz2" | tar --exclude='var/*' --exclude='./boot/*' -xjf - -C /mnt || {
            echo "RootFS extraction failed. Exiting." | tee -a "$LOG_FILE"
            exit 1
        }

        # Update /etc/fstab
        echo "Updating /etc/fstab..." | tee -a "$LOG_FILE"
        echo "UUID=a234f914-257f-4ae7-8ebf-614ddf817799       /update ext4    defaults        0       0" >> /mnt/etc/fstab || {
            echo "Failed to update /etc/fstab. Exiting." | tee -a "$LOG_FILE"
            exit 1
        }

        echo "RootFS update complete." | tee -a "$LOG_FILE"
EOF

    # Indicate that a reboot is required
    touch "$UPDATE_FLAG" | tee -a "$LOG_FILE"

    # Remove the RootFS update file
    echo "Deleting RootFS update file: $ROOTFS_UPDATE..." | tee -a "$LOG_FILE"
    rm -f "$ROOTFS_UPDATE" || { echo "Failed to delete $ROOTFS_UPDATE. Exiting." | tee -a "$LOG_FILE"; exit 1; }
else
    echo "RootFS update file not found: $ROOTFS_UPDATE. Skipping RootFS update." | tee -a "$LOG_FILE"
fi


# Check if the Kernel update file exists
if [ -f "$KERNEL_UPDATE" ]; then
    echo "Kernel update file found: $KERNEL_UPDATE" | tee -a "$LOG_FILE"

    # Update the kernel
    echo "Updating kernel..." | tee -a "$LOG_FILE"
    pv "$KERNEL_UPDATE" > /boot/kernel_2712.img || {
        echo "Kernel update failed. Exiting." | tee -a "$LOG_FILE"
        exit 1
    }

    # Indicate that a reboot is required
    touch "$UPDATE_FLAG" | tee -a "$LOG_FILE"

    # Remove the Kernel update file
    echo "Deleting Kernel update file: $KERNEL_UPDATE..." | tee -a "$LOG_FILE"
    rm -f "$KERNEL_UPDATE" || { echo "Failed to delete $KERNEL_UPDATE. Exiting." | tee -a "$LOG_FILE"; exit 1; }
    echo "Kernel update complete." | tee -a "$LOG_FILE"
else
    echo "Kernel update file not found: $KERNEL_UPDATE. Skipping kernel update." | tee -a "$LOG_FILE"
fi


# Reboot if the update flag exists
if [ -f "$UPDATE_FLAG" ]; then
    echo "Updates applied. Preparing for reboot..." | tee -a "$LOG_FILE"
    rm -f "$UPDATE_FLAG" || { echo "Failed to remove $UPDATE_FLAG. Exiting." | tee -a "$LOG_FILE"; exit 1; }

    # Change to a safe directory
    cd / || { echo "Failed to change to root directory. Exiting."; exit 1; }

    # Sync filesystem before reboot
    sync || { echo "Failed to sync filesystem."; exit 1; }

    # Attempt to reboot the system
    echo "Rebooting now..." | tee -a "$LOG_FILE"
    reboot || { 
        echo "Reboot command failed. Attempting alternative reboot." | tee -a "$LOG_FILE"
        echo b > /proc/sysrq-trigger
        exit 1
    }
else
    echo "No updates applied. No reboot needed." | tee -a "$LOG_FILE"
fi
