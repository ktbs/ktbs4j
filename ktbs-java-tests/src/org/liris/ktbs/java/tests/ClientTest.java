package org.liris.ktbs.java.tests;


import org.liris.ktbs.client.KTBSClientApplication;
import org.liris.ktbs.client.KtbsClient;
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
		
		Trace trace = client.getTrace("http://localhost:8001/", "base1", "t01");
		
		System.out.println(trace.getLabel());
		System.out.println(trace.getOrigin());
		System.out.println(trace.getTraceModelUri());
		
		for(Obsel obsel:trace.getObsels()) {
			System.out.println(obsel.getURI());
			System.out.println(obsel.getLabel());
			System.out.println(obsel.getBegin());
			System.out.println(obsel.getEnd());
			for(String att:obsel.getAttributes().keySet())
				System.out.println("\tatt: " + att + ":\t" + obsel.getAttributeValue(att));
			for(Relation rel:obsel.getOutgoingRelations())
				System.out.println("\t---- " + rel.getRelationName() + " ---->  " + obsel.getTargetObsel(rel.getRelationName()));
		}
		
//		System.out.println(base.getURI());
//		System.out.println(base.getLabel());
//		
//		for(String traceURI:base.getTraceURIs())
//			System.out.println("Trace: " + traceURI);
//		
//		for(String traceModelURI:base.getTraceModelURIs())
//			System.out.println("TraceModel: " + traceModelURI);
//		KtbsRoot root = KtbsResourceFactory.createKtbsRoot("http://localhost:8001/");
//		Base base = KtbsResourceFactory.createBase("http://localhost:8001/baseDamien/",root);
//		client.createBase("http://localhost:8001/", "baseDamien2");
		
		client.closeSession();
		
	}

}
