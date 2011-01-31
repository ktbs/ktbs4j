package org.liris.ktbs.visu2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.client.KtbsClientApplication;
import org.liris.ktbs.client.KtbsResponse;
import org.liris.ktbs.client.KtbsResponseStatus;
import org.liris.ktbs.client.KtbsRestService;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.rdf.JenaUtils;
import org.liris.ktbs.rdf.resource.RdfResourceRepository;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeMultiset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Utility functions to fix, analyze traces serialized in "almost valid" TURTLE 
 * syntax (hence the "fix" utility, see {@link #fixFiles(File, File, String, Map)}),
 * and to import them to a KTBS server.
 * 
 * @author Damien Cram
 *
 */
public class TraceImporter {

	private static final Log log = LogFactory.getLog(TraceImporter.class);
	
	private FilenameFilter filter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".ttl");
		}
	};

	/**
	 * Infer a trace model from traces contained in a triple set 
	 * and put it into a KTBS server.
	 * 
	 * @param model the triple set containing the input traces, as e Jena model
	 * @param visu2BaseURI the base uri in which the trace model and the traces must be created
	 * @param modelURI the uri of the model to infer
	 */
	public void putInKtbs(final Model model, String visu2BaseURI, String modelURI) {
		KtbsClientApplication app = KtbsClientApplication.getInstance();
		KtbsRestService service = app.getRestService("http://localhost:8001/");
		ResourceRepository repository = new RdfResourceRepository();

		KtbsResponse response = service.createBase("http://localhost:8001/", visu2BaseURI, null);
		checkSuccess(response);

		// Extract the trace model for the rdf statements
		TraceModelExtractor extractor = new TraceModelExtractor(repository, visu2BaseURI);
		TraceModel traceModel = extractor.inferTraceModel(modelURI,model);

		response = service.createTraceModel(visu2BaseURI, traceModel.getURI(), null);
		checkSuccess(response);

		// List all traces in the model
		Multimap<String, Resource> traces = HashMultimap.create();
		StmtIterator it = model.listStatements(
				null, 
				model.getProperty(KtbsConstants.P_HAS_TRACE), 
				(RDFNode)null
		);

		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			String trace = statement.getObject().asResource().getURI();
			trace+=trace.endsWith("/")?"":"/";
			Resource obsel = statement.getSubject().asResource();
			traces.put(trace, obsel);
		}

		Map<String, Long> origins = new TreeMap<String, Long>();
		// Create a stored trace for each trace URI
		for(String traceURI:traces.keySet()) {

			long earliestDate = Long.MAX_VALUE;
			// get the possible origin of the stored trace
			for(Resource obsel:traces.get(traceURI)) {
				String value = obsel.getProperty(model.getProperty(KtbsConstants.P_HAS_BEGIN)).getObject().asLiteral().getString();
				long date = Long.parseLong(value);
				if(date<earliestDate)
					earliestDate = date;
			}
			origins.put(traceURI, earliestDate);
			log.info("Creation of trace " + traceURI + " [origin:"+earliestDate+"]");

			response = service.createStoredTrace(
					visu2BaseURI, 
					traceURI, 
					modelURI, 
					new Date(earliestDate).toString(),
					null);
			checkSuccess(response);
		}

		Collection<String> nativePredicates = new HashSet<String>();
		nativePredicates.add(KtbsConstants.P_HAS_BEGIN);
		nativePredicates.add(KtbsConstants.P_HAS_END);
		nativePredicates.add(KtbsConstants.P_HAS_BEGIN_DT);
		nativePredicates.add(KtbsConstants.P_HAS_END_DT);
		nativePredicates.add(KtbsConstants.P_HAS_TRACE);
		nativePredicates.add(KtbsConstants.P_HAS_SUBJECT);
		nativePredicates.add("http://liris.cnrs.fr/silex/2009/ktbs#hasTraceType");
		nativePredicates.add(RDF.type.getURI());
		
		for(String traceURI:traces.keySet()) {
			log.info("Adding obsels to trace " + traceURI);
			int obselId = 0;

			// sort obsel by end date to ensure the monotonicity
			Set<Resource> obsels = new TreeSet<Resource>(new Comparator<Resource>() {
				@Override
				public int compare(Resource o1, Resource o2) {
					String end1 = o1.getProperty(model.getProperty(KtbsConstants.P_HAS_END)).getObject().asLiteral().getString();
					String end2 = o2.getProperty(model.getProperty(KtbsConstants.P_HAS_END)).getObject().asLiteral().getString();
					return end1.compareTo(end2);
				}
			});

			obsels.addAll(traces.get(traceURI));
			
			for(Resource o:obsels) {

				String obselURI = "obs"+obselId++;

				String begin = o.getProperty(model.getProperty(KtbsConstants.P_HAS_BEGIN)).getObject().asLiteral().getString();
				long beginLong = Long.parseLong(begin);

				String end = o.getProperty(model.getProperty(KtbsConstants.P_HAS_END)).getObject().asLiteral().getString();
				long endLong = Long.parseLong(end);

				String typeURI = o.getProperty(RDF.type).getResource().getURI();
				Statement stmt = o.getProperty(model.getProperty(KtbsConstants.P_HAS_SUBJECT));
				String subject = stmt==null?"unkown":stmt.getObject().asLiteral().getValue().toString();

				Map<String, Object> attributes = new HashMap<String, Object>();
				StmtIterator attIt = model.listStatements(o, null, (RDFNode)null);
				while (attIt.hasNext()) {
					Statement statement = (Statement) attIt.next();
					if(nativePredicates.contains(statement.getPredicate().getURI()))
						continue;

					attributes.put(statement.getPredicate().getURI(), JenaUtils.asJavaObject(statement.getObject()));
				}

				log.info("Adding obsel " + obselURI);
				response = service.createObsel(
						traceURI, 
						obselURI, 
						typeURI, 
						subject, 
						null, 
						null, 
						new BigInteger(Long.toString(beginLong - origins.get(traceURI))), 
						new BigInteger(Long.toString(endLong - origins.get(traceURI))), 
						attributes,
						null);

				checkSuccess(response);
			}
		}

	}

	private void checkSuccess(KtbsResponse response) {
		if(!response.hasSucceeded()) {
			if(response.getKtbsStatus() == KtbsResponseStatus.REQUEST_FAILED)
				System.err.println(response.getServerMessage());
			System.exit(1);
		} 
	}

	
	/**
	 * Calculates the number of obsels, obsel types, attributes, and attributes 
	 * per obsel type, that are contained in some traces and write these
	 * stats to a print stream in the Mediawiki syntax.
	 * 
	 * @param model the input traces, as a triple set represented in a Jena model
	 * @param out the print stream where the result should be printed
	 */
	public void doStats(Model model, PrintStream out) {
		out.println("= Statistiques globales =");
		out.println();
		out.println("* Nb triples: " + model.size() + "<br>");

		StmtIterator it = 
			model.listStatements(null, model.getProperty(KtbsConstants.P_HAS_TRACE), (RDFNode)null);

		Multiset<String> obselTypes = HashMultiset.create();
		Multiset<String> traces = HashMultiset.create();

		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			traces.add(statement.getObject().asResource().getURI());
			Resource rdfType = statement.getSubject().getPropertyResourceValue(RDF.type);
			obselTypes.add(rdfType.getURI());
		}

		out.println("* Nb traces: " + traces.elementSet().size() + "<br>");
		out.println("* Nb obsels: " + traces.size() + "<br>");
		out.println("* Nb obsel types: " + obselTypes.elementSet().size() + "<br>");


		out.println();
		out.println();
		out.println("= Types d'observé =");
		out.println();
		out.println("Les types d'observés des traces de Visu 2, et le nombre d'obsels de chaque type dans les traces.");
		out.println();

		for(String obselType:obselTypes.elementSet()) {
			out.println("* " + getLocal(obselType) + ": \t" + obselTypes.count(obselType) + "<br>");
		}
		Multiset<String> globalAttributes = TreeMultiset.create();

		out.println();
		out.println();
		out.println("= Types d'attribut =");
		out.println();
		out.println("Les types d'attributs des traces de Visu 2, et le nombre d'obsels possédant chaque type d'attributs.");
		out.println();
		for(String obselType:obselTypes.elementSet()) {
			out.println();
			out.println("== " + getLocal(obselType) + " ==");
			out.println();
			it = model.listStatements(null, RDF.type, model.getResource(obselType));

			Multiset<String> attributes = TreeMultiset.create();
			while (it.hasNext()) {
				Statement statement = (Statement) it.next();
				Resource obsel = statement.getSubject();
				StmtIterator obselPropIt = obsel.listProperties();
				while (obselPropIt.hasNext()) {
					Statement propStat = (Statement) obselPropIt.next();
					attributes.add(propStat.getPredicate().getURI());
				}
			}

			globalAttributes.addAll(attributes);
			for(String att:attributes.elementSet())
				out.println("* " + getLocal(att) + "\t("+attributes.count(att)+") <br>");
		}


		out.println();
		out.println();
		out.println("= Attributs par type d'observé =");
		out.println();
		out.println("Attributs définis pour chaque type d'obsel. Entre parenthèses: le nombre d'obsels de ce type possédant l'attribut.");
		out.println();
		for(String att:globalAttributes.elementSet())
			out.println(getLocal(att) + "\t(" + globalAttributes.count(att) + ") <br>");

		out.println();
		out.println();
		out.println("= Exemple de valeurs des attributs par type =");
		out.println();
		out.println("Au plus 5 valeurs différentes par attributs et par type sont données ci-dessous, afin de donner un aperçu des valeurs que peuvent prendre les attributs.");

		for(String obselType: obselTypes.elementSet()) {
			out.println();
			out.println("== " + getLocal(obselType) + " ==");
			out.println();

			Multimap<String, String> attValues = TreeMultimap.create();

			// display five values for each attributes
			it = model.listStatements(null, RDF.type, model.getResource(obselType));

			while (it.hasNext()) {
				Statement statement = (Statement) it.next();


				StmtIterator attIt = model.listStatements(
						statement.getSubject(), 
						null, 
						(RDFNode)null);

				while (attIt.hasNext()) {
					Statement attStmt = (Statement) attIt.next();
					attValues.put(
							attStmt.getPredicate().getURI(), 
							attStmt.getObject().isLiteral()?
									attStmt.getObject().asLiteral().getValue().toString():
										(attStmt.getObject().isAnon()?
												"Resource Anonyme":
													attStmt.getObject().asResource().getURI())
					);
				}

			}

			for(String att:attValues.keySet()) {
				out.println("* " + getLocal(att) + "<br>");
				int nb =1;
				for(String value:attValues.get(att)) {
					if(nb>5)
						break;
					out.println("** " + getLocal(value) + "<br>");
					nb++;
				}
			}
		}
	}

	private String getLocal(String s) {
		return s.replaceAll(RDF.type.getURI(), "rdf:")
		.replaceAll(KtbsConstants.NAMESPACE, "htbs:")
		.replaceAll("http://localhost:8001/visu/", "");

	}


	/**
	 * Parse the traces serialized in turtle files to a Jena model.
	 * 
	 * @param source the place where to find the turtle files containing the traces
	 * @return the Jena model containing the traces in RDF
	 * @throws FileNotFoundException
	 */
	public Model parse(File source) throws FileNotFoundException {
		Model unionModel = ModelFactory.createDefaultModel();

		for(File turtleFile:source.listFiles(filter)) {
			log.info("Parsing " + turtleFile.getName());
			Model model = ModelFactory.createDefaultModel();
			model.read(new FileInputStream(turtleFile), null, "TURTLE");
			unionModel.add(model);
		}

		return unionModel;
	}

	/**
	 * Fix the TURTLE syntax errors that may occur in some input 
	 * TURTLE-serialized traces contained in a source directory, resolve 
	 * relative URIs that are present in those files,  and write 
	 * the fix TURTLE-serialized traces to a destination directory.
	 * 
	 * <p>
	 * Only traces files with the ".ttl" suffix in the source directory are processed.
	 * </p>
	 * 
	 * @param source the directory where the turtle files containing the input traces are
	 * @param destination the directory where to write the fixed turtle files
	 * @param uriBaseForRelativeURIs the base URI against which relative resource URIs in 
	 * the sources files must be resolved (empty string if no resolution is whiched)
	 * @param replacements the corrections to apply to input traces, as "regex=replacement" pairs
	 * @throws IOException when troubles appeared in reading from/writing to source/destination directories
	 * @throws FileNotFoundException
	 */
	public void fixFiles(
			File source, 
			File destination, 
			String uriBaseForRelativeURIs,
			Map<String, String> replacements) throws IOException, FileNotFoundException {
		if(destination.exists()) {
			if(destination.listFiles() != null)
				for(File file:destination.listFiles())
					file.delete();
		} else
			destination.mkdir();


		RelativeURITurtleReader resolver = new RelativeURITurtleReader();

		for(File turtleFile:source.listFiles(filter)) {
			log.info("Fixing " + turtleFile.getName());

			
			// before resolving URIs, remove the annoying prefix declaration
			/* 
			 * (MULTILINE)
			 * FROM:
			 * @prefix : <../visu/> .
			 * 
			 * TO:
			 * @prefix : <> .
			 * 
			 */
			replacements.put("^\\s*@prefix\\s*:\\s*<\\.\\./visu/>\\s*\\.\\s*$", "@prefix : <> .");
			
			String string = resolver.resolve(
					IOUtils.toString(new FileInputStream(turtleFile), "ISO-8859-2"), 
					uriBaseForRelativeURIs);

			for(Entry<String, String> replacement:replacements.entrySet()) {
				Pattern p = Pattern.compile(replacement.getKey(), Pattern.MULTILINE);
				Matcher m = p.matcher(string);
				string = m.replaceAll(replacement.getValue());
			}

			IOUtils.write(
					string, 
					new FileOutputStream(new File(destination,turtleFile.getName())),
			"UTF-8");
		}
	}

}
