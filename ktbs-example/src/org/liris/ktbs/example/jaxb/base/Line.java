//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.08 at 04:40:26 PM CET 
//


package org.liris.ktbs.example.jaxb.base;

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
 *         &lt;element name="obselsBegin" type="{}obselLight"/>
 *         &lt;element name="obselsEnd" type="{}obselLight"/>
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
    "obselsBegin",
    "obselsEnd"
})
@XmlRootElement(name = "line")
public class Line {

    @XmlElement(required = true)
    protected ObselLight obselsBegin;
    @XmlElement(required = true)
    protected ObselLight obselsEnd;

    /**
     * Gets the value of the obselsBegin property.
     * 
     * @return
     *     possible object is
     *     {@link ObselLight }
     *     
     */
    public ObselLight getObselsBegin() {
        return obselsBegin;
    }

    /**
     * Sets the value of the obselsBegin property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObselLight }
     *     
     */
    public void setObselsBegin(ObselLight value) {
        this.obselsBegin = value;
    }

    /**
     * Gets the value of the obselsEnd property.
     * 
     * @return
     *     possible object is
     *     {@link ObselLight }
     *     
     */
    public ObselLight getObselsEnd() {
        return obselsEnd;
    }

    /**
     * Sets the value of the obselsEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObselLight }
     *     
     */
    public void setObselsEnd(ObselLight value) {
        this.obselsEnd = value;
    }

}
