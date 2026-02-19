#  balena-compulab-imx8p repository

## Clone/Initialize the repository

There are two ways of initializing this repository:
* Clone this repository with "git clone --recursive".

or

* Run "git clone" and then "git submodule update --init --recursive". This will
bring in all the needed dependencies.

## Build information

balenaOS currently only builds with cgroups v1. If your distribution defaults
to using cgroups v2, please boot with the following kernel command line
argument:
`systemd.unified_cgroup_hierarchy=0`

### Containerized build

* If you have a working docker installation, you can build in a containerized
  environment as follows:
  `./balena-yocto-scripts/build/balena-build.sh -d <device type> -s <shared directory>`

  Where:
    * Device type is one of the supported devices with a valid `<device type name>.coffee` description file.
    * Shared directory is the absolute path to the build folder

### Native build

To build all supported device types natively, please make sure your Linux
distribution is [supported](https://docs.yoctoproject.org/singleindex.html#supported-linux-distributions) by Yocto Project.

Additional host tools need to be installed for native builds to work.

* Run the barys build script:
  `./balena-yocto-scripts/build/barys`

* You can also run barys with the -h switch to inspect the available options

### Custom build using this repository

* Run the barys build script in dry run mode to setup an empty `build` directory
    `./balena-yocto-scripts/build/barys --remove-build --dry-run`

* Edit the `local.conf` in the `build/conf` directory

* Prepare build's shell environment
    `source layers/poky/oe-init-build-env`

* Run bitbake (see message outputted when you sourced above for examples)

### Build flags

* Consult layers/meta-balena/README.md for info on various build flags (setting
up serial console support for example) and build prerequisites. Build flags can
be set by using the build scripts (barys or balena-build) or by manually
modifying `local.conf`.

## Provisioning

Provisioning tools for flashing balenaOS on Compulab IOTDIN-IMX8P devices can be found [here](https://github.com/balena-os/iotdin-imx8p-flashtools.git)
