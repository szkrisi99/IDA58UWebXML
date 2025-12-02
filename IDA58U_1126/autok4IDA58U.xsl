<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>

<xsl:template match="/autok">
    <miskolci_autok>
        <xsl:for-each select="auto[tulaj/varos='Miskolc']">
            <rsz><xsl:value-of select="@rsz"/></rsz>
        </xsl:for-each>
    </miskolci_autok>
</xsl:template>

</xsl:stylesheet>
