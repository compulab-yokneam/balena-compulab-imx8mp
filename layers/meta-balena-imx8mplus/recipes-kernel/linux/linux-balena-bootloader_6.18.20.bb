require ${LAYERDIR_compulab-bsp-imx8mp}/recipes-kernel/linux/linux-compulab_6.18.20.bb

inherit kernel-yocto kernel fsl-kernel-localversion balena-bootloader

BALENA_DEFCONFIG_NAME = "compulab_v8_defconfig"

# Linux 6.18 limits DM_CRYPT to a module when TRUSTED_KEYS is modular.
# Keep trusted keys and their existing CAAM backend built in for early boot.
BALENA_CONFIGS_DEPS[device_mapper] += " \
    CONFIG_TRUSTED_KEYS=y \
    CONFIG_CRYPTO_DEV_FSL_CAAM=y \
    CONFIG_CRYPTO_DEV_FSL_CAAM_JR=y \
"

# Safely optimizes code density
BALENA_CONFIGS:append = " core-optimization"
BALENA_CONFIGS[core-optimization] = " \
    CONFIG_CC_OPTIMIZE_FOR_SIZE=y \
    CONFIG_NUMA=n \
"

# Strips unused SMMU drivers and hypervisor support
BALENA_CONFIGS:append = " iommu-smmu"
BALENA_CONFIGS[iommu-smmu] = " \
    CONFIG_ARM_SMMU=n \
    CONFIG_ARM_SMMU_V3=n \
    CONFIG_IOMMU_IO_PGTABLE=n \
    CONFIG_IOMMU_IO_PGTABLE_LPAE=n \
    CONFIG_XEN=n \
"

# Removes clock routing trees for OTHER i.MX8 chips (keeps CLK_IMX8MP enabled)
BALENA_CONFIGS:append = " alternative-clocks"
BALENA_CONFIGS[alternative-clocks] = " \
    CONFIG_CLK_IMX8MN=n \
    CONFIG_CLK_IMX8MQ=n \
    CONFIG_CLK_IMX8QXP=n \
"

# Disables unused AHCI/SATA (keeps USB 3.0 DWC3 enabled for i.MX8MP)
BALENA_CONFIGS:append = " extra-storage"
BALENA_CONFIGS[extra-storage] = " \
    CONFIG_SATA_AHCI=n \
    CONFIG_AHCI_IMX=n \
"

# Eliminates non-i.MX8MP VPUs (Malone/Windsor); i.MX8MP uses Hantro VPU
BALENA_CONFIGS:append = " multimedia-vpu"
BALENA_CONFIGS[multimedia-vpu] = " \
    CONFIG_MXC_VPU_MALONE=n \
    CONFIG_MXC_VPU_WINDSOR=n \
    CONFIG_MXC_HANTRO_V4L2=n \
    CONFIG_MXC_VIDEO_WAVE511_CTRL=n \
    CONFIG_MXC_VIDEO_WAVE6_CTRL=n \
    CONFIG_MXC_MUR=n \
"

# Eliminates enterprise-grade PCIe network adapters
BALENA_CONFIGS:append = " server-nics"
BALENA_CONFIGS[server-nics] = " \
    CONFIG_AMD_XGBE=n \
    CONFIG_THUNDER_NIC_PF=n \
    CONFIG_HNS3=n \
    CONFIG_NET_VENDOR_MELLANOX=n \
    CONFIG_E1000E=n \
"

SRC_URI:append:iot-gate-imx8plus = " \
    file://0204-add-iot-gate-imx8plus-m7-device-tree.patch \
    file://0105-add-mdpcb-m2tpm-device-tree.patch \
"

BALENA_CONFIGS_DEPS[secureboot] += " \
    CONFIG_MODULE_SIG_FORMAT=y \
    CONFIG_PKCS7_MESSAGE_PARSER=y \
    CONFIG_SYSTEM_DATA_VERIFICATION=y \
    CONFIG_SIGNED_PE_FILE_VERIFICATION=y \
"

BALENA_CONFIGS[secureboot] += " \
    CONFIG_KEXEC_IMAGE_VERIFY_SIG=y \
"

do_install:append() {
    rm -rf ${D}/etc ${D}/lib ${D}/usr
}

do_deploy:append () {
    BOOTENV_FILE="${DEPLOYDIR}/${KERNEL_PACKAGE_NAME}/bootenv"
    grub-editenv "${BOOTENV_FILE}" create
    grub-editenv "${BOOTENV_FILE}" set "resin_root_part=A"
    grub-editenv "${BOOTENV_FILE}" set "bootcount=0"
    grub-editenv "${BOOTENV_FILE}" set "upgrade_available=0"
}

do_deploy[depends] += " grub-native:do_populate_sysroot"

INITRAMFS_IMAGE = "balena-image-bootloader-initramfs"

KERNEL_PACKAGE_NAME = "balena-bootloader"

PROVIDES = "virtual/balena-bootloader"
