package org.liris.ktbs.rdf.resource.test;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.Mode;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaObselTypeTestCase extends AbstractKtbsJenaTestCase {

	private TraceModel traceModel;

	private ObselType sendMsg;
	private ObselType abstractMsg;
	private ObselType recvMsg;
	private ObselType openChat;
	private ObselType closeChat;
	private ObselType channelEvent;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		traceModel = loadInRepo(
				model1, 
				"model1.ttl", 
				TraceModel.class);
		
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
		assertEquals(1, KtbsUtils.count(openChat.listAttributes(Mode.ASSERTED)));
		AttributeType next = openChat.listAttributes(Mode.ASSERTED).next();
		assertEquals(traceModel.getAttributeType("http://localhost:8001/base1/model1/channel"), next);
		assertEquals(openChat, next.getDomain());
		
		assertEquals(0, KtbsUtils.count(closeChat.listAttributes(Mode.ASSERTED)));
		assertEquals(1, KtbsUtils.count(recvMsg.listAttributes(Mode.ASSERTED)));
		assertEquals(traceModel.getAttributeType("http://localhost:8001/base1/model1/from"), recvMsg.listAttributes(Mode.ASSERTED).next());
		assertEquals(recvMsg, recvMsg.listAttributes(Mode.ASSERTED).next().getDomain());
		
		assertEquals(1, KtbsUtils.count(abstractMsg.listAttributes(Mode.ASSERTED)));
		assertEquals(traceModel.getAttributeType("http://localhost:8001/base1/model1/message"), abstractMsg.listAttributes(Mode.ASSERTED).next());
		assertEquals(abstractMsg, abstractMsg.listAttributes(Mode.ASSERTED).next().getDomain());

		assertEquals(0, KtbsUtils.count(channelEvent.listAttributes(Mode.ASSERTED)));
		assertEquals(0, KtbsUtils.count(sendMsg.listAttributes(Mode.ASSERTED)));

		// open chant has no super obsel type
		assertEquals(1, KtbsUtils.count(openChat.listAttributes(Mode.INFERRED)));
		next = openChat.listAttributes(Mode.INFERRED).next();
		assertEquals(traceModel.getAttributeType("http://localhost:8001/base1/model1/channel"), next);
		assertEquals(openChat, next.getDomain());
		
		assertEquals(0, KtbsUtils.count(closeChat.listAttributes(Mode.INFERRED)));
		assertEquals(2, KtbsUtils.count(recvMsg.listAttributes(Mode.INFERRED)));
		Collection<AttributeType> c = KtbsUtils.toLinkedList(recvMsg.listAttributes(Mode.INFERRED));
		assertTrue(c.contains(traceModel.getAttributeType("http://localhost:8001/base1/model1/from")));
		assertTrue(c.contains(traceModel.getAttributeType("http://localhost:8001/base1/model1/message")));
		
		assertEquals(1, KtbsUtils.count(abstractMsg.listAttributes(Mode.INFERRED)));
		assertEquals(traceModel.getAttributeType("http://localhost:8001/base1/model1/message"), abstractMsg.listAttributes(Mode.INFERRED).next());
		assertEquals(abstractMsg, abstractMsg.listAttributes(Mode.INFERRED).next().getDomain());
		
		assertEquals(0, KtbsUtils.count(channelEvent.listAttributes(Mode.INFERRED)));
		assertEquals(1, KtbsUtils.count(sendMsg.listAttributes(Mode.INFERRED)));
		assertEquals(repository.getResource("http://localhost:8001/base1/model1/message", AttributeType.class), sendMsg.listAttributes(Mode.INFERRED).next());
		
	}

	@Test
	public void testListOutgoingRelations() {
		assertEquals(0, KtbsUtils.count(openChat.listOutgoingRelations(Mode.ASSERTED)));
		assertEquals(1, KtbsUtils.count(closeChat.listOutgoingRelations(Mode.ASSERTED)));

		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/closes"), closeChat.listOutgoingRelations(Mode.ASSERTED).next());
		assertEquals(closeChat, closeChat.listOutgoingRelations(Mode.ASSERTED).next().getDomain());

		assertEquals(0, KtbsUtils.count(recvMsg.listOutgoingRelations(Mode.ASSERTED)));
		assertEquals(0, KtbsUtils.count(abstractMsg.listOutgoingRelations(Mode.ASSERTED)));
		assertEquals(1, KtbsUtils.count(channelEvent.listOutgoingRelations(Mode.ASSERTED)));
		
		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel"), channelEvent.listOutgoingRelations(Mode.ASSERTED).next());
		assertEquals(channelEvent, channelEvent.listOutgoingRelations(Mode.ASSERTED).next().getDomain());

		assertEquals(0, KtbsUtils.count(sendMsg.listOutgoingRelations(Mode.ASSERTED)));

		
		
		assertEquals(0, KtbsUtils.count(openChat.listOutgoingRelations(Mode.INFERRED)));
		assertEquals(2, KtbsUtils.count(closeChat.listOutgoingRelations(Mode.INFERRED)));
		
		assertTrue(KtbsUtils.toLinkedList(closeChat.listOutgoingRelations(Mode.INFERRED)).contains(traceModel.getRelationType("http://localhost:8001/base1/model1/closes")));
		assertTrue(KtbsUtils.toLinkedList(closeChat.listOutgoingRelations(Mode.INFERRED)).contains(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel")));
		
		assertEquals(1, KtbsUtils.count(recvMsg.listOutgoingRelations(Mode.INFERRED)));
		assertEquals(1, KtbsUtils.count(abstractMsg.listOutgoingRelations(Mode.INFERRED)));
		assertEquals(1, KtbsUtils.count(channelEvent.listOutgoingRelations(Mode.INFERRED)));
		assertEquals(1, KtbsUtils.count(sendMsg.listOutgoingRelations(Mode.INFERRED)));
		
		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel"), channelEvent.listOutgoingRelations(Mode.INFERRED).next());
		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel"), recvMsg.listOutgoingRelations(Mode.INFERRED).next());
		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel"), abstractMsg.listOutgoingRelations(Mode.INFERRED).next());
		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel"), sendMsg.listOutgoingRelations(Mode.INFERRED).next());
	}

	@Test
	public void testHasSuperObselType() {
		assertFalse(openChat.hasSuperType(abstractMsg, Mode.ASSERTED));
		assertFalse(openChat.hasSuperType(channelEvent, Mode.ASSERTED));
		assertFalse(openChat.hasSuperType(abstractMsg, Mode.INFERRED));
		assertFalse(openChat.hasSuperType(channelEvent, Mode.INFERRED));

		assertFalse(closeChat.hasSuperType(abstractMsg, Mode.ASSERTED));
		assertTrue(closeChat.hasSuperType(channelEvent, Mode.ASSERTED));
		assertFalse(closeChat.hasSuperType(abstractMsg, Mode.INFERRED));
		assertTrue(closeChat.hasSuperType(channelEvent, Mode.INFERRED));

		assertTrue(abstractMsg.hasSuperType(channelEvent, Mode.ASSERTED));
		assertTrue(abstractMsg.hasSuperType(channelEvent, Mode.INFERRED));

		assertTrue(recvMsg.hasSuperType(abstractMsg, Mode.ASSERTED));
		assertTrue(sendMsg.hasSuperType(abstractMsg, Mode.ASSERTED));
		assertFalse(recvMsg.hasSuperType(channelEvent, Mode.ASSERTED));
		assertFalse(sendMsg.hasSuperType(channelEvent, Mode.ASSERTED));
		assertTrue(recvMsg.hasSuperType(abstractMsg, Mode.INFERRED));
		assertTrue(sendMsg.hasSuperType(abstractMsg, Mode.INFERRED));
		assertTrue(recvMsg.hasSuperType(channelEvent, Mode.INFERRED));
		assertTrue(sendMsg.hasSuperType(channelEvent, Mode.INFERRED));
	}
	
	@Test
	public void testListIncomingRelations() {
		assertEquals(1, KtbsUtils.count(openChat.listIncomingRelations(Mode.ASSERTED)));
		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel"), openChat.listIncomingRelations(Mode.ASSERTED).next());
		assertEquals(openChat, openChat.listIncomingRelations(Mode.ASSERTED).next().getRange());
		
		assertEquals(0, KtbsUtils.count(closeChat.listIncomingRelations(Mode.ASSERTED)));
		assertEquals(0, KtbsUtils.count(recvMsg.listIncomingRelations(Mode.ASSERTED)));
		assertEquals(0, KtbsUtils.count(abstractMsg.listIncomingRelations(Mode.ASSERTED)));
		assertEquals(0, KtbsUtils.count(channelEvent.listIncomingRelations(Mode.ASSERTED)));
		assertEquals(0, KtbsUtils.count(sendMsg.listIncomingRelations(Mode.ASSERTED)));

		assertEquals(1, KtbsUtils.count(openChat.listIncomingRelations(Mode.ASSERTED)));
		assertEquals(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel"), openChat.listIncomingRelations(Mode.ASSERTED).next());
		assertEquals(openChat, openChat.listIncomingRelations(Mode.ASSERTED).next().getRange());
		
		assertEquals(0, KtbsUtils.count(closeChat.listIncomingRelations(Mode.ASSERTED)));
		assertEquals(0, KtbsUtils.count(recvMsg.listIncomingRelations(Mode.ASSERTED)));
		assertEquals(0, KtbsUtils.count(abstractMsg.listIncomingRelations(Mode.ASSERTED)));
		assertEquals(0, KtbsUtils.count(channelEvent.listIncomingRelations(Mode.ASSERTED)));
		assertEquals(0, KtbsUtils.count(sendMsg.listIncomingRelations(Mode.ASSERTED)));
	}

	@Test
	public void testSetSuperObselType() {
		
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
