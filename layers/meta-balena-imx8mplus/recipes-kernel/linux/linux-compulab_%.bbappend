FILESEXTRAPATHS:prepend := "${THISDIR}/linux-compulab:"

inherit kernel-resin kernel-balena

DEPENDS += "rsync-native"

SRC_URI:append = "file://balena.cfg"

KERNEL_PACKAGE_NAME="kernel"

# Use the latest version provided by CompuLab
#
SRCBRANCH = "linux-compulab_v6.6.52"
SRCREV = "7a7b7f076ca65e4e6ac8adf0c97e3ab0d8fe5a1a"
LINUX_VERSION = "6.6.52"
PV = "6.6.52"

SCMVERSION="n"
