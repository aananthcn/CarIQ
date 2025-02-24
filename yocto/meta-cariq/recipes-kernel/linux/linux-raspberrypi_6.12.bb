require recipes-kernel/linux/linux-raspberrypi.inc

# Define the branch and source revision for the 6.12 kernel
KBRANCH = "rpi-6.12.y"
SRCREV = "60e5cbd47bcd98f6f71ea0a9d51f0509d9ecd942"

# Specify the repository and branch to fetch the source code
SRC_URI = "git://github.com/raspberrypi/linux.git;branch=${KBRANCH};protocol=https"

# Set the kernel version
PV = "6.12.15"


KERNEL_FEATURES:remove = "cfg/fs/vfat.scc"
