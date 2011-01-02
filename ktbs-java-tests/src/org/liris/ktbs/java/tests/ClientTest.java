package org.liris.ktbs.java.tests;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.client.KtbsClientApplication;
import org.liris.ktbs.client.KtbsResponse;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.impl.KtbsResourceFactory;

import com.ibm.icu.util.Calendar;

public class ClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KtbsClientApplication app = KtbsClientApplication.getInstance();

		KtbsClient client = app.getKtbsClient("http://localhost:8001/");
		client.startSession();

						KtbsResponse response = testPutTraceObsels(client);
		//				KtbsResponse response = testPutTraceInfo(client);
		//				KtbsResponse response = testCreateObsel(client);
		//		KtbsResponse response = client.getObsel("http://localhost:8001/base1/t01/0cd86043ba40d40aa91ad18bdd714634");
		//		KtbsResponse response = client.getTraceObsels("http://localhost:8001/base1/t01/");
		//				KtbsResponse response = client.getTraceInfo("http://localhost:8001/base1/t01/");
		//		KtbsResponse response = client.getKtbsRoot("http://localhost:8001/");
		//		KtbsResponse response = client.getBase("http://localhost:8001/","base1");
		//		KtbsResponse response = client.addObselsToTrace("http://localhost:8001/","t01");
		//		KtbsResponse response = client.createBase("http://localhost:8001/","baseHector2", "Une base n pour tester.");
		//		KtbsResponse response = client.createTrace("http://localhost:8001/baseHector2/", "traceHermes","http://localhost:8001/baseHector2/modelRene/",new Date(), "Une trace pour tester.");
		//		KtbsResponse response = client.createTraceModel("http://localhost:8001/baseHector2/", "modelRene","Super modèle qui dechire");

		//		for(KtbsResponse response:responses) {
		System.out.println("###########################################################");
		System.out.println(response.toString());
		System.out.println("###########################################################");
		//		}
		if(response.executedWithSuccess()) {
			//			displayBase((Base) response.getBodyAsKtbsResource());
			//			displayRoot((KtbsRoot) response.getBodyAsKtbsResource());
			//			displayObsel((Obsel) response.getBodyAsKtbsResource());
			//			displayTrace((Trace) response.getBodyAsKtbsResource());
		} 

		client.closeSession();
	}


	private static KtbsResponse testPutTraceInfo(KtbsClient client) {
		KtbsResponse response = client.getTraceInfo("http://localhost:8001/base1/t01/");
		Trace traceInfo = (Trace) response.getBodyAsKtbsResource();
		traceInfo.setLabel("Bonjour Nestor");
		return client.putTraceInfo(traceInfo, response.getHTTPETag());
	}


	private static KtbsResponse testCreateObsel(KtbsClient client) {

		Map<String, Serializable> attributes = new HashMap<String, Serializable>();
		attributes.put("http://localhost:8001/base1/model1/message", "Hello Girl");
		KtbsResponse response = client.createObsel(
				"http://localhost:8001/base1/t01/", 
				"observe6", 
				"dam", 
				"Mon 6ième observé", 
				"http://localhost:8001/base1/model1/SendMsg", 
				null, 
				null, 
				10000, 
				10000, 
				attributes
		);
		//		attributes.put("http://localhost:8001/base1/model1/channel", "BBC 2");
		//		KtbsResponse response = client.createObsel(
		//				"http://localhost:8001/base1/t01/", 
		//				"openchat2", 
		//				"micheal", 
		//				"Mon 3ème observé", 
		//				"http://localhost:8001/base1/model1/OpenChat", 
		//				new Date(), 
		//				new Date(), 
		//				-1,
		//				-1,
		//				attributes
		//		);
		return response;
	}


	private static KtbsResponse testPutTraceObsels(KtbsClient client) {
		Calendar c1 = Calendar.getInstance();
		KtbsResponse responseGet = client.getTraceObsels("http://localhost:8001/base1/t01/");
		Calendar c2 = Calendar.getInstance();
		Trace traceObsels = (Trace) responseGet.getBodyAsKtbsResource();
		String etag = responseGet.getHTTPETag();
		responseGet = client.getTraceInfo("http://localhost:8001/base1/t01/");
		Calendar c3 = Calendar.getInstance();

		Trace traceInfo = (Trace) responseGet.getBodyAsKtbsResource();

		int cnt = 0;
		for(Obsel obsel:traceObsels.getObsels()) {
			String messageAtt = "http://localhost:8001/base1/model1/channel";
			Serializable message = obsel.getAttributeValue(messageAtt);
			if(message!= null && cnt == 0) {
				cnt++;
				Map<String, Serializable> attributes  = new HashMap<String, Serializable>(obsel.getAttributes());
				attributes.remove(messageAtt);
				attributes.put(messageAtt, "#my-channel-modified23");


				Obsel newObsel = KtbsResourceFactory.createObsel(obsel.getURI(), obsel.getTraceURI(), "fion du bois", obsel.getBeginDT(), obsel.getEndDT(), 900, obsel.getEnd(), obsel.getTypeURI(), attributes, obsel.getLabel());
				newObsel.setLabel("Mon étiquette");
				traceInfo.addObsel(newObsel);
			} else {
				traceInfo.addObsel(obsel);
			}
		}

		KtbsResponse putResponse = client.putTraceObsels(traceInfo, etag);

		System.out.println("*********************************************************************************");
		System.out.println("*********************************************************************************");
		System.out.println("Première requête: " + (c2.getTimeInMillis() - c1.getTimeInMillis()));
		System.out.println("Deuxième requête: " + (c3.getTimeInMillis() - c2.getTimeInMillis()));
		System.out.println("*********************************************************************************");
		System.out.println("*********************************************************************************");
		return putResponse;
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
		System.out.println("Complies with model: " + trace.isCompliantWithModel());

		for(Obsel obsel:trace.getObsels()) {
			displayObsel(obsel);
		}
	}

	private static void displayObsel(Obsel obsel) {
		System.out.println("-------------------------------");
		System.out.println("** OBSEL **");
		System.out.println("URI: " + obsel.getURI());
		System.out.println("Type: " + obsel.getTypeURI());
		if(obsel.getLabel()!=null) System.out.println("Label: " + obsel.getLabel());
		if(obsel.getSubject()!=null) System.out.println("Subject: " + obsel.getSubject());
		System.out.println("Begin DT: " + obsel.getBeginDT());
		System.out.println("End DT: " + obsel.getEndDT());
		System.out.println("Begin: " + obsel.getBegin());
		System.out.println("End: " + obsel.getEnd());
		for(String att:obsel.getAttributes().keySet())
			System.out.println("\t#att# " + att + ":\t" + obsel.getAttributeValue(att));
		for(Relation rel:obsel.getOutgoingRelations())
			System.out.println("\t#rel# ---[" + rel.getRelationName() + "]--->  " + obsel.getTargetObsel(rel.getRelationName()));
	}

}
