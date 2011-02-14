<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink"
	exclude-result-prefixes="xsl">
	<xsl:output method="xml" indent="yes" encoding="UTF-8"
		doctype-system="http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd"
		doctype-public="-//W3C//DTD SVG 1.0//EN" />

	<xsl:template match="/">
		<svg width="1200" height="800" xml:lang="fr"
			xmlns="http://www.w3.org/2000/svg">
			<desc>Just try ...</desc>
			<xsl:apply-templates select="//trace" />
		</svg>
	</xsl:template>

	<xsl:template match="trace">
		<!-- variable scaleTrace qui permet de créer un cadre pour uen trace en 
			décalage les uns aux autres -->
		<xsl:param name="scaleTrace" select="number(position() - 1) * 300" />
		<!-- cadre où sera dessiné la trace -->
		<rect x="10" y="{600 - $scaleTrace}px" rx="10" ry="10" width="1180"
			height="200" stroke="#303030" fill="#FFFFFF" />

		<xsl:for-each select="obsels/child::obsel">
			<xsl:apply-templates select=".">
				<xsl:with-param name="scaleTrace" select="$scaleTrace" />
			</xsl:apply-templates>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="obsel">
		<xsl:param name="scaleTrace" />
		<xsl:param name="begin" select="hasBegin" />

		<xsl:choose>
			<xsl:when test="./obselType/@id = 'Alarme'">
				<image width="35px" height="35px" xlink:href="alarme.gif" x="{$begin*25}px"
					y="{720 - $scaleTrace}px" />
				<text id="ligne1" x="{$begin*25}px" y="{720 - $scaleTrace}px">
					(
					<xsl:value-of select="$begin" />
					,
					<xsl:value-of select="$scaleTrace" />
					)
				</text>
			</xsl:when>
			<xsl:when test="./obselType/@id = 'ActionOperateur'">
				<image width="35px" height="35px" xlink:href="actionOperateur.gif"
					x="{$begin*25}px" y="{680 - $scaleTrace}px" />
				<text id="ligne1" x="{$begin*25}px" y="{680 - $scaleTrace}px">
				(<xsl:value-of select="$begin" />, <xsl:value-of select="$scaleTrace" />)
				</text>
			</xsl:when>
			<xsl:when test="./obselType/@id = 'ActionInstructeur'">
				<image width="35px" height="35px" xlink:href="actionInstructeur.gif" x="{$begin*25}px" y="{640 - $scaleTrace}px" />
				<text id="ligne1" x="{$begin*25}px" y="{640 - $scaleTrace}px">
					(<xsl:value-of select="$begin" />,<xsl:value-of select="$scaleTrace" />)
				</text>
			</xsl:when>
		</xsl:choose>

		<xsl:apply-templates select="dest/line">
			<xsl:with-param name="scaleTrace" select="$scaleTrace" />
		</xsl:apply-templates>

	</xsl:template>

	<xsl:template match="line">
		<xsl:param name="scaleTrace" />
		<xsl:choose>
			<xsl:when test="@type='ActionOperateur'">
				<xsl:call-template name="draw">
					<xsl:with-param name="xE" select="number(begin) * 25 +18" />
					<xsl:with-param name="xB"
						select="number(../../hasBegin)*25 + 18" />
					<xsl:with-param name="yB" select="640 - $scaleTrace +32" />
					<xsl:with-param name="yE" select="720 - 40" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="draw">
					<xsl:with-param name="xE" select="number(begin) * 25 +18" />
					<xsl:with-param name="xB" select="number(../../hasBegin)*25 + 18" />
					<xsl:with-param name="yB" select="640 - $scaleTrace +32" />
					<xsl:with-param name="yE" select="720" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="draw">
		<xsl:param name="xE" />
		<xsl:param name="xB" />
		<xsl:param name="yE" />
		<xsl:param name="yB" />

		<line x1="{$xB}" y1="{$yB}" x2="{$xE}" y2="{$yE}"
			style="fill:none;stroke:black;stroke-width:2px;" />

		<text id="ligne1" x="{$xE}px" y="{$yE}px">
			(<xsl:value-of select="$xE" />, <xsl:value-of select="$yE" />)
		</text>
	</xsl:template>
</xsl:stylesheet>