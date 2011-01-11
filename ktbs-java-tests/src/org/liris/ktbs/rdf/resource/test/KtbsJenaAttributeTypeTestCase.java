package org.liris.ktbs.rdf.resource.test;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.TraceModel;

public class KtbsJenaAttributeTypeTestCase extends AbstractKtbsJenaTestCase {

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
		traceModel = loadInHolder(
				"base1/model1/", 
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
	public void testSetDomain() {
		assertEquals(rcvMsg, from.getDomain());
		from.setDomain(channelEvent);
		assertEquals(channelEvent, from.getDomain());
	}

}
