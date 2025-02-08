# CarIQ uses camera
VIDEO_CAMERA = "1"

# Let us start with 128 MB for GPU
GPU_MEM = "128"

# Remove the blank borders on HDMI displays
DISABLE_OVERSCAN = "1"

# ENABLE_SPI_BUS = "1"
# ENABLE_I2C = "1"
# ENABLE_UART = "1"


# Enable HDMI Audio
RPI_EXTRA_CONFIG:append = "\
dtparam=audio=on\n\
hdmi_force_edid_audio=1\n\
camera_auto_detect=1\n\
display_auto_detect=1\n\
dtoverlay=vc4-kms-v3d\n\
max_framebuffers=2\n\
disable_fw_kms_setup=1\n\
arm_boost=1\n\
"