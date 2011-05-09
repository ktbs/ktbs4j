package org.liris.ktbs.service.tests;

import java.math.BigInteger;

import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.dao.DaoException;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.ResourceService;

public class ResourceManagerSaveTestCase extends TestCase {
	private ResourceService manager;
	private PojoFactory factory;
	private IRoot root;

	@Before
	public void setUp() throws Exception {
		manager = Ktbs.getRestRootClient().getResourceService();
		root = manager.getResource("http://localhost:8001/", IRoot.class);
		factory = new PojoFactory();
	}

	public void testSaveTraceModel() {
		IBase base1 = (IBase)root.get("base1");
		ITraceModel model;
		try {
			String modelUri = manager.newTraceModel(base1.getUri(), "monModel");
			model = manager.getTraceModel(modelUri);
		} catch(DaoException e) {
			model = manager.getResource(base1.getUri()+"monModel/", ITraceModel.class);
		}


		IObselType abstractMsg = factory.createResource(model.getUri()+"AbstractMsg", IObselType.class);
		model.getObselTypes().add(abstractMsg);

		IObselType sendMsg = factory.createResource(model.getUri()+"SendMsg", IObselType.class);
		model.getObselTypes().add(sendMsg);

		sendMsg.getSuperObselTypes().add(abstractMsg);
		IObselType openChat = factory.createResource(model.getUri()+"OpenChat", IObselType.class);
		model.getObselTypes().add(openChat);


		IAttributeType message = factory.createResource(model.getUri()+"message", IAttributeType.class);
		message.getDomains().add(abstractMsg);
		model.getAttributeTypes().add(message);

		IAttributeType to = factory.createResource(model.getUri()+"to", IAttributeType.class);
		to.getDomains().add(sendMsg);
		model.getAttributeTypes().add(to);

		IRelationType relType = factory.createResource(model.getUri()+"onChannel", IRelationType.class);

		relType.getDomains().add(abstractMsg);
		relType.getRanges().add(openChat);
		model.getRelationTypes().add(relType);

		manager.saveResource(model);
	}

	public void testSaveAttributeType() {

	}

	public void testSaveStoredTrace() {
		IStoredTrace trace = (IStoredTrace)root.get("base1").get("t01");
		trace.addLabel("Label ajouté");

		// should not be saved since it was not changed
		assertTrue(manager.saveResource(trace));
	}

	public void testSaveObsel() {
		IStoredTrace iStoredTrace = (IStoredTrace)root.get("base1").get("t01");
		IObsel o = iStoredTrace.get("obs1");
		o.setBegin(new BigInteger("1001"));
		o.setEnd(new BigInteger("1002"));
		assertTrue(manager.saveResource(iStoredTrace, true));
	}
}
