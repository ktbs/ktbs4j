package org.liris.ktbs.example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.liris.ktbs.client.KtbsClientApplication;
import org.liris.ktbs.client.KtbsResponse;
import org.liris.ktbs.client.KtbsRestService;
import org.liris.ktbs.core.JenaConstants;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceLoadException;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.rdf.resource.RdfComputedTrace;
import org.liris.ktbs.rdf.resource.RdfObsel;
import org.liris.ktbs.rdf.resource.RdfResourceRepository;
import org.liris.ktbs.rdf.resource.RdfStoredTrace;
import org.liris.ktbs.utils.KtbsUtils;

/**
 * Implementation of populate
 * @author Dino
 *
 */
public class ManageObsel {
	
	private static final String SERVER_URL = "http://localhost:8001/";
	//private static final String BASE_URI = SERVER_URL +"base2/";
	//private static final String TRACE_URI = BASE_URI+"t01/";
	//private static final String MODEL_URI = BASE_URI+"model1/";
	
	private static KtbsClientApplication app = KtbsClientApplication.getInstance();
	private static KtbsRestService service = app.getRestService(SERVER_URL);
	private static ResourceRepository repository = new RdfResourceRepository();
	
	
	public static void main(String[] args) throws ResourceLoadException {
		
		//populateKtbs();
		String baseUriTemp = SERVER_URL + "base2/";
		String baseLabel = "Primary base";
		String baseUri = createBase( baseUriTemp, baseLabel);
		
		if(baseUri != null){
			String traceModelUriTemp = baseUri + "model1/";	
			String modelLabel = "Model of Primary trace";
			String traceModelUri = createTraceModel(baseUri, traceModelUriTemp, modelLabel);
		
			if(traceModelUri != null){
				traceModelUri = changeTraceModelLabel(traceModelUri,modelLabel);
				if(traceModelUri != null){
					List<String> listObselType = new ArrayList<String>();
					listObselType.add("ActionInstructeur");
					listObselType.add("ActionOperateur");
					listObselType.add("Alarme");
					traceModelUri = addObselTypeToModel(traceModelUri,listObselType.toArray(new String[0]) );
					if(traceModelUri != null){
						String methodUriTemp = baseUri + "methodFilter1/";	
						String methodLabel = "Timestamp Filter";
						String start = "0";
						String end = "10";
						String methodUri =createMethodFilter(baseUri, methodUriTemp, methodLabel, start, end );
						if(methodUri != null){
							String storedTraceUriTemp = baseUri+"firstTrace/";
							String traceOrigin = "";
							String traceLabel = "First M-Trace";
							String storedTraceUri = createStoredTrace(baseUri, storedTraceUriTemp, traceModelUri, traceOrigin, traceLabel);
							if(storedTraceUri != null){
								String subject = "Insructeur 1";
								String obselLabel = "Demande de changement de mode";
								String obselBegin = "0";
								String obselEnd = "5";
								String obselTypeUri = traceModelUri+"ActionInstructeur";
								String populationGenerateur = traceModelUri + "populationGenerateur";
								String roleSujetGenerateur = traceModelUri + "roleSujetGenerateur";
								String natureSujetGenerateur = traceModelUri + "natureSujetGenerateur";
								String sousSystemeElementaire = traceModelUri + "sousSystemeElementaire";
								String evenement = traceModelUri + "evenement";
								String numeroOrdre = traceModelUri + "numeroOrdre";
								String materiel = traceModelUri + "materiel";
								Map<String, Object> attributes = new HashMap<String, Object>();
								attributes.put(populationGenerateur, "Individu");
								attributes.put(roleSujetGenerateur, "Instructeur");
								attributes.put(natureSujetGenerateur, "Non évalué");
								attributes.put(sousSystemeElementaire, "");
								attributes.put(numeroOrdre, "");
								attributes.put(materiel, "");
								String obs1 = createObsel(storedTraceUri, obselTypeUri, attributes, subject, obselLabel, obselBegin, obselEnd);
								
								subject = "Op 1";
								obselLabel = "Image de conduite";
								obselBegin = "2";
								obselEnd = "10";
								obselTypeUri = traceModelUri+"ActionOperateur";
								attributes = new HashMap<String, Object>();
								attributes.put(populationGenerateur, "Individu");
								attributes.put(roleSujetGenerateur, "OperateurPrimaire");
								attributes.put(natureSujetGenerateur, "Evalué");
								attributes.put(sousSystemeElementaire, "RPR");
								attributes.put(numeroOrdre, "0053");
								attributes.put(materiel, "YC");
								String obs2 = createObsel(storedTraceUri, obselTypeUri, attributes, subject, obselLabel, obselBegin, obselEnd);
								
								subject = "CP0";
								obselLabel = "BAS TEMPS DE DOUBLEMENT CNI";
								obselBegin = "11";
								obselEnd = "11";
								obselTypeUri = traceModelUri+"Alarme";
								attributes = new HashMap<String, Object>();
								attributes.put(populationGenerateur, "Individu");
								attributes.put(roleSujetGenerateur, "Simulateur (Calculateur)");
								attributes.put(natureSujetGenerateur, "Non évalué");
								attributes.put(sousSystemeElementaire, "RPR");
								attributes.put(numeroOrdre, "1450");
								attributes.put(materiel, "KA");
								attributes.put(evenement, "En apparition");
								String obs3 = createObsel(storedTraceUri, obselTypeUri, attributes, subject, obselLabel, obselBegin, obselEnd);
								
								subject = "Op 1";
								obselLabel = "Image de conduite";
								obselBegin = "12";
								obselEnd = "16";
								obselTypeUri = traceModelUri+"ActionOperateur";
								attributes = new HashMap<String, Object>();
								attributes.put(populationGenerateur, "Individu");
								attributes.put(roleSujetGenerateur, "OperateurPrimaire");
								attributes.put(natureSujetGenerateur, "Evalué");
								attributes.put(sousSystemeElementaire, "RPR");
								attributes.put(numeroOrdre, "0015");
								attributes.put(materiel, "YE");
								String obs4 = createObsel(storedTraceUri, obselTypeUri, attributes, subject, obselLabel, obselBegin, obselEnd);
								
								subject = "CP0";
								obselLabel = "BAS TEMPS DE DOUBLEMENT CNI";
								obselBegin = "16";
								obselEnd = "16";
								obselTypeUri = traceModelUri+"Alarme";
								attributes = new HashMap<String, Object>();
								attributes.put(populationGenerateur, "Individu");
								attributes.put(roleSujetGenerateur, "Simulateur (Calculateur)");
								attributes.put(natureSujetGenerateur, "Non évalué");
								attributes.put(sousSystemeElementaire, "RPR");
								attributes.put(numeroOrdre, "1450");
								attributes.put(materiel, "KA");
								attributes.put(evenement, "En disparition");
								String obs5 = createObsel(storedTraceUri, obselTypeUri, attributes, subject, obselLabel, obselBegin, obselEnd);
								
								
								subject = "CP0";
								obselLabel = "FONCT 3 BOUCLES";
								obselBegin = "18";
								obselEnd = "20";
								obselTypeUri = traceModelUri+"Alarme";
								attributes = new HashMap<String, Object>();
								attributes.put(populationGenerateur, "Individu");
								attributes.put(roleSujetGenerateur, "Simulateur (Calculateur)");
								attributes.put(natureSujetGenerateur, "Non évalué");
								attributes.put(sousSystemeElementaire, "RPR");
								attributes.put(numeroOrdre, "1750");
								attributes.put(materiel, "KA");
								attributes.put(evenement, "En apparition");
								String obs6 = createObsel(storedTraceUri, obselTypeUri, attributes, subject, obselLabel, obselBegin, obselEnd);
								
								subject = "Op 1";
								obselLabel = "Image de conduite";
								obselBegin = "19";
								obselEnd = "26";
								obselTypeUri = traceModelUri+"ActionOperateur";
								attributes = new HashMap<String, Object>();
								attributes.put(populationGenerateur, "Individu");
								attributes.put(roleSujetGenerateur, "OperateurPrimaire");
								attributes.put(natureSujetGenerateur, "Evalué");
								attributes.put(sousSystemeElementaire, "RPR");
								attributes.put(numeroOrdre, "0015");
								attributes.put(materiel, "YE");
								String obs7 = createObsel(storedTraceUri, obselTypeUri, attributes, subject, obselLabel, obselBegin, obselEnd);
								
								subject = "CP0";
								obselLabel = "FONCT 3 BOUCLES";
								obselBegin = "24";
								obselEnd = "26";
								obselTypeUri = traceModelUri+"Alarme";
								attributes = new HashMap<String, Object>();
								attributes.put(populationGenerateur, "Individu");
								attributes.put(roleSujetGenerateur, "Simulateur (Calculateur)");
								attributes.put(natureSujetGenerateur, "Non évalué");
								attributes.put(sousSystemeElementaire, "RPR");
								attributes.put(numeroOrdre, "1750");
								attributes.put(materiel, "KA");
								attributes.put(evenement, "En disparition");
								String obs8 = createObsel(storedTraceUri, obselTypeUri, attributes, subject, obselLabel, obselBegin, obselEnd);
								
								String traceUri = insertObselsInTrace(storedTraceUri, obs1, obs2, obs2, obs3, obs4, obs5, obs6, obs7, obs8);
								if(traceUri != null){
									String traceComputedUri = baseUri + "secondTrace/";
									String traceComputedLabel = "Filtered Trace";
									String computedFilterTraceUri = createComputedTrace(baseUri, storedTraceUri, traceComputedUri, methodUri, traceComputedLabel);
									if(computedFilterTraceUri != null){
										String methodSparQlUri = baseUri + "methodSparQl/";
										String methodSparQlLabel = "SparQl";
										String modelSparQlUri = createMethodSparql(baseUri,methodSparQlUri, methodSparQlLabel);
										if(modelSparQlUri != null){
											traceComputedUri = baseUri + "thirdTrace/";
											traceComputedLabel = "SparQl trace";
											String computedSparqlTraceUri = createComputedTrace(baseUri, storedTraceUri, traceComputedUri, modelSparQlUri, traceComputedLabel);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Create a computed trace and return its uri
	 * @param baseUri
	 * @param traceSourceUri
	 * @param traceComputedUri
	 * @param methodUri
	 * @param traceComputedLabel
	 * @return
	 */
	public static String createComputedTrace(String baseUri, String traceSourceUri, String traceComputedUri, String methodUri, String traceComputedLabel){
		KtbsResponse response = service.retrieve(traceComputedUri);
		String ret = null;
		if (response.hasSucceeded()) {
			System.out.println("ComputedTrace "+traceComputedUri + " exists.");
			ret = traceComputedUri;

		} else {
			Collection<String> sourceTracesURIs = new ArrayList<String>();
			sourceTracesURIs.add(traceSourceUri);
			response = service.createComputedTrace(baseUri, traceComputedUri, methodUri, sourceTracesURIs, traceComputedLabel);
			if(response.hasSucceeded()){
				ret = response.getHTTPLocation();
			}
			
		}
		return ret;
	}
	
	/**
	 * Create a mehod base on sparql query and return its uri
	 * @param baseUri
	 * @param methodUri
	 * @param methodLabel
	 * @return
	 */
	public static String createMethodSparql(String baseUri, String methodUri, String methodLabel){
		KtbsResponse response = service.retrieve(methodUri);
		String ret = null;
		if (response.hasSucceeded()) {
			System.out.println("Method sparql  "+methodUri + " exists.");
			return methodUri;

		} else {
			String sparql = 
				"PREFIX : <http://localhost:8001/base2/model1/> " +
				"PREFIX ktbs: <http://liris.cnrs.fr/silex/2009/ktbs#> " +
				"PREFIX rdf: <http://www.w3.org/2000/01/rdf-schema#> " +
				"CONSTRUCT {" +
				"[" +
				"a :ActionInstructeur ;" +
				" ktbs:hasBegin ?beginApparition ;" +
				" ktbs:hasEnd ?endOperateur ;" +
				" ktbs:hasTrace <http://localhost:8001/base2/firstTrace/> ;" +
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
			
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("sparql", sparql);
			response = service.createMethod(baseUri, methodUri, "http://liris.cnrs.fr/silex/2009/ktbs#sparql", parameters, methodLabel);
			if(response.hasSucceeded()){
				ret = response.getHTTPLocation();
			}
			
		}
		return ret;
		
		
	}
	
	/**
	 * Insert into a trace some obsels
	 * @param traceUri
	 * @param obsels
	 * @return
	 */
	public static String insertObselsInTrace(String traceUri, String... obsels){
		String ret = null;
		try {
			KtbsResponse responseObsels = service.retrieve(traceUri + "@obsels");
			KtbsResponse responseAbout = service.retrieve(traceUri + "@about");
			
			StoredTrace t01 = null;
			if(responseAbout.hasSucceeded() && responseObsels.hasSucceeded()) {
				// Load (deserialize) the retrieved resources in the repository
				repository.loadResource(responseAbout.getBodyAsString(), JenaConstants.TURTLE);
				repository.loadResource(responseObsels.getBodyAsString(), JenaConstants.TURTLE);
				
				for (String obselUri : obsels) {
					t01 = (StoredTrace) repository.getResource(traceUri);
					RdfObsel obsel1 = (RdfObsel) repository.getResource(obselUri);
					t01.newObsel(obselUri, obsel1.getObselType(), null);
				}
				
				
				responseObsels = service.update(t01, responseObsels.getHTTPETag(), KtbsConstants.OBSELS_ASPECT);
				if(responseObsels.hasSucceeded()){
					ret = traceUri;
				}
			}
		} catch (ResourceLoadException e) {
			return null;
		}
		return ret;
	}
	
	/**
	 * Create obsel and return its uri
	 * @param traceUri
	 * @param obselTypeUri
	 * @param attributes
	 * @param subject
	 * @param obselLabel
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String createObsel(String traceUri, String obselTypeUri,
			Map<String, Object> attributes, String subject, String obselLabel, String begin, String end){
		String ret = null;
		
		KtbsResponse response = service.createObsel(traceUri, null, obselTypeUri,
				subject, 
				null, 
				null, 
				new BigInteger(begin), 
				new BigInteger(end), attributes, obselLabel);
		
		if(response.hasSucceeded()){
			if( response.getHTTPLocation() != null){
				ret = response.getHTTPLocation();
			}
		}
		System.out.println("Obsel "+ret + " created");
		return ret;
	}
	
	/**
	 * Create storedTrace and return its uri
	 * @param baseUri
	 * @param storedTraceUri
	 * @param modelUri
	 * @param traceOrigin
	 * @param traceLabel
	 * @return
	 */
	public static String createStoredTrace(String baseUri, String storedTraceUri, String modelUri, String traceOrigin, String traceLabel){
		KtbsResponse response = service.retrieve(storedTraceUri);
		String ret = null;
		if (response.hasSucceeded()) {
			System.out.println("StoredTrace filter "+storedTraceUri + " exists.");
			ret = storedTraceUri;

		} else {
			response = service.createStoredTrace(baseUri, storedTraceUri, modelUri, traceOrigin, traceLabel);
			if(response.hasSucceeded()){
				System.out.println("Stored trace "+storedTraceUri + " created");
				if( response.getHTTPLocation() != null){
					ret = response.getHTTPLocation();
				}else{
					ret = storedTraceUri;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Create method filter and return its url
	 * @param baseUri
	 * @param methodUri
	 * @param methodLabel
	 * @param start
	 * @param end
	 * @return
	 */
	
	public static String createMethodFilter(String baseUri, String methodUri, String methodLabel, String start, String end ){
		KtbsResponse response = service.retrieve(methodUri);
		String ret = null;
		if (response.hasSucceeded()) {
			System.out.println("Method filter "+methodUri + " exists.");
			ret = methodUri;

		} else {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("start", start);
			parameters.put("finish", end);
			response = service.createMethod(baseUri, methodUri, KtbsConstants.FILTER, parameters, methodLabel);
			if(response.hasSucceeded()){
				System.out.println("Method filter "+methodUri + " created");
				if( response.getHTTPLocation() != null){
					ret = response.getHTTPLocation();
				}else{
					ret = methodUri;
				}
			}
		}
		return ret;
	}
	
	
	public static String addObselTypeToModel(String traceModelUri, String... obselTypes){
		try {
			KtbsResponse response = service.retrieve(traceModelUri);
			repository.loadTraceModelResource(traceModelUri, response.getBodyAsString(), JenaConstants.TURTLE);
			TraceModel model = (TraceModel) repository.getResource(traceModelUri);
			addObselType(model, response, "ActionInstructeur","ActionOperateur", "Alarme");
		return traceModelUri;
		} catch (ResourceLoadException e) {
			return null;
		}
	}
	
	public static String changeTraceModelLabel(String traceModelUri,String  modelLabel){
		try {
			KtbsResponse response = service.retrieve(traceModelUri);
			repository.loadTraceModelResource(traceModelUri, response.getBodyAsString(), JenaConstants.TURTLE);
			TraceModel model = (TraceModel) repository.getResource(traceModelUri);
			model.setLabel(modelLabel);
			
			service.update(model, response.getHTTPETag());
			
			return traceModelUri;
		} catch (ResourceLoadException e) {
			return null;
		}
		
		
	}
	
	
	/**
	 * Create a TraceModel and return its uri
	 * @param baseUri
	 * @param traceModelUri
	 * @param modelLabel
	 * @return
	 * @throws ResourceLoadException
	 */
	public static String createTraceModel(String baseUri, String traceModelUri, String modelLabel)  throws ResourceLoadException {
		
		KtbsResponse response = service.retrieve(traceModelUri);
		String ret = null;
		if (response.hasSucceeded()) {
			System.out.println("TraceModel "+traceModelUri + " exists.");
			ret = traceModelUri;

		} else {
			System.out.println("TraceModel "+traceModelUri + " doesn't exists., so we can create it");
			response = service.createTraceModel(baseUri, traceModelUri, modelLabel);
			
			if(response.hasSucceeded() ){
				System.out.println("TraceModel "+traceModelUri + " created");
				if( response.getHTTPLocation() != null){
					ret = response.getHTTPLocation();
				}else{
					ret = traceModelUri;
				}
			}
		}
		return  ret;
	}
	
	/**
	 * Create a base and return its uri
	 * @param baseUri
	 * @param baseLabel
	 * @return
	 * @throws ResourceLoadException
	 */
	public static String createBase(String baseUri, String baseLabel)  throws ResourceLoadException {
		
		KtbsResponse response = service.retrieve(baseUri);
		String ret = null;
		if (response.hasSucceeded()) {
			System.out.println("Base "+baseUri + " exists.");
			ret = baseUri;

		} else {
			System.out.println("Base "+baseUri + " doesn't exists., so we can create it");
			response = service.createBase(SERVER_URL, baseUri, baseLabel);
			if(response.hasSucceeded()){
				System.out.println("Base "+baseUri + " created");	
			}
			
			if(response.hasSucceeded()){
				ret = response.getHTTPLocation();
			}
		}
		return  ret;
	}
	
	/**
	 * Make a sample populate
	 * @throws ResourceLoadException
	 */
	public static void populateKtbs(String baseUri, String modelUri)throws ResourceLoadException {
		KtbsResponse response = service.createBase(SERVER_URL, baseUri, "Primary base");
		System.out.println(response.getHTTPLocation());
		
		if(response.hasSucceeded()) {
			System.out.println("Base created");
			response = service.createTraceModel(baseUri, modelUri, "Model of Primary base");
			if(response.hasSucceeded()) {
				System.out.println("TraceModel created");
				response = service.retrieve(modelUri);
				repository.loadTraceModelResource(modelUri, response.getBodyAsString(), JenaConstants.TURTLE);
				TraceModel model = (TraceModel) repository.getResource(modelUri);
				model.setLabel("Model of Primary base");
				addObselType(model, response, "ActionInstructeur","ActionOperateur", "Alarme");
				
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("start", "0");
				parameters.put("finish", "10");
				String methodUri = baseUri+"method1/";
				service.createMethod(baseUri, methodUri, KtbsConstants.FILTER, parameters, "Timestamp Filter");
				
				String traceUri = baseUri+"firstTrace/";
				response = service.retrieve(traceUri);
				response = service.createStoredTrace(baseUri, traceUri, modelUri, "0", "First M-Trace");
				
				if(response.hasSucceeded()){
					System.out.println("Trace "+traceUri + " created");	
					
					//String obselUri = traceUri + "obs1";
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put(modelUri + "populationGenerateur/", "Individu");
					attributes.put(modelUri + "roleSujetGenerateur/", "Instructeur");
					attributes.put(modelUri + "natureSujetGenerateur/", "Non évalué");
					attributes.put(modelUri + "sousSystemeElementaire/", "");
					attributes.put(modelUri + "numeroOrdre/", "");
					attributes.put(modelUri + "materiel/", "");
					KtbsResponse responseO = service.createObsel(traceUri, null, modelUri+"ActionInstructeur",
							"Instructeur 1", 
							null, 
							null, 
							new BigInteger("0"), 
							new BigInteger("5"), attributes, "Demande de changement de mode");
					
					String obselUri = responseO.getHTTPLocation();
					
					//String obselUri2 = traceUri + "obs10";
					 attributes = new HashMap<String, Object>();
						attributes.put(modelUri + "populationGenerateur/", "Individu");
						attributes.put(modelUri + "roleSujetGenerateur/", "OperateurPrimaire");
						attributes.put(modelUri + "natureSujetGenerateur/", "Evalué");
						attributes.put(modelUri + "sousSystemeElementaire/", "RPR");
						attributes.put(modelUri + "numeroOrdre/", "0053");
						attributes.put(modelUri + "materiel/", "YC");
					responseO = service.createObsel(traceUri, null, modelUri + "ActionOperateur",
							"Op 1", 
							null, 
							null, 
							new BigInteger("2"), 
							new BigInteger("10"), attributes, "Image de conduite");
					String obselUri2 = responseO.getHTTPLocation();
					
					attributes = new HashMap<String, Object>();
					attributes.put(modelUri + "populationGenerateur/", "Individu");
					attributes.put(modelUri + "roleSujetGenerateur/", "OperateurPrimaire");
					attributes.put(modelUri + "natureSujetGenerateur/", "Evalué");
					attributes.put(modelUri + "sousSystemeElementaire/", "RPR");
					attributes.put(modelUri + "numeroOrdre/", "0015");
					attributes.put(modelUri + "materiel/", "YE");
				responseO = service.createObsel(traceUri, null, modelUri + "ActionOperateur",
						"Op 1", 
						null, 
						null, 
						new BigInteger("5"), 
						new BigInteger("12"), attributes, "Image de conduite");
				String obselUri21 = responseO.getHTTPLocation();
					
					//String obselUri3 = traceUri + "obs14";
					 attributes = new HashMap<String, Object>();
						attributes.put(modelUri + "populationGenerateur/", "Individu");
						attributes.put(modelUri + "roleSujetGenerateur/", "Simulateur (Calculateur)");
						attributes.put(modelUri + "natureSujetGenerateur/", "Non évalué");
						attributes.put(modelUri + "sousSystemeElementaire/", "RPR");
						attributes.put(modelUri + "numeroOrdre/", "1450");
						attributes.put(modelUri + "materiel/", "KA");
						attributes.put(modelUri + "evenement/", "En apparition");
					responseO = service.createObsel(traceUri, null, modelUri + "Alarme",
							"CP0", 
							null, 
							null, 
							new BigInteger("11"), 
							new BigInteger("15"), attributes, "BAS TEMPS DE DOUBLEMENT CNI");
					String obselUri3 = responseO.getHTTPLocation();
					
					attributes = new HashMap<String, Object>();
					attributes.put(modelUri + "populationGenerateur/", "Individu");
					attributes.put(modelUri + "roleSujetGenerateur/", "Simulateur (Calculateur)");
					attributes.put(modelUri + "natureSujetGenerateur/", "Non évalué");
					attributes.put(modelUri + "sousSystemeElementaire/", "RPR");
					attributes.put(modelUri + "numeroOrdre/", "1450");
					attributes.put(modelUri + "materiel/", "KA");
					attributes.put(modelUri + "evenement/", "En disparition");
				responseO = service.createObsel(traceUri, null, modelUri + "Alarme",
						"CP0", 
						null, 
						null, 
						new BigInteger("16"), 
						new BigInteger("17"), attributes, "BAS TEMPS DE DOUBLEMENT CNI");
				String obselUri31 = responseO.getHTTPLocation();
					
				
				
				
					KtbsResponse responseObsels = service.retrieve(traceUri + "@obsels");
					KtbsResponse responseAbout = service.retrieve(traceUri + "@about");
					
					StoredTrace t01 = null;
					if(responseAbout.hasSucceeded() && responseObsels.hasSucceeded()) {
						// Load (deserialize) the retrieved resources in the repository
						repository.loadResource(responseAbout.getBodyAsString(), JenaConstants.TURTLE);
						repository.loadResource(responseObsels.getBodyAsString(), JenaConstants.TURTLE);
						
						t01 = (StoredTrace) repository.getResource(traceUri);
						RdfObsel obsel1 = (RdfObsel) repository.getResource(obselUri);
						t01.newObsel(obselUri, obsel1.getObselType(), null);
						
						RdfObsel obsel2 = (RdfObsel) repository.getResource(obselUri2);
						t01.newObsel(obselUri2, obsel2.getObselType(), null);
						RdfObsel obsel21 = (RdfObsel) repository.getResource(obselUri21);
						t01.newObsel(obselUri21, obsel21.getObselType(), null);
						
						RdfObsel obsel3 = (RdfObsel) repository.getResource(obselUri3);
						t01.newObsel(obselUri3, obsel3.getObselType(), null);
						RdfObsel obsel31 = (RdfObsel) repository.getResource(obselUri31);
						t01.newObsel(obselUri31, obsel31.getObselType(), null);
						
						responseObsels = service.update(t01, responseObsels.getHTTPETag(), KtbsConstants.OBSELS_ASPECT);
						
						responseAbout = service.retrieve(traceUri + "@about");
						repository.loadResource(responseAbout.getBodyAsString(), JenaConstants.TURTLE);
						t01 = (StoredTrace) repository.getResource(traceUri);
						/*t01.addProperty("idSimulateur", "CP0");
						t01.addProperty("idTranche", "2");
						t01.addProperty("Formation", "Initiale");
						t01.addProperty("Cursus", "CESN FI Operateur");*/
						
						//responseAbout = service.update(t01, responseAbout.getHTTPETag(), KtbsConstants.ABOUT_ASPECT);
						
						
						Collection<String> sourceTracesURIs = new ArrayList<String>();
						sourceTracesURIs.add(traceUri);
						service.createComputedTrace(baseUri, baseUri+"secondTrace/", methodUri, sourceTracesURIs, "Filter trace");
						
						String sparql = 
							"PREFIX : <http://localhost:8001/base2/model1/>" +
							"PREFIX ktbs: <http://liris.cnrs.fr/silex/2009/ktbs#>" +
							"CONSTRUCT{" +
							"[" +
							"a :ActionInstructeur ;" +
							"ktbs:hasBegin ?begin ;" +
							"ktbs:hasEnd ?end ;" +
							"ktbs:hasSourceObsel ?alarme, ?actionoperateur " +
							"]" +
							"} " +
							"WHERE{" +
							"?alarme " +
							" a :Alarme ;" +
							//" ktbs:label \"BAS TEMPS DE DOUBLEMENT CNI\" ;"+
							" ktbs:hasBegin ?begin ." +
							"?actionoperateur " +
							" a :ActionOperateur ;" +
							//" :numeroOrdre \"0015\" ;"+
							" ktbs:hasEnd ?end ." +
							"}";
						
						methodUri = baseUri+"method2/";
						parameters = new HashMap<String, String>();
						parameters.put("sparql", sparql);
						service.createMethod(baseUri, methodUri, "http://liris.cnrs.fr/silex/2009/ktbs#sparql", parameters, "SparQl");
						service.createComputedTrace(baseUri, baseUri+"thirdTrace/", methodUri, sourceTracesURIs, "SparQl trace");
					}
					
				}else{
					System.out.println(response.getServerMessage());
				}
			}else{
				System.out.println(response.getServerMessage());
			}
		}else{
			System.out.println(response.getServerMessage());
		}
	}
	
	/**
	 * 
	 */
	public static void createObsel(String modelUri, String traceUri) throws ResourceLoadException{
		KtbsResponse responseObsels = service.retrieve(traceUri + "@obsels");
		KtbsResponse responseAbout = service.retrieve(traceUri + "@about");

		KtbsResponse responseM = service.retrieve(modelUri);
		repository.loadResource(responseM.getBodyAsString(), JenaConstants.TURTLE);
		
		StoredTrace t01 = null;
		if(responseAbout.hasSucceeded()) {
			// Load (deserialize) the retrieved resources in the repository
			repository.loadResource(responseAbout.getBodyAsString(), JenaConstants.TURTLE);
			repository.loadResource(responseObsels.getBodyAsString(), JenaConstants.TURTLE);
			
			t01 = (StoredTrace) repository.getResource(traceUri);
			
			String obselUri = "http://localhost:8001/base1/t01/monOb";
			
			KtbsResponse responseO = service.createObsel(traceUri, obselUri, modelUri + "RecvMsg", "Damien", 
					null, 
					null, 
					new BigInteger("10000"), 
					new BigInteger("12000"), null, "Demande de changement de mode");
			
			RdfObsel obsel1 = (RdfObsel) repository.getResource(obselUri);
			
			t01.newObsel(obselUri, obsel1.getObselType(), null);
			/*Obsel obsel = (Obsel) t01.getObsel(obselUri);
			obsel.addProperty("http://localhost:8001/base1/t01/plop", "une deuxième valeur");
			obsel.removeProperty("http://localhost:8001/base1/t01/plop");*/
			
			responseObsels = service.update(t01, responseObsels.getHTTPETag(), KtbsConstants.OBSELS_ASPECT);
			
			if(responseObsels.hasSucceeded()) {
				
			}else{
				System.out.println(responseObsels.getServerMessage());
			}
		}
	}
	
	/**
	 * Print all objects : Model, Trace and Obsel with all their parameters of base "base1"
	 * @throws ResourceLoadException
	 */
	/*public static void printAll() throws ResourceLoadException {
		printBase(BASE_URI);
		printModel(MODEL_URI);
		printTrace(TRACE_URI);
		printObsels(TRACE_URI);
	}*/
	
	/**
	 * Create a Trace with name traceName in the baseUri
	 * @param baseUri
	 * @throws ResourceLoadException
	 */
	public static void createTrace(String baseUri, String traceName, String modelUri)  throws ResourceLoadException {
		
		String traceUri = baseUri+traceName+"/";
		KtbsResponse response = service.retrieve(traceUri);
		if (response.hasSucceeded()) {
			System.out.println("Trace "+traceUri + " exists.");

		} else {
			System.out.println("Base "+traceUri + " doesn't exists., so we can create it");
			
			response = service.createTraceModel(baseUri, modelUri, "label trace model");
			
			response = service.createStoredTrace(baseUri, traceUri, modelUri, "0", "stored trace label");
			if(response.hasSucceeded()){
				System.out.println("Trace "+traceUri + " created");	
			}
			
			Base base = repository.createBase(baseUri);
			TraceModel traceModel = repository.createTraceModel(base, baseUri);
			
			StoredTrace storedTrace = repository.createStoredTrace(base, traceUri, traceModel, "stored trace label");
			//storedTrace.setLabel("My first trace");
			//storedTrace.addProperty("NewProperty", "strValue");
			response = service.retrieve(traceUri);
			response = service.update(storedTrace, response.getHTTPETag(),KtbsConstants.ABOUT_ASPECT);
			
			if(response.hasSucceeded()){
				System.out.println("Great");
			}else{
				System.out.println(response.getServerMessage());
			}
		}
	}
	
	
	
	/**
	 * Print Model "Base1"
	 * @param baseUri
	 * @throws ResourceLoadException
	 */
	public static void printBase(String baseUri)  throws ResourceLoadException {
		/*
		 * Retrieve http://localhost:8001/base1/
		 */
		KtbsResponse response = service.retrieve(baseUri);
		Base base1 = null;
		if (response.hasSucceeded()) {
			// Load (deserialize) the retrieved resource in the repository
			repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);

			// Get the resource from the repository
			base1 = (Base) repository.getResource(baseUri);
			System.out.println(base1.toString());

		} else {
			System.out.println("I could not retrieve the resource.");
			System.out.println(response.getServerMessage());
		}
	}
	
	/**
	 * Add obselType to model
	 * @param model
	 */
	public static void addObselType(TraceModel model,KtbsResponse response, String... obseltypeName ){
		
		for (String obs : obseltypeName) {
			ObselType obsT = model.newObselType(obs);
			obsT.setLabel(obs);
			//toto.setSuperObselType(model.getObselType(MODEL_URI + "RecvMsg"));
			//toto.setSuperObselType(null);
			String etag = response.getHTTPETag();
			response = service.update(model, etag);
			if(response.hasSucceeded()) 
				System.out.println("The trace model has been remotely modified");
			else
				System.out.println("Problem: " + response.getServerMessage());
			response = service.retrieve(model.getURI());
		}
	}
	
	/**
	 * Print Model "Model1" of "base1"
	 * @throws ResourceLoadException
	 */
	public static void printModel(String modelUri)  throws ResourceLoadException {
		/*
		 * Retrieve http://localhost:8001/base1/model1/
		 */
		KtbsResponse response = service.retrieve(modelUri);
		TraceModel model1 = null;
		if (response.hasSucceeded()) {
			// Load (deserialize) the retrieved resource in the repository
			repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);

			// Get the resource from the repository
			model1 = (TraceModel) repository.getResource(modelUri);
			System.out.println(model1.toString());

		} else {
			System.out.println("I could not retrieve the resource.");
			System.out.println(response.getServerMessage());
		}
	}
	
	/**
	 * Print Trace "t01" of "model1" of "base1"
	 * @param traceUri
	 * @throws ResourceLoadException
	 */
	public static void printTrace(String traceUri) throws ResourceLoadException {
		
		KtbsResponse responseObsels = service.retrieve(traceUri + "@obsels");
		KtbsResponse responseAbout = service.retrieve(traceUri + "@about");

		StoredTrace t01 = null;
		if(responseAbout.hasSucceeded()) {
			// Load (deserialize) the retrieved resources in the repository
			repository.loadResource(responseAbout.getBodyAsString(), JenaConstants.TURTLE);
			repository.loadResource(responseObsels.getBodyAsString(), JenaConstants.TURTLE);
			// Get the resource from the repository
			t01 = (StoredTrace) repository.getResource(traceUri);
			System.out.println(t01.toString());
			
			if(t01 instanceof RdfComputedTrace){
				System.out.println(((RdfComputedTrace) t01).getMethod().getLabel());
			}else{
				System.out.println(((RdfStoredTrace) t01).getURI());
			}
			
			
		}
	}
	
	/**
	 * Print all obsels of trace "t01" of model "Model1" of base "base1"
	 * @param traceUri
	 * @throws ResourceLoadException
	 */
	public static void printObsels(String traceUri) throws ResourceLoadException {
	
		KtbsResponse responseObsels = service.retrieve(traceUri + "@obsels");
		StoredTrace t01 = null;
		if( responseObsels.hasSucceeded()) {
			// Load (deserialize) the retrieved resources in the repository
			//repository.loadResource(responseObsels.getBody(), JenaConstants.TURTLE);

			// Get the resource from the repository
			t01 = (StoredTrace) repository.getResource(traceUri);			

			// Iterates over obsels
			for(Obsel obsel:KtbsUtils.toIterable(t01.listObsels())) {
								
				System.out.println(obsel.toString());
				
				
			}
		} else  {
			System.out.println("I could not retrieve the resource.");
			System.out.println(responseObsels.getServerMessage());
		}
		
		
		System.out.println("-----------------------------");
		
		
	}
}
