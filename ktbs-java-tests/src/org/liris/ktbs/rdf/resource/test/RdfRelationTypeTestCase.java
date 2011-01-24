package org.liris.ktbs.rdf.resource.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

public class RdfRelationTypeTestCase extends AbstractKtbsRdfResourceTestCase {

	private TraceModel traceModel;
	private RelationType onChannel;
	private RelationType closes;
	private RelationType toto;
	private RelationType tata;

	private ObselType abstractMsg;
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

		abstractMsg = traceModel.getObselType("http://localhost:8001/base1/model1/AbstractMsg");
		openChat = traceModel.getObselType("http://localhost:8001/base1/model1/OpenChat");
		closeChat = traceModel.getObselType("http://localhost:8001/base1/model1/CloseChat");
		channelEvent = traceModel.getObselType("http://localhost:8001/base1/model1/ChannelEvent");

		onChannel = traceModel.getRelationType("http://localhost:8001/base1/model1/onChannel");
		closes = traceModel.getRelationType("http://localhost:8001/base1/model1/closes");
		toto = traceModel.newRelationType("toto", abstractMsg, channelEvent);
		tata = traceModel.newRelationType("tata", openChat, closeChat);
	}


	@Test
	public void testGetDomainInferred() {
		Collection<ObselType> domains = Arrays.asList(closes.getDomainsInferred());
		assertEquals(2, domains.size());
		assertTrue(domains.contains(closeChat));
		assertTrue(domains.contains(channelEvent));

		domains = Arrays.asList(onChannel.getDomainsInferred());
		assertEquals(1, domains.size());
		assertTrue(domains.contains(channelEvent));
	}

	@Test
	public void testGetRangeInferred() {
		Collection<ObselType> ranges = Arrays.asList(closes.getRangesInferred());
		assertEquals(1, ranges.size());
		assertTrue(ranges.contains(openChat));

		ranges = Arrays.asList(onChannel.getRangesInferred());
		assertEquals(1, ranges.size());
		assertTrue(ranges.contains(openChat));
	}

	@Test
	public void testListDomains() {
		assertEquals(closeChat, closes.getDomain());
		closes.addDomain(openChat);
		closes.addDomain(abstractMsg);
		LinkedList<ObselType> linkedList = KtbsUtils.toLinkedList(closes.listDomains());
		assertEquals(3, linkedList.size());
		assertTrue(linkedList.contains(closeChat));
		assertTrue(linkedList.contains(openChat));
		assertTrue(linkedList.contains(abstractMsg));
	}

	@Test
	public void testListRanges() {
		LinkedList<ObselType> linkedList = KtbsUtils.toLinkedList(onChannel.listRanges());
		assertEquals(1, linkedList.size());
		assertEquals(openChat, onChannel.getRange());
		onChannel.addRange(abstractMsg);
		linkedList = KtbsUtils.toLinkedList(onChannel.listRanges());
		assertEquals(2, linkedList.size());
		assertTrue(linkedList.contains(abstractMsg));
	}

	@Test
	public void testGetDomain() {
		assertEquals(closeChat, closes.getDomain());
		assertEquals(channelEvent, onChannel.getDomain());
	}

	@Test
	public void testGetRange() {
		assertEquals(null, closes.getRange());
		assertEquals(openChat, onChannel.getRange());
	}

	@Test
	public void testGetSuperRelationType() {
		assertEquals(onChannel, closes.getSuperRelationType());
	}

	@Test
	public void testAddDomain() {
		assertEquals(closeChat, closes.getDomain());
		closes.addDomain(openChat);
		closes.addDomain(abstractMsg);
		LinkedList<ObselType> linkedList = KtbsUtils.toLinkedList(closes.listDomains());
		assertEquals(3, linkedList.size());
		assertTrue(linkedList.contains(closeChat));
		assertTrue(linkedList.contains(openChat));
		assertTrue(linkedList.contains(abstractMsg));

	}

	@Test
	public void testAddRange() {
		LinkedList<ObselType> linkedList = KtbsUtils.toLinkedList(onChannel.listRanges());
		assertEquals(1, linkedList.size());
		assertEquals(openChat, onChannel.getRange());
		onChannel.addRange(abstractMsg);
		linkedList = KtbsUtils.toLinkedList(onChannel.listRanges());
		assertEquals(2, linkedList.size());
		assertTrue(linkedList.contains(abstractMsg));
	}

	@Test
	public void testSetSuperRelationType() {
		assertEquals(onChannel, closes.getSuperRelationType());
		closes.setSuperRelationType(toto);
		assertEquals(toto, closes.getSuperRelationType());
		closes.setSuperRelationType(tata);
		assertEquals(tata, closes.getSuperRelationType());
	}
}
