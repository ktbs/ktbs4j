package org.liris.ktbs.service.tests;

import org.liris.ktbs.core.KtbsClient;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.TraceModelService;

public class RetroRoomTraceModelCreation {

	public static void main(String[] args) {
		RetroRoomTraceModelCreation app = new RetroRoomTraceModelCreation();

		app.create();
	}

	private ResourceService resourceService;
	private TraceModelService traceModelService;
	
	private void create() {
		resourceService = KtbsClient.getRestResourceService();
		traceModelService = KtbsClient.getRestTraceModelService();
		
		ITraceModel model = resourceService.newTraceModel("http://localhost:8001/base1/", "visuRetro");
		
		IObselType startEdit = traceModelService.newObselType(model, "StartEdit");
		IObselType endEdit = traceModelService.newObselType(model, "EndEdit");
		IObselType edit = traceModelService.newObselType(model, "Edit");
		startEdit.getSuperObselTypes().add(edit);
		endEdit.getSuperObselTypes().add(edit);
		
		traceModelService.save(model);
	}
}
