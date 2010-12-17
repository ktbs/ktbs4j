package org.liris.ktbs.java.tests;


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
		
//		KtbsResponse response = client.getObsel("http://localhost:8001/base1/t01/81f0d55ab8df9aad28437e2eed7b8b4d");
//		KtbsResponse response = client.getTraceObsels("http://localhost:8001/base1/t01/");
//		KtbsResponse response = client.getTraceInfo("http://localhost:8001/base1/t01/");
//		KtbsResponse response = client.getKtbsRoot("http://localhost:8001/");
//		KtbsResponse response = client.getBase("http://localhost:8001/baseHector/");
//		KtbsResponse response = client.addObselsToTrace("http://localhost:8001/","t01");
//		KtbsResponse response = client.createBase("http://localhost:8001/","baseHector", "Une base n pour tester.");
//		KtbsResponse response = client.createTrace("http://localhost:8001/baseHector/", "traceHermes","http://localhost:8001/baseHector/modelRene/",new Date(), "Une trace pour tester.");
		KtbsResponse response = client.createTraceModel("http://localhost:8001/baseHector/", "modelRene","Super modèle qui dechire");
		
		System.out.println("###########################################################");
		System.out.println(response.toString());
		System.out.println("###########################################################");
		
		if(response.executedWithSuccess()) {
			displayBase((Base) response.getBodyAsKtbsResource());
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
