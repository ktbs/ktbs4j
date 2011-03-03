package org.liris.ktbs.service.tests;

import org.liris.ktbs.core.Ktbs;
import org.liris.ktbs.core.KtbsClient;
import org.liris.ktbs.core.domain.ResourceFactory;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.TraceModelService;

public class RetroRoomTraceModelCreation {

	private KtbsClient client;
	private ResourceFactory factory;
	
	public RetroRoomTraceModelCreation(KtbsClient restClient) {
		this.client = restClient;
		traceModelService = client.getTraceModelService();
		resourceService = client.getResourceService();
		factory = Ktbs.getPojoFactory();
	}

	public static void main(String[] args) {
		RetroRoomTraceModelCreation app = new RetroRoomTraceModelCreation(Ktbs.getRestClient());

		app.create();
	}

	private TraceModelService traceModelService;
	private ResourceService resourceService;
	
	private void create() {
		
		ITraceModel model = traceModelService.createTraceModel("http://localhost:8001/base1/test2/");
		
//		try {
//			model = resourceService.newTraceModel("http://localhost:8001/base1/", "visuRxetro");
//		} catch(Exception e) {
//			model = resourceService.getKtbsResource("http://localhost:8001/base1/visuRetro/", ITraceModel.class);
//		}

		IObselType startEdit = traceModelService.newObselType(model, "StartEdit");
		IObselType edit = traceModelService.newObselType(model, "Edit");
		IObselType edsit = traceModelService.newObselType(model, "Edit234");
		startEdit.getSuperObselTypes().add(edit);
		edsit.getSuperObselTypes().add(edit);
		
		traceModelService.save(model, true);
	}
}
