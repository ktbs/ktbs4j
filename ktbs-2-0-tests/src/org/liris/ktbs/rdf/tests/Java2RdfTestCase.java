package org.liris.ktbs.rdf.tests;

import java.lang.reflect.Field;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ProxyFactory;
import org.liris.ktbs.core.domain.PojoFactory;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IRoot;
import org.liris.ktbs.rdf.Java2Rdf;
import org.liris.ktbs.rdf.Rdf2Java;
import org.liris.ktbs.serial.SerializationConfig;
import org.liris.ktbs.serial.SerializationMode;
import org.liris.ktbs.tests.Examples;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import junit.framework.TestCase;

public class Java2RdfTestCase extends TestCase {
	private Model model;
	private Java2Rdf mapper;
	
	
	private void createMapper(IKtbsResource resource) {
		mapper = new Java2Rdf(resource);
	}

	private void writeModel() {
		model.write(System.out, KtbsConstants.JENA_TURTLE, "");
	}
	
	@Override
	protected void setUp() throws Exception {
		model = ModelFactory.createDefaultModel();
		System.out.println("----------------------------------------------------------------------");
	}
	
	public void testSerializeRoot() {
		createMapper(Examples.getRoot());
		model = mapper.getModel();
		writeModel();
		
	}
	
	public void testSerializeBase() {
		createMapper(Examples.getBase1());
		model = mapper.getModel();
		writeModel();
	}

	public void testSerializeBaseWithChildrenTypes() {
		createMapper(Examples.getBase1());
		SerializationConfig config = new SerializationConfig();
		config.setChildMode(SerializationMode.URI_AND_TYPE);
		mapper.setConfig(config);
		model = mapper.getModel();
		writeModel();
	}

	public void testSerializeTraceModel() {
		createMapper(Examples.getTraceModel1());
		model = mapper.getModel();
		writeModel();
		
	}

	public void testSerializeTraceModelWithChildren() {
		createMapper(Examples.getTraceModel1());
		SerializationConfig config = new SerializationConfig();
		config.setChildMode(SerializationMode.CASCADE);
		mapper.setConfig(config);
		model = mapper.getModel();
		writeModel();
		
	}
}
