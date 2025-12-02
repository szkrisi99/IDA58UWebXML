<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes" />

<xsl:template match="/autok">
    <rendszamok>
        <xsl:for-each select="auto">
            <rendszam><xsl:value-of select="@rsz"/></rendszam>
        </xsl:for-each>
    </rendszamok>
</xsl:template>

</xsl:stylesheet>
