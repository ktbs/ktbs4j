//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.08 at 04:40:26 PM CET 
//


package org.liris.ktbs.example.jaxb.base;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hasBegin" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="hasEnd" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element ref="{}obselType"/>
 *         &lt;element ref="{}label"/>
 *         &lt;element ref="{}params"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author Dino
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "hasBegin",
    "hasEnd",
    "obselType",
    "label",
    "params"
})
@XmlRootElement(name = "obsel")
public class Obsel {

    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected BigInteger hasBegin;
    @XmlElement(required = true)
    protected BigInteger hasEnd;
    @XmlElement(required = true)
    protected ObselType obselType;
    @XmlElement(required = true)
    protected String label;
    @XmlElement(required = true)
    protected Params params;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the hasBegin property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHasBegin() {
        return hasBegin;
    }

    /**
     * Sets the value of the hasBegin property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHasBegin(BigInteger value) {
        this.hasBegin = value;
    }

    /**
     * Gets the value of the hasEnd property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHasEnd() {
        return hasEnd;
    }

    /**
     * Sets the value of the hasEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHasEnd(BigInteger value) {
        this.hasEnd = value;
    }

    /**
     * Gets the value of the obselType property.
     * 
     * @return
     *     possible object is
     *     {@link ObselType }
     *     
     */
    public ObselType getObselType() {
        return obselType;
    }

    /**
     * Sets the value of the obselType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObselType }
     *     
     */
    public void setObselType(ObselType value) {
        this.obselType = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the params property.
     * 
     * @return
     *     possible object is
     *     {@link Params }
     *     
     */
    public Params getParams() {
        return params;
    }

    /**
     * Sets the value of the params property.
     * 
     * @param value
     *     allowed object is
     *     {@link Params }
     *     
     */
    public void setParams(Params value) {
        this.params = value;
    }

}