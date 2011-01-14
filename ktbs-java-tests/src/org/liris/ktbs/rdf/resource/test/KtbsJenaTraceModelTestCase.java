package org.liris.ktbs.rdf.resource.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaTraceModelTestCase extends AbstractKtbsJenaTestCase {

	private TraceModel traceModel;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		traceModel = loadInRepo(
				"model1.ttl", 
				TraceModel.class);
	}

	@Test
	public void testGet() {
		AttributeType channel = (AttributeType) traceModel.get("http://localhost:8001/base1/model1/channel");
		AttributeType from = (AttributeType) traceModel.get("http://localhost:8001/base1/model1/from");
		AttributeType message = (AttributeType) traceModel.get("http://localhost:8001/base1/model1/message");
		RelationType closes = (RelationType) traceModel.get("http://localhost:8001/base1/model1/closes");
		RelationType onChannel = (RelationType) traceModel.get("http://localhost:8001/base1/model1/onChannel");
		ObselType sendMsg = (ObselType) traceModel.get("http://localhost:8001/base1/model1/SendMsg");
		ObselType abstractMsg = (ObselType) traceModel.get("http://localhost:8001/base1/model1/AbstractMsg");
		ObselType recvMsg = (ObselType) traceModel.get("http://localhost:8001/base1/model1/RecvMsg");
		ObselType openChat = (ObselType) traceModel.get("http://localhost:8001/base1/model1/OpenChat");
		ObselType closeChat = (ObselType) traceModel.get("http://localhost:8001/base1/model1/CloseChat");
		ObselType channelEvent = (ObselType) traceModel.get("http://localhost:8001/base1/model1/ChannelEvent");
		

		assertEquals(channel, EmptyResourceFactory.getInstance().createRelationType("http://localhost:8001/base1/model1/channel"));
		assertEquals(from, EmptyResourceFactory.getInstance().createRelationType("http://localhost:8001/base1/model1/from"));
		assertEquals(message, EmptyResourceFactory.getInstance().createRelationType("http://localhost:8001/base1/model1/message"));
		assertEquals(sendMsg, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/SendMsg"));
		assertEquals(abstractMsg, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/AbstractMsg"));
		assertEquals(recvMsg, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/RecvMsg"));
		assertEquals(openChat, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/OpenChat"));
		assertEquals(closeChat, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/CloseChat"));
		assertEquals(channelEvent, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/ChannelEvent"));
		assertEquals(closes, EmptyResourceFactory.getInstance().createRelationType("http://localhost:8001/base1/model1/closes"));
		assertEquals(onChannel, EmptyResourceFactory.getInstance().createRelationType("http://localhost:8001/base1/model1/onChannel"));

		
		assertNull(traceModel.get("http://localhost:8001/base1/model1/closes/"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/onChannel/"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/SendMsg/"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/AbstractMsg/"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/RecvMsg/"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/OpenChat/"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/CloseChat/"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/ChannelEvent/"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/bidon"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/channel/"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/from/"));
		assertNull(traceModel.get("http://localhost:8001/base1/model1/message/"));
	}

	@Test
	public void testListAttributeTypes() {
		assertEquals(3, KtbsUtils.count(traceModel.listAttributeTypes()));
		Set<AttributeType> c = new HashSet<AttributeType>(KtbsUtils.toLinkedList(traceModel.listAttributeTypes()));
		assertEquals(3, c.size());
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/channel")));
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/from")));
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/message")));
		
	}

	@Test
	public void testListRelationTypes() {
		assertEquals(2, KtbsUtils.count(traceModel.listRelationTypes()));
		Set<RelationType> c = new HashSet<RelationType>(KtbsUtils.toLinkedList(traceModel.listRelationTypes()));
		assertEquals(2, c.size());
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/onChannel")));
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/closes")));
	}

	@Test
	public void testListObselTypes() {
		assertEquals(6, KtbsUtils.count(traceModel.listObselTypes()));
		Set<ObselType> c = new HashSet<ObselType>(KtbsUtils.toLinkedList(traceModel.listObselTypes()));
		assertEquals(6, c.size());
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/SendMsg")));
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/RecvMsg")));
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/AbstractMsg")));
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/ChannelEvent")));
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/OpenChat")));
		assertTrue(c.contains(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/model1/CloseChat")));
	}

	@Test
	public void testGetObselType() {
		ObselType sendMsg = traceModel.getObselType("http://localhost:8001/base1/model1/SendMsg");
		ObselType abstractMsg = traceModel.getObselType("http://localhost:8001/base1/model1/AbstractMsg");
		ObselType recvMsg = traceModel.getObselType("http://localhost:8001/base1/model1/RecvMsg");
		ObselType openChat = traceModel.getObselType("http://localhost:8001/base1/model1/OpenChat");
		ObselType closeChat = traceModel.getObselType("http://localhost:8001/base1/model1/CloseChat");
		ObselType channelEvent = traceModel.getObselType("http://localhost:8001/base1/model1/ChannelEvent");
		
		assertEquals(sendMsg, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/SendMsg"));
		assertEquals(abstractMsg, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/AbstractMsg"));
		assertEquals(recvMsg, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/RecvMsg"));
		assertEquals(openChat, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/OpenChat"));
		assertEquals(closeChat, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/CloseChat"));
		assertEquals(channelEvent, EmptyResourceFactory.getInstance().createObselType("http://localhost:8001/base1/model1/ChannelEvent"));

		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/SendMsg/"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/AbstractMsg/"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/RecvMsg/"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/OpenChat/"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/CloseChat/"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/ChannelEvent/"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/bidon"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/closes"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/channel"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/from"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/message"));
		assertNull(traceModel.getObselType("http://localhost:8001/base1/model1/onChannel"));
	}

	@Test
	public void testGetRelationType() {
		RelationType closes = traceModel.getRelationType("http://localhost:8001/base1/model1/closes");
		RelationType onChannel = traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel");
		
		assertEquals(closes, EmptyResourceFactory.getInstance().createRelationType("http://localhost:8001/base1/model1/closes"));
		assertEquals(onChannel, EmptyResourceFactory.getInstance().createRelationType("http://localhost:8001/base1/model1/onChannel"));

		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/closes/"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel/"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/SendMsg"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/AbstractMsg"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/RecvMsg"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/OpenChat"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/CloseChat"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/ChannelEvent"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/bidon"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/channel"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/from"));
		assertNull(traceModel.getRelationType("http://localhost:8001/base1/model1/message"));
	}

	@Test
	public void testNewAttributeType() {
		assertEquals(3, KtbsUtils.count(traceModel.listAttributeTypes()));
		ObselType sendMsg = traceModel.getObselType("http://localhost:8001/base1/model1/SendMsg");
		ObselType recvMsg = traceModel.getObselType("http://localhost:8001/base1/model1/RecvMsg");
		AttributeType att1 = traceModel.newAttributeType("attribut-perso1", sendMsg);
		AttributeType att2 = traceModel.newAttributeType("attribut-perso2", recvMsg);
		
		assertEquals(5, KtbsUtils.count(traceModel.listAttributeTypes()));
		assertTrue(KtbsUtils.toLinkedList(traceModel.listAttributeTypes()).contains(att1));
		assertTrue(KtbsUtils.toLinkedList(traceModel.listAttributeTypes()).contains(att2));

		assertEquals(sendMsg, att1.getDomain());
		assertEquals(recvMsg, att2.getDomain());
	}
	
	
	@Test
	public void testNewObselType() {
		assertEquals(6, KtbsUtils.count(traceModel.listObselTypes()));
		ObselType type = traceModel.newObselType("type-toto");
		assertEquals(7, KtbsUtils.count(traceModel.listObselTypes()));
		assertTrue(KtbsUtils.toLinkedList(traceModel.listObselTypes()).contains(type));
	}
	
	
	@Test
	public void testNewRelationType() {
		assertEquals(2, KtbsUtils.count(traceModel.listRelationTypes()));
		ObselType sendMsg = traceModel.getObselType("http://localhost:8001/base1/model1/SendMsg");
		ObselType recvMsg = traceModel.getObselType("http://localhost:8001/base1/model1/RecvMsg");
		RelationType rel = traceModel.newRelationType("relation-perso1", sendMsg, recvMsg);
		
		assertEquals(3, KtbsUtils.count(traceModel.listRelationTypes()));
		assertTrue(KtbsUtils.toLinkedList(traceModel.listRelationTypes()).contains(rel));
		
		assertEquals(sendMsg, rel.getDomain());
		assertEquals(recvMsg, rel.getRange());
	}
	
	@Test
	public void testGetAttributeType() {
		AttributeType channel = traceModel.getAttributeType("http://localhost:8001/base1/model1/channel");
		AttributeType from = traceModel.getAttributeType("http://localhost:8001/base1/model1/from");
		AttributeType message = traceModel.getAttributeType("http://localhost:8001/base1/model1/message");
		
		assertEquals(channel, EmptyResourceFactory.getInstance().createRelationType("http://localhost:8001/base1/model1/channel"));
		assertEquals(from, EmptyResourceFactory.getInstance().createRelationType("http://localhost:8001/base1/model1/from"));
		assertEquals(message, EmptyResourceFactory.getInstance().createRelationType("http://localhost:8001/base1/model1/message"));

		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/closes"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/onChannel"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/SendMsg"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/AbstractMsg"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/RecvMsg"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/OpenChat"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/CloseChat"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/ChannelEvent"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/bidon"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/channel/"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/from/"));
		assertNull(traceModel.getAttributeType("http://localhost:8001/base1/model1/message/"));
	}
}
