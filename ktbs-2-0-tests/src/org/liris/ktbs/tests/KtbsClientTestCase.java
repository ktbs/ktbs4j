package org.liris.ktbs.tests;


import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.core.KtbsClient;
import org.liris.ktbs.core.ResourceManager;
import org.liris.ktbs.core.domain.interfaces.IAttributePair;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IComputedTrace;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IMethod;
import org.liris.ktbs.core.domain.interfaces.IMethodParameter;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IPropertyStatement;
import org.liris.ktbs.core.domain.interfaces.IRelationStatement;
import org.liris.ktbs.core.domain.interfaces.IRoot;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITrace;
import org.liris.ktbs.core.domain.interfaces.WithParameters;

public class KtbsClientTestCase extends TestCase {

	private ResourceManager manager;
	
	@Before
	public void setUp() throws Exception {
		try {
			manager = KtbsClient.getDefaultRestManager();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetRoot() {

		System.out.println("Getting the root");
		IRoot root = manager.getKtbsResource("http://localhost:8001/", IRoot.class);
		IBase base1 = root.get("base1");

		for(IMethod method: base1.getMethods()) {
			delim();
			displayMethod(method);
		}
		
		IStoredTrace trace1 = (IStoredTrace) base1.get("t01");
		
		delim();
		displayStoredTrace(trace1);
	}

	private void displayStoredTrace(IStoredTrace trace1) {
		displayResource(trace1);
		displayTrace(trace1);
		
	}

	private void displayTrace(ITrace trace1) {
		display("Origin",trace1.getOrigin());
		display("Trace model",trace1.getTraceModel().getUri());
		display("Complies with model",trace1.getCompliesWithModel());
		System.out.println("Transformed traces");
		for(IComputedTrace ct:trace1.getTransformedTraces()) 
			System.out.println("\t - " + ct.getUri());
		
		System.out.println("Obsels:");
		for(IObsel obsel:trace1.getObsels()) {
			System.out.println("*******");
			displayObsel(obsel);
		}
	}

	private void displayObsel(IObsel obsel) {
		displayResource(obsel);
		display("beginDT", obsel.getBeginDT());
		display("endDT", obsel.getEndDT());
		display("begin", obsel.getBegin());
		display("end", obsel.getEnd());
		display("subject", obsel.getSubject());
		System.out.println("Attributes");
		for(IAttributePair pair:obsel.getAttributePairs()) {
			display(pair);
		}
		System.out.println("Outgoing relations");
		for(IRelationStatement rel:obsel.getOutgoingRelations()) {
			display(rel);
		}
		System.out.println("Source obsels");
		for(IObsel o:obsel.getSourceObsels()) {
			System.out.println("- " + o.getUri());
		}
	}

	private void display(IRelationStatement rel) {
		System.out.println("\t" + rel.getFromObsel().getLocalName() + " --- " + rel.getRelation().getLocalName() + " ---> " + rel.getToObsel().getLocalName());
		
	}

	private void display(IAttributePair pair) {
		System.out.println("\t" + pair.getAttributeType().getLocalName() + ":\t " + pair.getValue());
		
	}

	private void delim() {
		System.out.println("-------------------------------------------------------------------");
	}

	private void displayMethod(IMethod method) {
		displayResource(method);
		display("Etag", method.getEtag());
		display("Inherits", method.getInherits());
		displayParameters(method);
	}
	
	private void displayParameters(WithParameters r) {
		System.out.println("Method parameters: ");
		for(IMethodParameter param:r.getMethodParameters()) {
			System.out.println("\t - "+param.getName()+":\t " + param.getValue());
		}
		
	}

	private void display(String prop, Object value) {
		System.out.println(prop+":\t " + value);		
	}

	private void displayResource(IKtbsResource r) {

		System.out.println(r.getUri() + "\t (" + r.getTypeUri() + ")");
		System.out.println(" - local name: " + r.getLocalName());
		String labels = "";
		boolean first = true;
		for(String label:r.getLabels()) {
			labels+=first?label:(", " +label);
			first = false;
		}
		
		System.out.println("Labels: " + labels);
		System.out.println("Properties: ");
		for(IPropertyStatement stmt:r.getProperties()) {
			System.out.println("\t " + stmt.getProperty() +":\t " + stmt.getValue());
		}

	}
}
