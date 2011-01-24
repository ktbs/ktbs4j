package org.liris.ktbs.example;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.liris.ktbs.client.KtbsClientApplication;
import org.liris.ktbs.client.KtbsResponse;
import org.liris.ktbs.client.KtbsRestService;
import org.liris.ktbs.core.JenaConstants;
import org.liris.ktbs.core.ResourceLoadException;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.Mode;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.rdf.resource.RdfResourceRepository;
import org.liris.ktbs.utils.KtbsUtils;

public class InteractWithRemoteKTBSServer {

	public static void main(String[] args) throws ResourceLoadException {
		KtbsClientApplication app = KtbsClientApplication.getInstance();
		KtbsRestService service = app.getRestService("http://localhost:8001/");

		ResourceRepository repository = new RdfResourceRepository();

		/*
		 * PREREQUISITE: the KTBS server has been fulled with populate-ktbs and populate-t01
		 */


		/*
		 * Retrieve http://localhost:8001/base1/
		 */
		// Perform the REST request
		KtbsResponse response = service.retrieve("http://localhost:8001/base1/");
		if(response.hasSucceeded()) {
			// Load (deserialize) the retrieved resource in the repository
			repository.loadResource(response.getBody(), JenaConstants.TURTLE);

			// Get the resource from the repository
			Base base1 = (Base) repository.getResource("http://localhost:8001/base1/");

			System.out.println("Label: " + base1.getLabel());
			System.out.println("Type: " + base1.getResourceType());

		} else  {
			System.out.println("I could not retrieve the resource.");
			System.out.println(response.getServerMessage());
		}
		System.out.println("***********************************************");



		/*
		 * Retrieve http://localhost:8001/base1/model1/
		 */
		// Perform the REST request
		response = service.retrieve("http://localhost:8001/base1/model1/");
		TraceModel model1 = null;
		if(response.hasSucceeded()) {
			// Load (deserialize) the retrieved resource in the repository
			repository.loadResource(response.getBody(), JenaConstants.TURTLE);

			// Get the resource from the repository
			model1 = (TraceModel) repository.getResource("http://localhost:8001/base1/model1/");

			System.out.println("Label: " + model1.getLabel());
			System.out.println("Type: " + model1.getResourceType());


			Iterator<AttributeType> attTypes = model1.listAttributeTypes();
			while (attTypes.hasNext()) {
				AttributeType attributeType = (AttributeType) attTypes.next();
				System.out.println("\t" + attributeType.getURI() + " ("+attributeType.getDomain()+")");
			}

			Iterator<RelationType> relTypes = model1.listRelationTypes();
			// KtbsUtils class can be used to facilitate the iteration
			for(RelationType relType:KtbsUtils.toIterable(relTypes)) 
				System.out.println("\t" + relType.getURI() + " (domain: "+relType.getDomain()+", range: "+relType.getRange()+")");

		} else  {
			System.out.println("I could not retrieve the resource.");
			System.out.println(response.getServerMessage());
		}
		System.out.println("***********************************************");


		/*
		 * Change the trace model http://localhost:8001/base1/model1/
		 */
		ObselType toto = model1.newObselType("Toto");
		toto.setLabel("Mon obsel type perso");
		toto.setSuperObselType(model1.getObselType("http://localhost:8001/base1/model1/OpenChat"));
		
		String etag = response.getHTTPETag();
		response = service.update(model1, etag);
		if(response.hasSucceeded()) 
			System.out.println("The trace model has been remotely modified");
		else
			System.out.println("Problem: " + response.getServerMessage());
			
		
		/*
		 * Retrieve http://localhost:8001/base1/t01/
		 */

		// Perform the REST request
		KtbsResponse responseAbout = service.retrieve("http://localhost:8001/base1/t01/@about");
		KtbsResponse responseObsels = service.retrieve("http://localhost:8001/base1/t01/@obsels");
		StoredTrace t01 = null;
		if(responseAbout.hasSucceeded() && responseObsels.hasSucceeded()) {
			// Load (deserialize) the retrieved resources in the repository
			repository.loadResource(responseAbout.getBody(), JenaConstants.TURTLE);
			repository.loadResource(responseObsels.getBody(), JenaConstants.TURTLE);

			// Get the resource from the repository
			t01 = (StoredTrace) repository.getResource("http://localhost:8001/base1/t01/");

			System.out.println("Label: " + t01.getLabel());
			System.out.println("Type: " + t01.getResourceType());
			System.out.println("Number of obsels: " + KtbsUtils.count(t01.listObsels()));

			// Iterates over obsels
			for(Obsel obsel:KtbsUtils.toIterable(t01.listObsels())) {
				System.out.println("----");
				System.out.println(obsel.getURI());
				System.out.println(obsel.getLabel());
				System.out.println(obsel.getResourceType());
				ObselType obselType = obsel.getObselType();
				System.out.println(obselType.getURI());
				System.out.println(obsel.getBeginDT());
				System.out.println(obsel.getEndDT());
				System.out.println(obsel.getBegin());
				System.out.println(obsel.getEnd());

				for(AttributeType attType:KtbsUtils.toIterable(obselType.listAttributes(Mode.INFERRED)))
					System.out.println("\t (att) - " + attType.getURI() + ": " + obsel.getAttributeValue(attType));

				Iterable<RelationType> relations = KtbsUtils.toIterable(obselType.listOutgoingRelations(Mode.INFERRED));
				for(RelationType relType:relations)
					System.out.println("\t (rel) - " + relType.getURI() + ": " + obsel.getTargetObsel(relType));
			}
		} else  {
			System.out.println("I could not retrieve the resource.");
			System.out.println(response.getServerMessage());
		}
		
		
		System.out.println("-----------------------------");
		
		/*
		 * Add a new obsel
		 */
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("http://localhost:8001/base1/model1/message", "Est-ce que ça va ?");
		response = service.createObsel(
				t01.getURI(), 
				"http://localhost:8001/base1/t01/obs5", 
				"http://localhost:8001/base1/model1/SendMsg", 
				"Damien", 
				null, 
				null, 
				new BigInteger("10000"), 
				new BigInteger("12000"), 
				attributes);
		
		if(response.hasSucceeded()) 
			System.out.println("The obsel has been remotely created");
		else
			System.out.println("The obsel has not been created: " + response.getServerMessage());
	}
	
	
	
	
}
