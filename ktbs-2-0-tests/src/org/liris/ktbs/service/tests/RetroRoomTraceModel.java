package org.liris.ktbs.service.tests;

import org.liris.ktbs.core.Ktbs;
import org.liris.ktbs.core.KtbsClient;
import org.liris.ktbs.core.domain.ResourceFactory;
import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.TraceModelService;

public class RetroRoomTraceModel {

	private KtbsClient client;
	private TraceModelService traceModelService;
	private ResourceFactory factory;
	
	public RetroRoomTraceModel(KtbsClient restClient) {
		this.client = restClient;
		traceModelService = client.getTraceModelService();
		factory = Ktbs.getPojoFactory();
	}

	public static void main(String[] args) {
		RetroRoomTraceModel app = new RetroRoomTraceModel(Ktbs.getRestClient());
		app.create("http://localhost:8001/base1/", "visuModel");
	}

	
	private void create(String base, String modelName) {
		ITraceModel model = traceModelService.createTraceModel(base + modelName + "/");
		
		IObselType retroEvent = traceModelService.newObselType(model,"RetroEvent");
		IObselType retroTraceVisualizationEvent = traceModelService.newObselType(model,"RetroTraceVisualizationEvent");
		IObselType retroExpandTraceLineEvent =  traceModelService.newObselType(model,"RetroExpandTraceLineEvent");
		IObselType retroMinimizeTraceLineEvent =  traceModelService.newObselType(model,"RetroMinimizeTraceLineEvent");
		IObselType retroTraceTimeScaleEvent =  traceModelService.newObselType(model,"RetroTraceTimeScaleEvent");
		IObselType retroStartScaleChangeEvent =  traceModelService.newObselType(model,"RetroStartScaleChangeEvent");
		IObselType retroEndScaleChangeEvent =  traceModelService.newObselType(model,"RetroEndScaleChangeEvent");
		IObselType retroTraceTimeSlideEvent =  traceModelService.newObselType(model,"RetroTraceTimeSlideEvent");
		IObselType retroStartTraceTimeSlideEvent =  traceModelService.newObselType(model,"RetroStartTraceTimeSlideEvent");
		IObselType retroEndTraceTimeSlideEvent =  traceModelService.newObselType(model,"RetroEndTraceTimeSlideEvent");
		IObselType retroExploreObselEvent =  traceModelService.newObselType(model,"RetroExploreObselEvent");
		IObselType retroObselTypeLineEvent =  traceModelService.newObselType(model,"RetroObselTypeLineEvent");
		IObselType retroAddObselTypeToLineEvent =  traceModelService.newObselType(model,"RetroAddObselTypeToLineEvent");
		IObselType retroDeleteObselTypeFromLineEvent =  traceModelService.newObselType(model,"RetroDeleteObselTypeFromLineEvent");
		IObselType retroWorkbenchEvent =  traceModelService.newObselType(model,"RetroWorkbenchEvent");
		IObselType retroViewResizeEvent =  traceModelService.newObselType(model,"RetroViewResizeEvent");
		IObselType retroStartViewResizeEvent =  traceModelService.newObselType(model,"RetroStartViewResizeEvent");
		IObselType retroEndViewResizeEvent =  traceModelService.newObselType(model,"RetroEndViewResizeEvent");
		IObselType retroActiveTabEvent =  traceModelService.newObselType(model,"RetroActiveTabEvent");
		IObselType retroCommentEvent =  traceModelService.newObselType(model,"RetroCommentEvent");
		IObselType retroStartCreateCommentEvent =  traceModelService.newObselType(model,"RetroStartCreateCommentEvent");
		IObselType retroClickButtonStartCreateCommentEvent =  traceModelService.newObselType(model,"RetroClickButtonStartCreateCommentEvent");
		IObselType retroDoubleClickTraceLineStartCreateCommentEvent =  traceModelService.newObselType(model,"RetroDoubleClickTraceLineStartCreateCommentEvent");
		IObselType retroDropObselStartCreateCommentEvent =  traceModelService.newObselType(model,"RetroDropObselStartCreateCommentEvent");
		IObselType retroStartEditEvent =  traceModelService.newObselType(model,"RetroStartEditEvent");
		IObselType retroCancelEditEvent =  traceModelService.newObselType(model,"RetroCancelEditEvent");
		IObselType retroDeleteCommentEvent =  traceModelService.newObselType(model,"RetroDeleteCommentEvent");
		IObselType retroSaveCommentEvent =  traceModelService.newObselType(model,"RetroSaveCommentEvent");
		IObselType retroCommentTimeEvent =  traceModelService.newObselType(model,"RetroCommentTimeEvent");
		IObselType retroCommentStartDurationChangeEvent =  traceModelService.newObselType(model,"RetroCommentStartDurationChangeEvent");
		IObselType retroCommentEndDurationChangeEvent =  traceModelService.newObselType(model,"RetroCommentEndDurationChangeEvent");
		IObselType retroCommentStartSlideEvent =  traceModelService.newObselType(model,"RetroCommentStartSlideEvent");
		IObselType retroCommentEndSlideEvent =  traceModelService.newObselType(model,"RetroCommentEndSlideEvent");
		IObselType retroLoadRetrospectedSessionEvent =  traceModelService.newObselType(model,"RetroLoadRetrospectedSessionEvent");
		IObselType retroExitRetrospectedSessionEvent =  traceModelService.newObselType(model,"RetroExitRetrospectedSessionEvent");
		IObselType retroVideoEvent =  traceModelService.newObselType(model,"RetroVideoEvent");
		IObselType retroPauseVideoEvent =  traceModelService.newObselType(model,"RetroPauseVideoEvent");
		IObselType retroPlayVideoEvent =  traceModelService.newObselType(model,"RetroPlayVideoEvent");
		IObselType retroVideoGoToTimeEvent =  traceModelService.newObselType(model,"RetroVideoGoToTimeEvent");
		IObselType retroPlayFromObselEvent =  traceModelService.newObselType(model,"RetroPlayFromObselEvent");

		
		IAttributeType syncRoomTraceId =  traceModelService.newAttributeType(model,"syncRoomTraceId", null);
		syncRoomTraceId.getDomains().add(retroEvent);
		
		IAttributeType traceSubjectId =  traceModelService.newAttributeType(model,"traceSubjectId", null);
		traceSubjectId.getDomains().add(retroTraceVisualizationEvent);
		
		IAttributeType userName =  traceModelService.newAttributeType(model,"userName", null);
		userName.getDomains().add(retroTraceVisualizationEvent);

		IAttributeType userAvatar =  traceModelService.newAttributeType(model,"userAvatar", null);
		userAvatar.getDomains().add(retroTraceVisualizationEvent);
		
		IAttributeType timeWindowLowerBound =  traceModelService.newAttributeType(model,"timeWindowLowerBound", null);
		timeWindowLowerBound.getDomains().add(retroStartScaleChangeEvent);
		timeWindowLowerBound.getDomains().add(retroTraceTimeSlideEvent);
		
		IAttributeType timeWindowUpperBound =  traceModelService.newAttributeType(model,"timeWindowUpperBound", null);
		timeWindowUpperBound.getDomains().add(retroStartScaleChangeEvent);
		timeWindowUpperBound.getDomains().add(retroTraceTimeSlideEvent);
		
		IAttributeType scaleValue =  traceModelService.newAttributeType(model,"scaleValue", null);
		scaleValue.getDomains().add(retroStartScaleChangeEvent);
		
		IAttributeType obselId =  traceModelService.newAttributeType(model,"obselId", null);
		
		
		IAttributeType tooltipValue =  traceModelService.newAttributeType(model,"tooltipValue", null);
		IAttributeType obselTypeName =  traceModelService.newAttributeType(model,"obselTypeName", null);
		IAttributeType uiWidget =  traceModelService.newAttributeType(model,"uiWidget", null);
		IAttributeType newActiveTab =  traceModelService.newAttributeType(model,"newActiveTab", null);
		IAttributeType commentValue =  traceModelService.newAttributeType(model,"commentValue", null);
		IAttributeType commentId =  traceModelService.newAttributeType(model,"commentId", null);
		IAttributeType commentBeginDate =  traceModelService.newAttributeType(model,"commentBeginDate", null);
		IAttributeType commentEndDate =  traceModelService.newAttributeType(model,"commentEndDate", null);
		IAttributeType videoViewWidth =  traceModelService.newAttributeType(model,"videoViewWidth", null);
		IAttributeType videoViewHeight =  traceModelService.newAttributeType(model,"videoViewHeight", null);
		IAttributeType traceLineViewWidth =  traceModelService.newAttributeType(model,"traceLineViewWidth", null);
		IAttributeType traceLineViewHeight =  traceModelService.newAttributeType(model,"traceLineViewHeight", null);
		IAttributeType tabHolderWidth =  traceModelService.newAttributeType(model,"tabHolderWidth", null);
		IAttributeType tabHolderHeight =  traceModelService.newAttributeType(model,"tabHolderHeight", null);
		IAttributeType editType =  traceModelService.newAttributeType(model,"editType", null);
		IAttributeType sessionId =  traceModelService.newAttributeType(model,"sessionId", null);
		IAttributeType cause =  traceModelService.newAttributeType(model,"cause", null);
		IAttributeType videoTime =  traceModelService.newAttributeType(model,"videoTime", null);
		
		model.getLabels().add("Le modèle de trace du salon de rétrospection de visu");
		traceModelService.save(model, true);
	}
}
