# Preserve the recipe's exclusions using Scarthgap's CVE status metadata.
# These CVEs apply to the Aviatrix client, a different product.
CVE_CHECK_IGNORE = ""
CVE_STATUS[CVE-2020-7224] = "not-applicable-platform: Issue applies to the Aviatrix OpenVPN client"
CVE_STATUS[CVE-2020-27569] = "not-applicable-platform: Issue applies to the Aviatrix OpenVPN client"
