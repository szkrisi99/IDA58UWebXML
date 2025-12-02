<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes" />

<xsl:template match="/autok">
    <autok_ar_sorrendben>
        <xsl:for-each select="auto">
            <xsl:sort select="ar" data-type="number" order="ascending"/>
            <auto>
                <rsz><xsl:value-of select="@rsz"/></rsz>
                <ar><xsl:value-of select="ar"/></ar>
            </auto>
        </xsl:for-each>
    </autok_ar_sorrendben>
</xsl:template>

</xsl:stylesheet>
