package org.liris.ktbs.visu2;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Collection;

import org.liris.ktbs.core.JenaConstants;

import com.ctc.wstx.io.CharsetNames;
import com.hp.hpl.jena.n3.turtle.TurtleParseException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class QueryMySQLObsels {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MySQLObselRetriever retriever = new MySQLObselRetriever();
		Collection<String> types = retriever.getObselTypes();
		for(String type:types) {
			System.out.println("public static final String OT_" + type + " = " + type);
		}

		Model model = ModelFactory.createDefaultModel();
		for(String turtle:retriever.getRdfFields()) {
			String s = new RelativeURITurtleReader().resolve(turtle, "http://localhost:8001/visu2/");
			s = Visu2Utils.fixTurtle(s);
			try {
				model.read(new StringReader(s), "", JenaConstants.TURTLE);
			} catch(TurtleParseException e) {
				if(turtle.contains("Trace null")) {
					// ktbs:hasTrace null; dans le turtle, ignorer cet obsel
					continue;
				}
				System.out.println("********************************************************");
				System.out.println(e.getMessage());
				System.out.println("---");
				System.out.println(turtle);
				System.out.println("---");
				System.out.println(s);
			}
		}

		System.out.println(model.size());
	}

}
