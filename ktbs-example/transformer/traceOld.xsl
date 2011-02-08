<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ktbs='http://liris.cnrs.fr/silex/2009/ktbs#'
  	xmlns:_7='http://localhost:8001/base2/model1/'
  	xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'
  	xmlns:rdfs='http://www.w3.org/2000/01/rdf-schema#'
  	xmlns:xlink="http://www.w3.org/1999/xlink"
  	exclude-result-prefixes="xsl ktbs _7 rdf rdfs" >
  	<xsl:output method="xml" indent="yes" encoding="UTF-8" 
  	doctype-system="http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd"
  	doctype-public="-//W3C//DTD SVG 1.0//EN" /> 
  	<xsl:param name="scaleTrace" />
  	
	<xsl:template match="rdf:RDF">
		<!-- <div>Trace <xsl:value-of select="substring-before(substring-after(descendant::node()[1]/@rdf:about, 'base2/'), '/')" /><br/>Nombre d'observé : <xsl:value-of select="count(child::*)"></xsl:value-of><br/> -->
		
			<svg  width="1200" height="800" xml:lang="fr" xmlns="http://www.w3.org/2000/svg" >			
			<rect x="10" y="{600 - $scaleTrace}px" rx="0" ry="0" width="1180" height="200" stroke="#303030" fill="#FFFFFF"/>
			<desc>Just try ...</desc>
			<xsl:variable name="scale" select="0" />
				<xsl:for-each select="child::*">
					<!-- <xsl:sort select="ktbs:hasBegin" data-type="number"/> -->
					<!-- <xsl:value-of select="name()" />(<xsl:value-of select="ktbs:hasBegin" />, <xsl:value-of select="ktbs:hasEnd" />) -->
					
					<xsl:apply-templates select="." >
						<xsl:with-param name="begin" select="ktbs:hasBegin" />
	 					<xsl:with-param name="end" select="ktbs:hasEnd" />
					</xsl:apply-templates>
				</xsl:for-each>
		
			</svg>
	</xsl:template>
	
	<xsl:template match="_7:Alarme">
	 	<xsl:param name="begin" />
	 	<xsl:param name="end" />
	 	<image  width="50px" height="50px"  xlink:href="alarme.jpg" x="{$begin*25}px" y="{720 - $scaleTrace}px"/>
	</xsl:template>
	<xsl:template match="_7:ActionOperateur">
	<xsl:param name="begin" />
	 	<xsl:param name="end" />
	<image  width="50px" height="50px"  xlink:href="actionOperateur.gif" x="{$begin*25}px" y="{680 - $scaleTrace}px"/>
	</xsl:template>
	<xsl:template match="_7:ActionInstructeur">
	<xsl:param name="begin" />
	 	<xsl:param name="end" />
		<image  width="50px" height="50px"  xlink:href="actionInstructeur.gif" x="{$begin*25}px" y="{640 - $scaleTrace}px"/>
		<xsl:for-each select="child::ktbs:hasSourceObsel/*">
		
			<xsl:param name="yE" select="720"/>
			<xsl:param name="xB" select="$begin*25 + 18"/>
			<xsl:param name="yB" select="640 - $scaleTrace"/>	
			
			<xsl:param name="xE" select="number(descendant::ktbs:hasBegin) * 25 +25"/>
			<!--  <xsl:param name="yE" select="720 - number($modul)"/>-->
			
			<xsl:choose>
			<xsl:when test="name()='_7:ActionOperateur'">
				<line x1="{$xB}" x2="{$xE}" y1="{$yB}" y2="{$yE - 40}" style="fill:none;stroke:black;stroke-width:2px;"/>
			</xsl:when>
			<xsl:otherwise>
				<line x1="{$xB}" x2="{$xE}" y1="{$yB}" y2="{$yE}" style="fill:none;stroke:black;stroke-width:2px;"/>
			</xsl:otherwise>
			</xsl:choose>
			
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>