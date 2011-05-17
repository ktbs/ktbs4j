package org.liris.ktbs.debug.rdfparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.client.KtbsConstants;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.service.ResourceService;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class DebugRdfParser {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
//		checkReturnedModel();
		KtbsClient client = Ktbs.getRestClient("http://localhost:8001/");
		
		ResourceService service = client.getResourceService();
		IObselType cc = service.getResource("http://localhost:8001/dino/0/alarme", IObselType.class);
		System.out.println(cc);
	}

	private static void checkReturnedModel() throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel();
		FileInputStream is = new FileInputStream("src/org/liris/ktbs/debug/rdfparser/model-0-alarme.rdf");
		model.read(
				is, 
				"http://localhost:8001/dino/0/", 
				KtbsConstants.JENA_RDF_XML);
		
		StmtIterator it = model.listStatements(model.getResource("http://localhost:8001/dino/0/alarme"), RDF.type, (RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			System.out.println(statement);
		}
	}

}
