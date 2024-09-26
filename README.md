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
 * `bitbake cariq-ccn-image`
 * If the build is successful, you can find the SD Card Image in following path:
	* `CarIQ/build-ccn/tmp/deploy/images/khadas-vim3/cariq-ccn-image-khadas-vim3.wic.bz2`

### Edge Node X (where, X = 1, 2)
 * `cd CarIQ/build-enX`
 * `source ../yocto/poky/oe-init-build-env .`
 * `bitbake cariq-en1-image`
 * `bitbake cariq-en2-image`
 * If the build(s) are successful, you can find the SD Card Image in following path:
	* `CarIQ/build-en1/tmp/deploy/images/raspberrypi5/cariq-en1-image-raspberrypi5.rootfs.wic.bz2`
	* `CarIQ/build-en2/tmp/deploy/images/raspberrypi5/cariq-en2-image-raspberrypi5.rootfs.wic.bz2`


## Flashing images
### Compute Node
 * Insert SD card into the PC
 * Open balenaEtcher program
 * Select "cariq-amlogic-image-khadas-vim3.wic" as the source
 * Choose the inserted SD card drive as the target.
 * Click flash.


## Running images
### Compute Node
 * Erase the eMMC
 * Insert the SD Card and Power On.
