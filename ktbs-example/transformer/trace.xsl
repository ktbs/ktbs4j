<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink"
  	exclude-result-prefixes="xsl" >
  	<xsl:output method="xml" indent="yes" encoding="UTF-8" 
  	doctype-system="http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd"
  	doctype-public="-//W3C//DTD SVG 1.0//EN" /> 
  	
  	<xsl:template match="/">
  		<svg  width="1200" height="800" xml:lang="fr" xmlns="http://www.w3.org/2000/svg" >			
			<desc>Just try ...</desc>
  				<xsl:apply-templates select="//trace" />
  		</svg>
  	</xsl:template>
  	
	<xsl:template match="trace">
		<xsl:param name="scaleTrace" select="number(position() - 1) * 300" />
		<!-- <div>Trace <xsl:value-of select="substring-before(substring-after(descendant::node()[1]/@rdf:about, 'base2/'), '/')" /><br/>Nombre d'observé : <xsl:value-of select="count(child::*)"></xsl:value-of><br/> -->
		<rect x="10" y="{600 - $scaleTrace}px" rx="0" ry="0" width="1180" height="200" stroke="#303030" fill="#FFFFFF"/>
		<xsl:for-each select="obsels/child::obsel">
			<!-- <xsl:sort select="ktbs:hasBegin" data-type="number"/> -->
			<!-- <xsl:value-of select="name()" />(<xsl:value-of select="ktbs:hasBegin" />, <xsl:value-of select="ktbs:hasEnd" />) -->
			
			<xsl:apply-templates select="." >
				<xsl:with-param name="begin" select="hasBegin" />
					<xsl:with-param name="end" select="hasEnd" />
					<xsl:with-param name="scaleTrace" select="$scaleTrace" />
			</xsl:apply-templates>
		</xsl:for-each>
		
		<xsl:for-each select="methods/method/lines/line">
			<xsl:apply-templates select="." >
					<xsl:with-param name="scaleTrace" select="$scaleTrace" />
					<xsl:with-param name="obselBegin" select="obselBegin" />
					<xsl:with-param name="obselEnd" select="obselEnd" />
			</xsl:apply-templates>
		</xsl:for-each>
	</xsl:template>
	
	
	<xsl:template match="line">
		<xsl:param name="scaleTrace"  />
		<xsl:param name="obselBegin"  />
		<xsl:param name="obselEnd"  />
		
		<xsl:param name="yE" select="720"/>
		<xsl:param name="xE" select="number($obselEnd/begin) * 25 +25"/>
		
		<xsl:param name="xB" select="number($obselBegin/begin)*25 + 18"/>
		<xsl:param name="yB" select="640 - $scaleTrace"/>	
		
		<line x1="{$xB}" y1="{$yB}" x2="{$xE}" y2="{$yE}" style="fill:none;stroke:black;stroke-width:2px;"/>
		
		<text id="ligne1" x="{$xE}px" y="{$yE}px">
	 		(<xsl:value-of select="$xE" />, <xsl:value-of select="$yE" />)
		</text>
	</xsl:template>
	
	
	<xsl:template match="obsel"> 
		<xsl:param name="scaleTrace" />
	 	<xsl:param name="begin" />
	 	<xsl:param name="end" />
	 	
	 	<xsl:choose >
	 		<xsl:when test="./obselType/@id = 'Alarme'">
	 			<image  width="35px" height="35px"  xlink:href="alarme.jpg" x="{$begin*25}px" y="{720 - $scaleTrace}px"/>
	 			<text id="ligne1" x="{$begin*25}px" y="{720 - $scaleTrace}px">
	 				(<xsl:value-of select="$begin" />, <xsl:value-of select="$scaleTrace" />)
				</text>
	 		</xsl:when>
	 		<xsl:when test="./obselType/@id = 'ActionOperateur'">
	 			<image  width="35px" height="35px"  xlink:href="actionOperateur.gif" x="{$begin*25}px" y="{680 - $scaleTrace}px"/>
	 			<text id="ligne1" x="{$begin*25}px" y="{680 - $scaleTrace}px">
	 				(<xsl:value-of select="$begin" />, <xsl:value-of select="$scaleTrace" />)
				</text>
	 		</xsl:when>
	 		<xsl:when test="./obselType/@id = 'ActionInstructeur'">
	 			<image  width="35px" height="35px"  xlink:href="actionInstructeur.gif" x="{$begin*25}px" y="{640 - $scaleTrace}px"/>
	 			<text id="ligne1" x="{$begin*25}px" y="{640 - $scaleTrace}px">
	 				(<xsl:value-of select="$begin" />, <xsl:value-of select="$scaleTrace" />)
				</text>
	 		</xsl:when>
	 	</xsl:choose>	 	
	</xsl:template>

</xsl:stylesheet>