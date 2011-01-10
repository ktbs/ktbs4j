package org.liris.ktbs.rdf.resource.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaObselTypeTestCase {

	private TraceModel traceModel;

	private ObselType sendMsg;
	private ObselType abstractMsg;
	private ObselType recvMsg;
	private ObselType openChat;
	private ObselType closeChat;
	private ObselType channelEvent;
	
	@Before
	public void setUp() throws Exception {
		FileInputStream fis = new FileInputStream("turtle/model1.ttl");
		traceModel = KtbsJenaResourceFactory.getInstance().createTraceModel(
				"http://localhost:8001/base1/model1/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();
		
		sendMsg = traceModel.getObselType("http://localhost:8001/base1/model1/SendMsg");
		abstractMsg = traceModel.getObselType("http://localhost:8001/base1/model1/AbstractMsg");
		recvMsg = traceModel.getObselType("http://localhost:8001/base1/model1/RecvMsg");
		openChat = traceModel.getObselType("http://localhost:8001/base1/model1/OpenChat");
		closeChat = traceModel.getObselType("http://localhost:8001/base1/model1/CloseChat");
		channelEvent = traceModel.getObselType("http://localhost:8001/base1/model1/ChannelEvent");
	}

	@Test
	public void testGetTraceModel() {
		assertEquals(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/"), channelEvent.getTraceModel());
		assertEquals(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/"), abstractMsg.getTraceModel());
		assertEquals(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/"), sendMsg.getTraceModel());
		assertEquals(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/"), recvMsg.getTraceModel());
		assertEquals(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/"), openChat.getTraceModel());
		assertEquals(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/"), closeChat.getTraceModel());
	}

	@Test
	public void testListAttributes() {
		assertEquals(1, KtbsUtils.count(openChat.listAttributes()));
		AttributeType next = openChat.listAttributes().next();
		assertEquals(traceModel.getAttributeType("http://localhost:8001/base1/model1/channel"), next);
		assertEquals(openChat, next.getDomain());
		
		assertEquals(0, KtbsUtils.count(closeChat.listAttributes()));
		assertEquals(1, KtbsUtils.count(recvMsg.listAttributes()));
		assertEquals(traceModel.getAttributeType("http://localhost:8001/base1/model1/from"), recvMsg.listAttributes().next());
		assertEquals(recvMsg, recvMsg.listAttributes().next().getDomain());
		
		assertEquals(1, KtbsUtils.count(abstractMsg.listAttributes()));
		assertEquals(traceModel.getAttributeType("http://localhost:8001/base1/model1/message"), abstractMsg.listAttributes().next());
		assertEquals(abstractMsg, abstractMsg.listAttributes().next().getDomain());

		assertEquals(0, KtbsUtils.count(channelEvent.listAttributes()));
		assertEquals(0, KtbsUtils.count(sendMsg.listAttributes()));
	}

	@Test
	public void testListOutgoingRelations() {
		assertEquals(0, KtbsUtils.count(openChat.listOutgoingRelations()));
		assertEquals(1, KtbsUtils.count(closeChat.listOutgoingRelations()));

		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/closes"), closeChat.listOutgoingRelations().next());
		assertEquals(closeChat, closeChat.listOutgoingRelations().next().getDomain());

		assertEquals(0, KtbsUtils.count(recvMsg.listOutgoingRelations()));
		assertEquals(0, KtbsUtils.count(abstractMsg.listOutgoingRelations()));
		assertEquals(1, KtbsUtils.count(channelEvent.listOutgoingRelations()));
		
		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel"), channelEvent.listOutgoingRelations().next());
		assertEquals(channelEvent, channelEvent.listOutgoingRelations().next().getDomain());

		assertEquals(0, KtbsUtils.count(sendMsg.listOutgoingRelations()));
	}

	@Test
	public void testListIncomingRelations() {
		assertEquals(1, KtbsUtils.count(openChat.listIncomingRelations()));
		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel"), openChat.listIncomingRelations().next());
		assertEquals(openChat, openChat.listIncomingRelations().next().getRange());
		
		
		assertEquals(0, KtbsUtils.count(closeChat.listIncomingRelations()));
		assertEquals(0, KtbsUtils.count(recvMsg.listIncomingRelations()));
		assertEquals(0, KtbsUtils.count(abstractMsg.listIncomingRelations()));
		assertEquals(0, KtbsUtils.count(channelEvent.listIncomingRelations()));
		assertEquals(0, KtbsUtils.count(sendMsg.listIncomingRelations()));
	}

	@Test
	public void testGetSuperObselType() {
		assertEquals(channelEvent,closeChat.getSuperObselType());
		assertEquals(channelEvent,abstractMsg.getSuperObselType());
		assertEquals(abstractMsg,recvMsg.getSuperObselType());
		assertEquals(abstractMsg,sendMsg.getSuperObselType());

		assertNull(channelEvent.getSuperObselType());
		assertNull(openChat.getSuperObselType());
	}

}
