SRCREV = "101ed6a09814486c57507783308fea3372e02d94"

# BalenaOS integration patches
SRC_URI:append = " \
    file://0505-Disable-BINMAN.patch \ 
    file://0506-resin-specific-env-integration-non-kconfig.patch \ 
    file://0507-compulab-imx8m-plus-Add-BalenOS-environmet.patch \ 
    file://balena.cfg \ 
"
