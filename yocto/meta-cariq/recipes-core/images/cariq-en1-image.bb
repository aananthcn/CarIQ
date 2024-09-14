DESCRIPTION = "CarIQ SD Card Image for Raspberry Pi Boards"

COMPATIBLE_MACHINE = "^rpi$"


# Define the function to write the cariq_node.txt file
python() {
    import os

    # Define the file path where the cariq_node.txt will be created
    tmpdir = d.getVar('TMPDIR', True)
    file_path = os.path.join(tmpdir, 'cariq_node.txt')

    # Delete the file if it exists
    if os.path.exists(file_path):
        try:
            os.remove(file_path)
        except Exception as e:
            bb.fatal("Failed to delete existing file %s: %s" % (file_path, str(e)))

    # Set the CARIQ_NODE value
    cariq_node = "en1"

    # Write the cariq_node value to the file
    try:
        with open(file_path, 'w') as f:
            f.write(cariq_node)
            bb.note("Successfully wrote %s to %s" % (cariq_node, file_path))
    except Exception as e:
        bb.fatal("Failed to write cariq_node.txt: %s" % str(e))

    # Verify if the file was created and contains the correct content
    if os.path.exists(file_path):
        with open(file_path, 'r') as f:
            content = f.read().strip()
            bb.note("Content of cariq_node.txt: %s" % content)
    else:
        bb.fatal("cariq_node.txt file not found at %s" % file_path)
}


IMAGE_FEATURES += "ssh-server-openssh package-management splash x11 hwcodecs"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    packagegroup-core-ssh-openssh \
    kernel-image \
    kernel-modules \
    kernel-devicetree \
    opkg \
    opkg-collateral \
    packagegroup-xfce-base \
    mesa \
    libgl \
    packagegroup-basic \
    packagegroup-base \
    lshw \
    opencv \
    ffmpeg \
    socat \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${MACHINE_EXTRA_RRECOMMENDS} \
    "

# Adding network manager for Edge Node
IMAGE_INSTALL += "networkmanager network-manager-applet"

# systemd is used as init manager for all nodes
IMAGE_INSTALL:append = " systemd systemd-analyze systemd-serialgetty"

# for camera streaming
IMAGE_INSTALL:append = " gstreamer1.0 gstreamer1.0-plugins-base \
        gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-plugins-ugly \
        gstreamer1.0-libav x264 gstreamer1.0-rtsp-server gstreamer1.0-vaapi gst-devtools \
        gstreamer1.0-python v4l-utils libsdl2"

# Python stufss
IMAGE_INSTALL:append = " python3 python-is-python3"


# CarIQ Apps
IMAGE_INSTALL += " camera-streamer"



# WiFi in RPis won't work if generic linux-firmware is used, hence MACHINE_EXTRA_RRECOMMENDS include special bins
IMAGE_INSTALL:remove = "linux-firmware"

# 4G Rootfs
IMAGE_ROOTFS_SIZE = "4194304"


inherit core-image
