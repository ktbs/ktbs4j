package org.liris.ktbs.rdf.tests;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.domain.KtbsResource;
import org.liris.ktbs.core.domain.Obsel;
import org.liris.ktbs.core.domain.PojoFactory;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.rdf.Java2Rdf;
import org.liris.ktbs.serial.SerializationConfig;
import org.liris.ktbs.serial.SerializationMode;
import org.liris.ktbs.tests.Examples;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Java2RdfTestCase extends TestCase {
	private Model model;
	private Java2Rdf mapper;
	private PojoFactory pojoFactory;
	
	private void writeModel() {
		model.write(System.out, KtbsConstants.JENA_TURTLE, "");
	}
	
	@Override
	protected void setUp() throws Exception {
		model = ModelFactory.createDefaultModel();
		mapper = new Java2Rdf(new SerializationConfig());
		pojoFactory = new PojoFactory();
		System.out.println("----------------------------------------------------------------------");
	}
	
	public void testSerializeRoot() {
		System.out.println("\tt01 root (default mode)");
		model = mapper.getModel(Examples.getRoot());
		writeModel();
	}
	
	public void testSerializeBase() {
		System.out.println("\tt01 base1 (default mode)");
		model = mapper.getModel(Examples.getBase1());
		writeModel();
	}

	public void testSerializeT01Info() {
		System.out.println("\tt01 info");
		model = mapper.getModel(Examples.getStoredTrace1());
		writeModel();
	}
	
	public void testSerializeT01InfoAndObsels() {
		System.out.println("\tt01 info and obsels");
		SerializationConfig config = new SerializationConfig();
		config.setChildMode(SerializationMode.CASCADE);
		mapper.setConfig(config);
		model = mapper.getModel(Examples.getStoredTrace1());
		writeModel();
	}

	public void testSerializeBaseWithChildrenTypes() {
		System.out.println("\tBase with children types");
		SerializationConfig config = new SerializationConfig();
		config.setChildMode(SerializationMode.URI_AND_TYPE);
		mapper.setConfig(config);
		model = mapper.getModel(Examples.getBase1());
		writeModel();
	}

	public void testSerializeTraceModel() {
		System.out.println("\tTrace Model");
		model = mapper.getModel(Examples.getTraceModel1());
		writeModel();
		
	}

	public void testSerializeTraceModelWithChildren() {
		System.out.println("\tTrace Model With Children");
		SerializationConfig config = new SerializationConfig();
		config.setChildMode(SerializationMode.CASCADE);
		mapper.setConfig(config);
		model = mapper.getModel(Examples.getTraceModel1());
		writeModel();
	}

	public void testSerializeAnonObsel() {
		System.out.println("\tAnon obsel");
		IObsel obsel = new Obsel();
		obsel.setObselType(pojoFactory.createResource("http://localhost:8001/base1/mdoel1/type", IObselType.class));
		obsel.setBegin(new BigInteger("1200"));
		obsel.setEndDT("La fin");
		((KtbsResource)obsel).setParentResource(pojoFactory.createResource("http://localhost:8001/base1/t01/", IStoredTrace.class));
		model = mapper.getModel(obsel);
		writeModel();
	}
	public void testSerializeObselCollection() {
		System.out.println("\tObsel collection");
		
		SerializationConfig config = new SerializationConfig();
		config.setLinkSameTypeMode(SerializationMode.CASCADE);
		mapper.setConfig(config);
		
		Set<IObsel> obsels = new HashSet<IObsel>();
		
		obsels.add(Examples.getObs1());
		obsels.add(Examples.getObs2());
		obsels.add(Examples.getObs3());
		obsels.add(Examples.getObs4());

		model = mapper.getModel(obsels);
		writeModel();
	}
}
