package org.liris.ktbs.rdf.resource.test;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

public class RdfAttributeTypeTestCase extends AbstractKtbsRdfResourceTestCase {

	private TraceModel traceModel;
	
	private AttributeType from;
	private AttributeType message;
	private AttributeType channel;
	
	private ObselType rcvMsg;
	private ObselType abstractMsg;
	private ObselType openChat;
	private ObselType channelEvent;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		traceModel = loadInRepo(
				model1,
				"model1.ttl", 
				TraceModel.class);
		abstractMsg = traceModel.getObselType("http://localhost:8001/base1/model1/AbstractMsg");
		openChat = traceModel.getObselType("http://localhost:8001/base1/model1/OpenChat");
		channelEvent = traceModel.getObselType("http://localhost:8001/base1/model1/ChannelEvent");
		rcvMsg = traceModel.getObselType("http://localhost:8001/base1/model1/RecvMsg");
		
		from = traceModel.getAttributeType("http://localhost:8001/base1/model1/from");
		message = traceModel.getAttributeType("http://localhost:8001/base1/model1/message");
		channel = traceModel.getAttributeType("http://localhost:8001/base1/model1/channel");
	}

	@Test
	public void testGetDomain() {
		assertEquals(rcvMsg, from.getDomain());
		assertEquals(openChat, channel.getDomain());
		assertEquals(abstractMsg, message.getDomain());
	}
	
	@Test
	public void testListDomains() {
		assertEquals(1, KtbsUtils.count(from.listDomains()));
		assertTrue(KtbsUtils.toLinkedList(from.listDomains()).contains(rcvMsg));
		from.addDomain(channelEvent);
		assertEquals(2, KtbsUtils.count(from.listDomains()));
		assertTrue(KtbsUtils.toLinkedList(from.listDomains()).contains(rcvMsg));
		assertTrue(KtbsUtils.toLinkedList(from.listDomains()).contains(channelEvent));
	}
	
	@Test
	public void testAddDomain() {
		assertEquals(1, KtbsUtils.count(from.listDomains()));
		assertTrue(KtbsUtils.toLinkedList(from.listDomains()).contains(rcvMsg));
		from.addDomain(channelEvent);
		assertEquals(2, KtbsUtils.count(from.listDomains()));
		assertTrue(KtbsUtils.toLinkedList(from.listDomains()).contains(rcvMsg));
		assertTrue(KtbsUtils.toLinkedList(from.listDomains()).contains(channelEvent));
	}
}
