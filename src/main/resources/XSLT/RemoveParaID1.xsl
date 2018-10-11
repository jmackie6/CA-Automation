<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lds="http://www.lds.org/schema/lds-meta/v1">

    <xsl:output indent="no" method="html" />



    <xsl:template match="node() | @*" >
        <xsl:copy>
            <xsl:apply-templates select="node() | @*" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="//p[@id='p1']" />



</xsl:stylesheet>