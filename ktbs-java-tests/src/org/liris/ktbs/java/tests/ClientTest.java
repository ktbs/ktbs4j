package org.liris.ktbs.java.tests;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.liris.ktbs.client.KTBSClientApplication;
import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.client.KtbsResponse;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;

public class ClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KTBSClientApplication app = KTBSClientApplication.getInstance();
		
		KtbsClient client = app.getKtbsClient("http://localhost:8001/");
		client.startSession();
		
//		KtbsResponse response = client.getObsel("http://localhost:8001/base1/t01/0cd86043ba40d40aa91ad18bdd714634");
//		KtbsResponse response = client.getTraceObsels("http://localhost:8001/base1/t01/");
//		KtbsResponse response = client.getTraceInfo("http://localhost:8001/base1/t01/");
//		KtbsResponse response = client.getKtbsRoot("http://localhost:8001/");
//		KtbsResponse response = client.getBase("http://localhost:8001/","base1");
//		KtbsResponse response = client.addObselsToTrace("http://localhost:8001/","t01");
//		KtbsResponse response = client.createBase("http://localhost:8001/","baseHector2", "Une base n pour tester.");
//		KtbsResponse response = client.createTrace("http://localhost:8001/baseHector2/", "traceHermes","http://localhost:8001/baseHector2/modelRene/",new Date(), "Une trace pour tester.");
//		KtbsResponse response = client.createTraceModel("http://localhost:8001/baseHector2/", "modelRene","Super modèle qui dechire");
		
//		TraceBuilder traceBuilder = new TraceBuilder();
//		traceBuilder.createNewTrace("http://localhost:8001/base1/t01/", "http://localhost:8001/base1/model1/", "Une trace", "24-12-2010", "10:00:00");
////		traceBuilder.addObsel("10:18:00", "10:25:00", "Joseph", "Observé que j'ai vu", "OpenChat", "http://localhost:8001/base1/t01/model1/#channel", "Mon channel 3");
//		traceBuilder.addObsel("10:20:00", "10:20:00", "Damien", "Observé que j'ai vu aussi", "SendMsg", "http://localhost:8001/base1/t01/model1/#message", "Bonjour Jacqueline");
//		Collection<Obsel> obsels = traceBuilder.getTrace().getObsels();
//		KtbsResponse response = client.addObselsToTrace("http://localhost:8001/base1/t01/", obsels);
		
//		Map<String, Serializable> attributes = new HashMap<String, Serializable>();
//		attributes.put("http://localhost:8001/base1/model1/message", "Recevez-vous ce message ?");
//		KtbsResponse response = client.createObsel(
//				"http://localhost:8001/base1/t01/", 
//				"observe5", 
//				"hervé", 
//				"Mon nième observé", 
//				"http://localhost:8001/base1/model1/SendMsg", 
//				new Date(), 
//				new Date(), 
//				attributes,
//				"http://localhost:8001/base1/model1/onChannel",
//				"http://localhost:8001/base1/t01/openchat2/"
//		);
//		attributes.put("http://localhost:8001/base1/model1/channel", "BBC 2");
//		KtbsResponse response = client.createObsel(
//				"http://localhost:8001/base1/t01/", 
//				"openchat2", 
//				"micheal", 
//				"Mon 3ème observé", 
//				"http://localhost:8001/base1/model1/OpenChat", 
//				new Date(), 
//				new Date(), 
//				attributes
//		);
		
		KtbsResponse response = client.deleteTrace("http://localhost:8001/base1/");
//		KtbsResponse response = client.deleteTrace("http://localhost:8001/base1/t01/8d375d5fd9edc11de41c07b9d19549c5");
//		KtbsResponse response = client.deleteTrace("http://localhost:8001/base1/t01/");

		System.out.println("###########################################################");
		System.out.println(response.toString());
		System.out.println("###########################################################");
		
		if(response.executedWithSuccess()) {
//			displayBase((Base) response.getBodyAsKtbsResource());
//			displayRoot((KtbsRoot) response.getBodyAsKtbsResource());
//			displayObsel((Obsel) response.getBodyAsKtbsResource());
//			displayTrace((Trace) response.getBodyAsKtbsResource());
		} 
		
		client.closeSession();
	}
	

	private static void displayBase(Base base) {
		System.out.println("********************************************************");
		System.out.println("** KTBS Base **");
		System.out.println("URI: " + base.getURI());
		System.out.println("Label: " + base.getLabel());
		for(String uri:base.getTraceURIs())
			System.out.println("\ttrace: "+ uri);
		for(String uri:base.getTraceModelURIs())
			System.out.println("\ttrace model: "+ uri);
		
	}

	private static void displayRoot(KtbsRoot root) {
		System.out.println("********************************************************");
		System.out.println("** KTBS Root **");
		System.out.println("URI: " + root.getURI());
		System.out.println("Label: " + root.getLabel());
		for(String uri:root.getBaseURIs())
			System.out.println("\tbase: " + uri);
		
	}

	private static void displayTrace(Trace trace) {
		System.out.println("********************************************************");
		System.out.println("** TRACE **");
		System.out.println("URI: " + trace.getURI());
		System.out.println("Label: " + trace.getLabel());
		System.out.println("Origin: " + trace.getOrigin());
		System.out.println("Trace Model URI: " + trace.getTraceModelUri());
		
		for(Obsel obsel:trace.getObsels()) {
			displayObsel(obsel);
		}
	}

	private static void displayObsel(Obsel obsel) {
		System.out.println("-------------------------------");
		System.out.println("** OBSEL **");
		System.out.println("URI: " + obsel.getURI());
		System.out.println("Type: " + obsel.getTypeURI());
		System.out.println("Label: " + obsel.getLabel());
		System.out.println("Begin: " + obsel.getBegin());
		System.out.println("End: " + obsel.getEnd());
		for(String att:obsel.getAttributes().keySet())
			System.out.println("\t#att# " + att + ":\t" + obsel.getAttributeValue(att));
		for(Relation rel:obsel.getOutgoingRelations())
			System.out.println("\t#rel# ---[" + rel.getRelationName() + "]--->  " + obsel.getTargetObsel(rel.getRelationName()));
	}

}
