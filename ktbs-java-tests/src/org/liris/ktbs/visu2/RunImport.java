package org.liris.ktbs.visu2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;

public class RunImport {

	private static File source = new File("/home/dcram/Documents/trace-visu/traces/visu2/");
	private static File destination = new File("/home/dcram/Documents/trace-visu/traces/visu2-absolute/");

	private static Map<String, String> replacements = new HashMap<String, String>();
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		TraceImporter importer = new TraceImporter();

		// The base that will contain imported resources
		String visu2BaseURI = "http://localhost:8001/visu2/";

		
		// Transform visu2-imported obsels into valid TURTLE
		initReplacements();
		importer.fixFiles(source, destination, visu2BaseURI, replacements);
		
		
		// Parse visu2 TURTLE-encoded files into a RDF model
		Model model = importer.parse(destination);
		
		/*
		 *  Infer a proper trace model from this traces and put it 
		 *  into the KTBS server together with the traces.
		 */
		String modelURI = "http://localhost:8001/visu2/model-visu2/";
		importer.putInKtbs(model, visu2BaseURI, modelURI);
	}

	private static void initReplacements() {
		replacements = new HashMap<String, String>();

		/* 
		 * (MULTILINE)
		 * FROM:
		 * . a 
		 * 
		 * TO:
		 * [] a 
		 */
		replacements.put("^\\s*\\.\\s+a\\s+", "[] a ");


		/* 
		 * (MULTILINE)
		 * FROM:
		 * :hasText "Couverture de la bande dessinée "Bienvenue à Boboland"";
		 * 
		 * TO:
		 * :hasText  """Couverture de la bande dessinée "Bienvenue à Boboland"""";
		 * 
		 */
		replacements.put("^\\s*(:\\S+)\\s+\"(.*\".*)\"\\s*;", "$1 \"\"\"\n$2\n\"\"\";");
		
		/* 
		 * FROM "" TO "'"
		 */
		replacements.put("\\x92", "'");
		
		
		/* 
		 * FROM a tab character "\t" (not allowed in literals by the TURTLE parser of the KTBS) TO a whitespace character " ".
		 */
		replacements.put("\t", " ");
	}
}
