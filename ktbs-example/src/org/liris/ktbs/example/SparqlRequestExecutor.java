package org.liris.ktbs.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.liris.ktbs.core.JenaConstants;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * 
 * @author Dino
 *
 */
public class SparqlRequestExecutor {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		// Open the bloggers RDF graph from the filesystem
		InputStream in = new FileInputStream(new File("sparql/obsel.rdf"));

		// Create an empty in-memory model and populate it from the graph
		Model model = ModelFactory.createDefaultModel();
		model.read(in,null,JenaConstants.RDF_XML); // null base URI, since model URIs are absolute
		in.close();

		// Create a new query
		String sparql = 
			"PREFIX : <http://localhost:8001/base2/model1/> " +
			"PREFIX ktbs: <http://liris.cnrs.fr/silex/2009/ktbs#> " +
			"PREFIX rdf: <http://www.w3.org/2000/01/rdf-schema#> " +
			"CONSTRUCT {" +
			"[" +
			"a :ActionInstructeur ;" +
			" ktbs:hasBegin ?beginApparition ;" +
			" ktbs:hasEnd ?endOperateur ;" +
			" ktbs:hasSourceObsel ?alarme1,?alarme2, ?actionoperateur ;" +
			" rdf:label ?label1, ?label2 ;" +
			" :evenement ?evenement1, ?evenement2 ;" +
			" :numeroOrdre ?numOrdre " +
			"]" +
			"} " +
			"WHERE{" +
			
			" ?alarme1 " +
			"  a :Alarme ;" +
			"   ktbs:hasBegin ?beginApparition ;" +
			"   ktbs:hasEnd ?endApparition ;" +
			"   :evenement ?evenement1 ;" +
			"   rdf:label ?label1 ."+
			"  FILTER (regex(?evenement1 , \"En apparition\" ))"+
			
			" ?alarme2 " +
			"  a :Alarme ;" +
			"   ktbs:hasBegin ?beginDisparition ;" +
			"   ktbs:hasEnd ?endDisparition ;" +
			"   :evenement ?evenement2 ;" +
			"   rdf:label ?label2 ."+
			"  FILTER (regex(?label1 , ?label2 ))"+
			"  FILTER (regex(?evenement2 , \"En disparition\" ))"+
			
			" ?actionoperateur " +
			"  a :ActionOperateur ;" +
			"   :numeroOrdre ?numOrdre ;"+
			"   ktbs:hasBegin ?beginOperateur ;" +
			"   ktbs:hasEnd ?endOperateur ." +
			
			"  FILTER (?beginApparition <= ?beginOperateur)" +
			"  FILTER (?endApparition < ?beginDisparition)" +
			"  FILTER (?endDisparition >= ?endOperateur)" +
			"}";

		Query query = QueryFactory.create(sparql);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		Model resultModel = qe.execConstruct();
		OutputStream outputStreamWriter = new FileOutputStream(new File("result/obs1.xml"));
		resultModel.write(outputStreamWriter, JenaConstants.RDF_XML, null);
			
		
		outputStreamWriter.close();
		// Important - free up resources used running the query
		qe.close();


	}

}
