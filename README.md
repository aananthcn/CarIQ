# CarIQ



# Getting Started
## Setting up the build-machine (one-time)
 * Use Ubuntu 22.04 machine
 * Install the following packages for Yocto
	* `sudo apt install build-essential python3 python3-pip python3-pexpect python3-git python3-jinja2 python3-subunit git locales chrpath cpio wget diffstat gawk gcc-multilib g++-multilib bison flex texinfo unzip zip bzip2 libncurses5-dev libncursesw5-dev zlib1g-dev bmap-tools mtools dosfstools parted syslinux-common libsdl1.2-dev xterm libgl1-mesa-dev gdb-multiarch strace lzop lz4`
 * Make changes to locale, else bitbake is not happy
	* Generate locale for UTF-8
		* `sudo locale-gen en_US.UTF-8`
		* `sudo update-locale LANG=en_US.UTF-8`
	* Export locale env. Variables (optional)
		* `export LC_ALL=en_US.UTF-8`
		* `export LANG=en_US.UTF-8`
 * `git clone https://github.com/aananthcn/CarIQ.git`
 * `cd CarIQ`
 * `python tools/setup_cariq.py yocto/layers.json`


 ## Build images
 ### Central Compute Node
 * `cd CarIQ/build-ccn`
 * `source ../yocto/poky/oe-init-build-env .`
 * First build the minimal image for OTA (/update partition)
   * `bitbake core-image-minimal`
 * Now build an ext4 image with contents from core-image-minimal. Here 'sudo' is needed because the script uses admin commands.
   * `sudo ../tools/ota/prepare-update-fs.sh ccn`
 * Finally build the target image (the target image's .wks file would take the update.ext4 image)
   * `bitbake cariq-ccn-nirvana`
 * If the build is successful, you can find the SD Card Image in following path
   * `CarIQ/build-ccn/tmp/deploy/images/khadas-vim3/cariq-ccn-nirvana-khadas-vim3.wic.bz2`

### Edge Node X (where, X = 1, 2)
 * `cd CarIQ/build-enX`
 * `source ../yocto/poky/oe-init-build-env .`
 * First build the minimal image for OTA (/update partition)
   * `bitbake core-image-minimal`
 * Now build an ext4 image with contents from core-image-minimal. Here 'sudo' is needed because the script uses admin commands.
   * `sudo ../tools/ota/prepare-update-fs.sh enX`
 * Finally build the target image (the target image's .wks file would take the update.ext4 image)
   * `bitbake cariq-enX-nirvana`
 * If the build(s) are successful, you can find the SD Card Image in following path:
   * `CarIQ/build-en1/tmp/deploy/images/raspberrypi5/cariq-en1-nirvana-raspberrypi5.rootfs.wic.bz2`
   * `CarIQ/build-en2/tmp/deploy/images/raspberrypi5/cariq-en2-nirvana-raspberrypi5.rootfs.wic.bz2`


## Flashing images
### Compute Node
 * Insert SD card into the PC
 * Open balenaEtcher program
 * Select "cariq-amlogic-image-khadas-vim3.wic" as the source
 * Choose the inserted SD card drive as the target.
 * Click flash.

## Flashing images via OTA
### Compute Node
 * Create OTA Package for starting a campaign. Here the release version is `0.0.3`, which must be same as `CARIQ_SW_VERSION` in local.conf
   * `cd CarIQ/build-ccn/`
   * `../tools/ota/ota-creator.py 0.0.3 ccn`
 * Upload the update package to the OTA server (assuming hosted via Apache server on cariqserver)
   * `sudo rm -r /var/www/html/files/2025-01-03/ota-pkg-ccn/`
   * `sudo mv /home/emb-aanahcn/labs/CarIQ/build-ccn/tmp/deploy/images/khadas-vim3/ota-pkg-ccn /var/www/html/files/2025-01-03/`
 * Download from the target. Ensure that the target is able to ping the OTA server IP address.
   * Run `ota-checker.py`
   * If download doesn't start, try modifying the "sw_version" parameter of `/etc/ota/ota-config.json` file.
 * Once the download is complete, run the following command:
   * `ota-updater.sh`
   * This should update the software images and reboot the system.


## Running images
### Compute Node
 * Erase the eMMC
 * Insert the SD Card and Power On.
