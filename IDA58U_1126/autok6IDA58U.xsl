<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:key name="varosSzerint" match="auto" use="tulaj/varos"/>

<xsl:output method="xml" indent="yes"/>

<xsl:template match="/autok">
    <varosok>
        <xsl:for-each select="auto[generate-id() = generate-id(key('varosSzerint', tulaj/varos)[1])]">
            <varos_adat>
                <varos><xsl:value-of select="tulaj/varos"/></varos>
                <db><xsl:value-of select="count(key('varosSzerint', tulaj/varos))"/></db>
                <osszar>
                    <xsl:value-of select="sum(key('varosSzerint', tulaj/varos)/ar)"/>
                </osszar>
            </varos_adat>
        </xsl:for-each>
    </varosok>
</xsl:template>

</xsl:stylesheet>
