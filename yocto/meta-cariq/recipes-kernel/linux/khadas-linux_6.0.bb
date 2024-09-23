DESCRIPTION = "Linux for Khadas VIM boards with NPU support"

LICENSE = "GPLv2"

# Repo & Branch details
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git;protocol=https;tag=v6.0;branch=master"
SRC_URI[sha256sum] = "746e0e1fd3b19af5ab28da4227f8246558a7e6a95f6ee21bf94bb7f6ba1a2b13"

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"


inherit kernel
require recipes-kernel/linux/linux-yocto.inc


# Ensure kernel-fitimage class is used
KERNEL_CLASSES += "kernel-fitimage"

# kernel configs
SRC_URI += "file://defconfig"

# Khadas VIM3 patches from: https://github.com/khadas/fenix/tree/master/packages/linux-mainline/patches/6.0
SRC_URI += "file://0001-LOCAL-set-meson-gx-cma-pool-to-896MB.patch"
SRC_URI += "file://0002-LOCAL-set-meson-g12-cma-pool-to-896MB.patch"
SRC_URI += "file://0003-LOCAL-arm64-fix-Kodi-sysinfo-CPU-information.patch"
SRC_URI += "file://0004-LOCAL-arm64-meson-add-Amlogic-Meson-GX-PM-Suspend.patch"
SRC_URI += "file://0005-LOCAL-arm64-dts-meson-add-support-for-GX-PM-and-Virt.patch"
SRC_URI += "file://0006-LOCAL-arm64-dts-meson-add-rtc-vrtc-aliases-to-Khadas.patch"
SRC_URI += "file://0007-LOCAL-arm64-dts-meson-add-rtc-vrtc-aliases-to-Khadas.patch"
SRC_URI += "file://0008-LOCAL-arm64-dts-meson-add-rtc-vrtc-aliases-to-Minix-.patch"
SRC_URI += "file://0009-LOCAL-usb-hub-disable-autosuspend-for-Genesys-Logic-.patch"
SRC_URI += "file://0010-LOCAL-of-partial-revert-of-fdt.c-changes.patch"
SRC_URI += "file://0011-FROMLIST-v1-ASoC-meson-aiu-Fix-HDMI-codec-control-se.patch"
SRC_URI += "file://0012-FROMLIST-v1-arm64-dts-meson-make-dts-use-gpio-fan-ma.patch"
SRC_URI += "file://0013-FROMLIST-v1-mmc-meson-gx-fix-deferred-probing.patch"
SRC_URI += "file://0014-FROMLIST-v3-Bluetooth-btrtl-Add-support-for-RTL8822C.patch"
SRC_URI += "file://0015-FROMLIST-v5-dt-bindings-vendor-prefixes-Add-Titan-Mi.patch"
SRC_URI += "file://0016-WIP-ASoC-hdmi-codec-reorder-channel-allocation-list.patch"
SRC_URI += "file://0017-WIP-mmc-meson-gx-mmc-set-core-clock-phase-to-270-deg.patch"
SRC_URI += "file://0018-WIP-arm64-dts-meson-add-Broadcom-WiFi-to-P212-dtsi.patch"
SRC_URI += "file://0019-WIP-arm64-dts-meson-move-pwm_ef-node-in-P212-dtsi.patch"
SRC_URI += "file://0020-WIP-arm64-dts-meson-remove-WiFi-BT-nodes-from-Khadas.patch"
SRC_URI += "file://0021-WIP-arm64-dts-meson-set-p212-p23x-q20x-SDIO-to-100MH.patch"
SRC_URI += "file://0022-WIP-arm64-dts-meson-add-UHS-SDIO-capabilities-to-p21.patch"
SRC_URI += "file://0023-WIP-arm64-dts-meson-remove-SDIO-node-from-Khadas-VIM.patch"
SRC_URI += "file://0024-WIP-drivers-meson-vdec-remove-redundant-if-statement.patch"
SRC_URI += "file://0025-WIP-drivers-meson-vdec-improve-mmu-and-fbc-handling-.patch"
SRC_URI += "file://0026-WIP-drivers-meson-vdec-add-HEVC-decode-codec.patch"
SRC_URI += "file://0027-WIP-drivers-meson-vdec-add-handling-to-HEVC-decoder-.patch"
SRC_URI += "file://0028-WIP-drivers-meson-vdec-add-HEVC-support-to-GXBB.patch"
SRC_URI += "file://0029-WIP-drivers-meson-vdec-check-if-parser-has-really-pa.patch"
SRC_URI += "file://0030-WIP-drm-meson-meson_vclk-fix-VIC-alternate-timings.patch"
SRC_URI += "file://0031-rockchip_khadas_edge_add_missed_spiflash.patch"
SRC_URI += "file://0032-rockchip_khadas_edge_add_ir_recv.patch"
SRC_URI += "file://0033-arm64-dts-VIM3-VIM3L-fix-memory-size-for-vendor-u-bo.patch"
SRC_URI += "file://0034-arm64-dts-VIM2-fix-broken-ethernet-interface-up-down.patch"
SRC_URI += "file://0035-VIM3-hack-for-PCIe.patch"
SRC_URI += "file://0036-ETH-setup-mac-address-from-command-line.patch"
SRC_URI += "file://0037-VIM3-Change-fan-auto-control-temp-to-50-C.patch"
SRC_URI += "file://0038-Revert-builddeb-Fix-rootless-build-in-setuid-setgid-.patch"
SRC_URI += "file://0039-Revert-builddeb-Enable-rootless-builds.patch"
SRC_URI += "file://0040-packaging-5.x-with-postinstall-scripts.patch"
SRC_URI += "file://0041-builddeb-update-dtb-file-for-SD-images.patch"
SRC_URI += "file://0043-add-gpiomem-driver.patch"
SRC_URI += "file://0044-add-gpiomem-node-with-khadas-board.patch"
SRC_URI += "file://0045-add-device-model-to-proc-cpuinfo.patch"
SRC_URI += "file://0046-arm64-dts-VIM3-3L-add-serial-aliases.patch"
SRC_URI += "file://0047-arm64-dts-VIM1-update-serial-aliases.patch"
SRC_URI += "file://0048-arm64-dts-VIM2-update-serial-aliases.patch"
SRC_URI += "file://0049-arm64-dts-VIM3-3L-add-i2c-aliases.patch"
SRC_URI += "file://0050-VIM1-VIM2-add-i2c-aliases.patch"
SRC_URI += "file://0051-arm64-dts-VIM3-add-npu-node.patch"
SRC_URI += "file://0052-arm64-dts-VIM3L-add-npu-node.patch"
SRC_URI += "file://0053-arm64-dts-meson-sm1-khadas-vim3l-use-one-sound-node-.patch"
SRC_URI += "file://0054-arm64-dts-meson-add-spdif-out-to-khadas-vim.patch"
SRC_URI += "file://0055-arm64-dts-meson-add-spdif-out-to-khadas-vim2.patch"
SRC_URI += "file://0056-arm64-dts-meson-khadas-vim3-remake-simple-sound-for-.patch"
SRC_URI += "file://0057-PCI-add-PCIe-Max-Read-Request-Size.patch"
SRC_URI += "file://0058-PCI-DWC-meson-setup-512-PCIe-Max-Read-Request-Size.patch"
SRC_URI += "file://0059-Khadas-remove-unsused-dtb.patch"
SRC_URI += "file://0060-add-Overlay-ConfigFS-interface.patch"
SRC_URI += "file://0061-dtb-enable-creation-of-__symbols__-node.patch"
SRC_URI += "file://0062-package-fix-out-of-the-tree-modules-compilation.patch"
SRC_URI += "file://0063-add-npu-to-staging.patch"
SRC_URI += "file://0064-add-npu-driver-source.patch"
SRC_URI += "file://0065-npu-driver-adaptation-mainline.patch"
SRC_URI += "file://0066-export-symbol-max_pcie_mrrs.patch"
SRC_URI += "file://0067-fix-amlogic-npu-build-issue.patch"
SRC_URI += "file://0068-FROMGIT-5.18-arm64-dts-meson-sm1-add-spdifin-spdifou.patch"
SRC_URI += "file://0069-add-overlay-compilation-support.patch"
SRC_URI += "file://0070-VIMs-add-device-tree-overlays.patch"
# SRC_URI += "file://0100_npu_driver.patch"


# To suppress warnings
CFLAGS += "-Wno-error"
KERNEL_FEATURES:remove = "cfg/fs/vfat.scc"
KERNEL_DANGLING_FEATURES_WARN_ONLY = "1"
KCONF_AUDIT_LEVEL = "1"


# do_patch() {
#     # go to src directory: tmp/work-shared/khadas-vim3/kernel-source
#     cd ${S}
    
#     # Loop through all patch files in the patches directory
#     for patch in ${WORKDIR}/sources-unpack/*.patch; do
#         echo "Applying patch: $patch"
#         patch -p1 < "$patch"
#     done
# }
do_patch() {
    # Go to src directory: tmp/work-shared/khadas-vim3/kernel-source
    cd ${S}

    # Loop through all patch files in the patches directory
    for patch in ${WORKDIR}/sources-unpack/*.patch; do
        echo "Applying patch: $patch"

        # Check if the patch is already applied
        if patch --dry-run -p1 < "$patch" > /dev/null 2>&1; then
            # If the patch applies cleanly in dry-run, then apply it
            patch -p1 < "$patch"
        else
            # If the patch does not apply cleanly, skip it
            echo "Patch $patch is already applied, skipping..."
        fi
    done
}

