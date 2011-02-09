package org.liris.ktbs.example;

import org.liris.ktbs.client.KtbsClientApplication;
import org.liris.ktbs.client.KtbsResponse;
import org.liris.ktbs.client.KtbsRestService;
import org.liris.ktbs.core.JenaConstants;
import org.liris.ktbs.core.ResourceLoadException;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.rdf.resource.RdfResourceRepository;
import org.liris.ktbs.rdf.resource.RdfStoredTrace;
import org.liris.ktbs.utils.KtbsUtils;

/**
 * 
 * @author Dino
 *
 */
public class XmlBaseGenerator {

	private static final String SERVER_URL = "http://localhost:8001/";
	private static final String BASE_URI = SERVER_URL +"base2/";
	private static KtbsClientApplication app = KtbsClientApplication.getInstance();
	private static KtbsRestService service = app.getRestService(SERVER_URL);
	private static ResourceRepository repository = new RdfResourceRepository();
	
	/**
	 * @param args
	 * @throws ResourceLoadException 
	 */
	public static void main(String[] args) throws ResourceLoadException {
		KtbsResponse response = service.retrieve(BASE_URI);
		repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);
		response = service.retrieve(BASE_URI+"firstTrace/@obsels");
		repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);
		response = service.retrieve(BASE_URI+"firstTrace/@about");
		repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);
		response = service.retrieve(BASE_URI+"model1/");
		repository.loadTraceModelResource(BASE_URI+"model1/",response.getBodyAsString(), JenaConstants.TURTLE);
		
		response = service.retrieve(BASE_URI+"methodFilter1/");
		repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);
		response = service.retrieve(BASE_URI+"methodSparQl/");
		repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);
		
		response = service.retrieve(BASE_URI+"secondTrace/@obsels");
		repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);
		response = service.retrieve(BASE_URI+"secondTrace/@about");
		repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);
		response = service.retrieve(BASE_URI+"thirdTrace/@obsels");
		repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);
		response = service.retrieve(BASE_URI+"thirdTrace/@about");
		repository.loadResource(response.getBodyAsString(), JenaConstants.TURTLE);
		
		
		Base base = (Base)repository.getResource(BASE_URI);
		RdfStoredTrace rdfStoredTrace = (RdfStoredTrace)repository.getResource(BASE_URI+"firstTrace/");
		for (Obsel obsel : KtbsUtils.toIterable(rdfStoredTrace.listObsels())) {
			System.out.println(obsel);
		} 
		
		
	}

}
