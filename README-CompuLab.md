# CompuLab IOT-GATE-IMX8PV2 demo

This repository adds the CompuLab IOT-GATE-IMX8PV2 device
(`iot-gate-imx8pv2`) with two DRAM configurations: `d1d8` and `d2d4`.

## Disclaimer

**This repository is for demonstration purposes only. The IOT-GATE-IMX8PV2
device is not yet officially supported by balenaOS.**

## Preparation

Before following this guide, complete the repository initialization, host
prerequisites, and build environment preparation described in the original
[README.md](README.md). **Only after completing those preparation steps should
you continue with the build and deployment instructions in this
README-CompuLab.md.**

When initializing the build directory, run the following from the repository
root instead of the automatic-template example in the original README:

```bash
./balena-yocto-scripts/build/barys --dry-run \
  --templates-path layers/meta-balena-imx8mplus/conf/templates/default
```

The explicit template path avoids a `barys` discovery issue with Scarthgap's
`conf/templates/default` layout. This initializes `build/conf/local.conf` and
`build/conf/bblayers.conf` without starting a build. Review `build/conf/local.conf`,
then continue with the build instructions below.

The original [CompuLab BSP layer README.md](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/scarthgap-balena/README.md)
provides additional background on the CompuLab layer and its Yocto setup.
The BSP layer is a Git submodule hosted in a separate repository, so its
README link points directly to that repository.
For this balenaOS demo, use this repository's preparation instructions above
and the machine-specific commands below.

## Prebuilt images

Ready-to-run images can be downloaded from this
[Google Drive folder](https://drive.google.com/drive/u/0/folders/1REYE01zKlkRpRNGsrhA9LZWa-pV1vbVt).

## Build the balenaOS image

After completing the preparation above, initialize the build environment
from the repository root:

```bash
source layers/poky/oe-init-build-env build
```

This command changes the working directory to `build`. Export `MACHINE`
using one of the following settings to match your device's DRAM configuration:

| DRAM configuration | Machine |
| --- | --- |
| `d1d8` | `iot-gate-imx8pv2-d1d8` |
| `d2d4` | `iot-gate-imx8pv2` |

### D1D8

```bash
export MACHINE=iot-gate-imx8pv2-d1d8
```

### D2D4

```bash
export MACHINE=iot-gate-imx8pv2
```

With `MACHINE` exported, build the image:

```bash
MACHINE=${MACHINE} bitbake balena-image
```

Build artifacts are written to `build/tmp/deploy/images/${MACHINE}/`, relative
to the repository root.

## Deployment

### Method #1: Flash eMMC using UUU

**UUU is the preferred deployment method.**

Connect the device to the development host using the device's **micro-USB
Programming (PROG) port**. The host must have `uuu` installed.

Keep `MACHINE` set to the configuration used for the build. From the `build`
directory, enter the deployment directory:

```bash
cd tmp/deploy/images/${MACHINE}
```

Replace `<timestamp>` below with the timestamp in your generated
`.balenaos-img` filename before running the command:

```bash
sudo uuu -d -v -b emmc_all flash.bin balena-image-${MACHINE}-<timestamp>.balenaos-img
```

Use the bootloader and image matching your device's DRAM configuration.
This command writes the bootloader and balenaOS
image to the device's eMMC, overwriting its existing contents.

### Method #2: Boot from a USB flasher image

**This method has not been tested yet. Use the UUU method above in preference.**

Prepare a bootable USB drive using the `balena-image-flasher` image matching
your device's DRAM configuration. To build the flasher image, run the following
from the configured `build` directory with `MACHINE` exported as above:

```bash
MACHINE=${MACHINE} bitbake balena-image-flasher
```

On the development host, insert a USB drive and identify its device name:

```bash
lsblk -o NAME,SIZE,MODEL,TRAN,MOUNTPOINTS
```

Unmount any mounted partitions on that USB drive. From
`build/tmp/deploy/images/${MACHINE}/`, write the **entire flasher image** to
the **whole USB device**, for example `/dev/sdX`, rather than a partition such
as `/dev/sdX1`:

```bash
sudo dd if=balena-image-flasher-${MACHINE}-<timestamp>.balenaos-img of=/dev/sdX bs=4M status=progress conv=fsync
sync
```

Replace `<timestamp>` with the timestamp in your flasher image filename and
`/dev/sdX` with the USB drive's actual device path. For example, a D1D8 build
may produce `balena-image-flasher-iot-gate-imx8pv2-d1d8-20261015080027.balenaos-img`.
**Writing the image erases all existing contents of the selected USB device.**

After writing completes, safely remove the USB drive from the development
host. With the IOT-GATE-IMX8PV2 powered off, insert the drive into one of its
USB host ports, then power on and boot from the USB drive. The flasher image
is intended to install balenaOS onto the device's internal eMMC, overwriting
its existing contents. Monitor the console for completion before removing
the USB drive and rebooting from eMMC.
