<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/orarend">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Órarend</title>
            </head>
            <body>
                <h2>Órarend</h2>
                <table border="1" cellpadding="5">
                    <tr bgcolor="#dddddd">
                        <th>ID</th>
                        <th>Típus</th>
                        <th>Tárgy</th>
                        <th>Nap</th>
                        <th>tól</th>
                        <th>ig</th>
                        <th>Helyszín</th>
                        <th>Oktató</th>
                        <th>Szak</th>
                    </tr>
                    <xsl:for-each select="ora">
                        <tr>
                            <td><xsl:value-of select="@id"/></td>
                            <td><xsl:value-of select="@tipus"/></td>
                            <td><xsl:value-of select="targy"/></td>
                            <td><xsl:value-of select="idopont/nap"/></td>
                            <td><xsl:value-of select="idopont/tol"/></td>
                            <td><xsl:value-of select="idopont/ig"/></td>
                            <td><xsl:value-of select="helyszin"/></td>
                            <td><xsl:value-of select="oktato"/></td>
                            <td><xsl:value-of select="szak"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
