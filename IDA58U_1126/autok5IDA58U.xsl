<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:key name="tipusSzerint" match="auto" use="normalize-space(tipus)" />

<xsl:output method="xml" indent="yes" />

<xsl:template match="/autok">
    <tipusok>
        <xsl:for-each select="auto[generate-id() = generate-id(key('tipusSzerint', normalize-space(tipus))[1])]">
            <xsl:sort select="count(key('tipusSzerint', normalize-space(tipus)))" order="descending" data-type="number"/>
            <tipus_elem>
                <tipus><xsl:value-of select="normalize-space(tipus)"/></tipus>
                <db><xsl:value-of select="count(key('tipusSzerint', normalize-space(tipus)))"/></db>
            </tipus_elem>
        </xsl:for-each>
    </tipusok>
</xsl:template>

</xsl:stylesheet>
