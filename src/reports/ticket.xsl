<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
    <output>
      <xsl:apply-templates
        select="/collection/entity[item[name = 'type']/value = 'workitem']" />
    </output>
	</xsl:template>
  
<!-- Template  -->
  <xsl:template match="/collection/entity[item[name = 'type']/value = 'workitem']">
    <xsl:variable name="currentUniqueID" select="./item[name = '$uniqueid']/value" />
    <node>
      <xsl:value-of select="item[name='txtsubject']/value" />
      currentUniqueID: <xsl:value-of select="$currentUniqueID" />
      Subject:<xsl:value-of select="txtsubject" />
    </node>
  </xsl:template>
  
  
</xsl:stylesheet>