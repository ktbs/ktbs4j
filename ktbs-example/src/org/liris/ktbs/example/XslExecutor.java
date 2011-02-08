package org.liris.ktbs.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
/**
 * 
 * @author Dino
 *
 */
public class XslExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String xml = "result/allObs.xml";
		String xml = "result/base.xml";
		String svg = "result/base.svg";
		String xsl = "transformer/trace.xsl";
		
		try{
		      
			//execute( xml,  xsl,  svg, "0");
			execute( xml,  xsl,  svg);
			//xml = "result/sparqlResult.xml";
			//svg = "result/sparqlObs.svg";
			//execute( xml,  xsl,  svg, "200");	
		    }
		    catch (Exception e){
		      e.printStackTrace();
		    }
	}
	
	private static void execute(String xml, String xsl, String svg /*, String scaleTrace*/) throws FileNotFoundException, TransformerException{
		TransformerFactory tFactory = TransformerFactory.newInstance();
	      Transformer transformer = tFactory.newTransformer(new StreamSource(xsl));
	      //transformer.setParameter("scaleTrace", scaleTrace);
	      transformer.transform(new StreamSource(xml), new StreamResult(new FileOutputStream(new File(svg))));
	}

}
