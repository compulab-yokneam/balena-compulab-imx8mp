SRCREV = "107ce16ffe5b175eb06f35a8920496dd715db238"

# gcc-14 build fix patches
SRC_URI:append = " \
    file://0500-fix-compulab-Address-compilation-issues-with-gcc-14..patch \ 
    file://0501-fix-generic-Address-compilation-issues-with-gcc-14.2.patch \ 
    file://0502-fix-compulab-imx8mm-Address-compilation-issues-with-.patch \ 
    file://0503-fix-compulab-imx8mp-Address-compilation-issues-with-.patch \ 
"

# BalenaOS integration patches
SRC_URI:append = " \
    file://0504-board-compulab-fdt-Issue-fdt_set_env_addr-if-CONFIG_.patch \ 
    file://0505-Disable-BINMAN.patch \ 
    file://0506-resin-specific-env-integration-non-kconfig.patch \ 
    file://0507-compulab-imx8m-plus-Add-BalenOS-environmet.patch \ 
    file://balena.cfg \ 
"
