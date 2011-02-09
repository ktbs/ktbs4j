package org.liris.ktbs.visu2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import com.hp.hpl.jena.rdf.model.Model;

public class RunImportFromMySQL {
	
	private static File sourceDir = new File("/home/dcram/Documents/trace-visu/traces/visu2-03022011/source");
	private static File destinationDir = new File("/home/dcram/Documents/trace-visu/traces/visu2-03022011/absolute");
	private static File statDir = new File("/home/dcram/Documents/trace-visu/traces/visu2-03022011/stats");

	
	public static void main(String[] args) throws FileNotFoundException, IOException {
	
		if(!sourceDir.exists())
			sourceDir.mkdirs();
		if(!destinationDir.exists())
			destinationDir.mkdirs();
		if(!statDir.exists())
			statDir.mkdirs();
		
		MySQLObselRetriever retriever = new MySQLObselRetriever();
		Collection<String> fields = retriever.getRdfFields("%Retro%");
		File sourceFile = new File(sourceDir, "source-from-mysql.ttl");
		FileWriter writer = new FileWriter(sourceFile);
		for(String f:fields) {
			writer.write(f);
			writer.write("\n\n");
		}
		writer.flush();
		writer.close();
		
		TraceImporter importer = new TraceImporter();

		// The base that will contain imported resources
		String visu2BaseURI = "http://localhost:8001/visu2/";

		
		// Transform visu2-imported obsels into valid TURTLE
		importer.fixFiles(sourceDir, destinationDir, visu2BaseURI, Visu2Utils.getFixRegexs());
		
		
		// Parse visu2 TURTLE-encoded files into a RDF model
		Model model = importer.parse(destinationDir);
		importer.doStats(model, new PrintStream(new File(statDir, "stats.txt")));
		
		
		
		/*
		 *  Infer a proper trace model from this traces and put it 
		 *  into the KTBS server together with the traces.
		 */
		String modelURI = "http://localhost:8001/visu2/model-visu2/";
		importer.putInKtbs(model, visu2BaseURI, modelURI);
	}

	
}
