package org.liris.ktbs.tests;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.core.domain.AttributePair;
import org.liris.ktbs.core.domain.KtbsResource;
import org.liris.ktbs.core.domain.PojoFactory;
import org.liris.ktbs.core.domain.RelationStatement;
import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IRelationStatement;
import org.liris.ktbs.core.domain.interfaces.IRelationType;
import org.liris.ktbs.core.domain.interfaces.IRoot;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
import org.liris.ktbs.utils.ThreeKeyedMap;

import com.ibm.icu.util.Calendar;

public class Examples {
	
	private static PojoFactory pojoFactory = new PojoFactory();
	
	private static Map<String, IKtbsResource> resources = new HashMap<String, IKtbsResource>();
	private static ThreeKeyedMap<IRelationStatement> relations = new ThreeKeyedMap<IRelationStatement>();

	
	public static IRoot getRoot() {
		String uri = "http://localhost:8001/";
		
		if(resources.containsKey(uri))
			return (IRoot)resources.get(uri);
		
		IRoot root = pojoFactory.createResource(uri, IRoot.class);
		root.addLabel("Un KTBS Root");
		root.getBases().add(getBase1());
		root.getBases().add(getBase2());
		
		resources.put(uri, root);
		return root;
	}

	public static IBase getBase1() {
		String uri = "http://localhost:8001/base1/";
		if(resources.containsKey(uri))
			return (IBase)resources.get(uri);
		IBase base1 = pojoFactory.createResource(uri, IBase.class);
		base1.addLabel("Label de la base 1");
		base1.getStoredTraces().add(getStoredTrace1());
		base1.getTraceModels().add(getTraceModel1());
		resources.put(uri, base1);
		return base1;
	}

	public static IBase getBase2() {
		String uri = "http://localhost:8001/base2/";
		if(resources.containsKey(uri))
			return (IBase)resources.get(uri);
		IBase base2 = pojoFactory.createResource(uri, IBase.class);
		resources.put(uri, base2);
		
		return base2;
	}

	public static IStoredTrace getStoredTrace1() {
		String uri = "http://localhost:8001/base1/t01/";
		if(resources.containsKey(uri))
			return (IStoredTrace)resources.get(uri);
		IStoredTrace t01 = pojoFactory.createResource(uri, IStoredTrace.class);
		t01.addLabel("Label 1");
		t01.addLabel("Label 2");
		t01.addLabel("Label 2");
		
		t01.setTraceModel(getTraceModel1());
		t01.setOrigin("Origine de la trace 1");
		t01.setDefaultSubject("Nestor");
		
		t01.getObsels().add(getObs1());
		t01.getObsels().add(getObs2());
		t01.getObsels().add(getObs3());
		t01.getObsels().add(getObs4());
		
		resources.put(uri, t01);
		return t01;
	}

	public static ITraceModel getTraceModel1() {
		String uri = "http://localhost:8001/base1/model1/";
		if(resources.containsKey(uri))
			return (ITraceModel)resources.get(uri);
		ITraceModel model1 = pojoFactory.createResource(uri, ITraceModel.class);
		
		IObselType abstractMsg = getAbstractMessage();
		IObselType channelEvent = getChannelEvent();
		IObselType sendMsg = getSendMsg();
		IObselType recvMsg = getRecvMsg();
		IObselType openChat = getOpenChat();
		IObselType closeChat = getCloseChat();
		
		IAttributeType message = getMessage();
		IAttributeType from = getFrom();
		IAttributeType channel = getChannel();
		
		IRelationType closes = getCloses();
		IRelationType onChannel = getOnChannel();
		
		
		model1.getObselTypes().add(abstractMsg);
		model1.getObselTypes().add(channelEvent);
		model1.getObselTypes().add(sendMsg);
		model1.getObselTypes().add(recvMsg);
		model1.getObselTypes().add(openChat);
		model1.getObselTypes().add(closeChat);
		
		model1.getAttributeTypes().add(message);
		model1.getAttributeTypes().add(from);
		model1.getAttributeTypes().add(channel);
		
		model1.getRelationTypes().add(closes);
		model1.getRelationTypes().add(onChannel);
		
		resources.put(uri, model1);
		return model1;
	}

	public static IObselType getAbstractMessage() {
		String uri = "http://localhost:8001/base1/model1/AbstractMsg";
		if(resources.containsKey(uri))
			return (IObselType)resources.get(uri);
		IObselType abstractMsg = pojoFactory.createResource(uri, IObselType.class);
		
		resources.put(uri, abstractMsg);
		return abstractMsg;
	}

	public static IObselType getChannelEvent() {
		String uri = "http://localhost:8001/base1/model1/ChannelEvent";
		if(resources.containsKey(uri))
			return (IObselType)resources.get(uri);
		IObselType channelEvent = pojoFactory.createResource(uri, IObselType.class);
		resources.put(uri, channelEvent);
		channelEvent.getSuperObselTypes().add(getChannelEvent());
		return channelEvent;
	}

	public static IObselType getRecvMsg() {
		String uri = "http://localhost:8001/base1/model1/RecvMsg";
		if(resources.containsKey(uri))
			return (IObselType)resources.get(uri);
		IObselType recvMsg = pojoFactory.createResource(uri, IObselType.class);
		resources.put(uri, recvMsg);
		recvMsg.getSuperObselTypes().add(getAbstractMessage());
		return recvMsg;
	}
	
	public static IObselType getSendMsg() {
		String uri = "http://localhost:8001/base1/model1/SendMsg";
		if(resources.containsKey(uri))
			return (IObselType)resources.get(uri);
		IObselType sendMsg = pojoFactory.createResource(uri, IObselType.class);
		resources.put(uri, sendMsg);
		sendMsg.getSuperObselTypes().add(getAbstractMessage());
		return sendMsg;
	}

	public static IObselType getCloseChat() {
		String uri = "http://localhost:8001/base1/model1/CloseChat";
		if(resources.containsKey(uri))
			return (IObselType)resources.get(uri);
		IObselType closeChat = pojoFactory.createResource(uri, IObselType.class);
		resources.put(uri, closeChat);
		closeChat.getSuperObselTypes().add(getChannelEvent());
		return closeChat;
	}
	
	public static IObselType getOpenChat() {
		String uri = "http://localhost:8001/base1/model1/OpenChat";
		if(resources.containsKey(uri))
			return (IObselType)resources.get(uri);
		IObselType openChat = pojoFactory.createResource(uri, IObselType.class);
		resources.put(uri, openChat);
		return openChat;
	}

	public static IRelationType getOnChannel() {
		String uri = "http://localhost:8001/base1/model1/onChannel";
		if(resources.containsKey(uri))
			return (IRelationType)resources.get(uri);
		IRelationType onChannel = pojoFactory.createResource(uri, IRelationType.class);
		resources.put(uri, onChannel);
		onChannel.getDomains().add(getChannelEvent());
		onChannel.getRanges().add(getOpenChat());
		return onChannel;
	}
	
	public static IRelationType getCloses() {
		String uri = "http://localhost:8001/base1/model1/closes";
		if(resources.containsKey(uri))
			return (IRelationType)resources.get(uri);
		IRelationType closes = pojoFactory.createResource(uri, IRelationType.class);
		resources.put(uri, closes);
		closes.getSuperRelationTypes().add(getOnChannel());
		closes.getDomains().add(getCloseChat());
		return closes;
	}

	public static IAttributeType getMessage() {
		String uri = "http://localhost:8001/base1/model1/message";
		if(resources.containsKey(uri))
			return (IAttributeType)resources.get(uri);
		IAttributeType message = pojoFactory.createResource(uri, IAttributeType.class);
		resources.put(uri, message);
		message.getDomains().add(getAbstractMessage());
		return message;
	}

	public static IAttributeType getFrom() {
		String uri = "http://localhost:8001/base1/model1/from";
		if(resources.containsKey(uri))
			return (IAttributeType)resources.get(uri);
		IAttributeType from = pojoFactory.createResource(uri, IAttributeType.class);
		resources.put(uri, from);
		from.getDomains().add(getRecvMsg());
		return from;
	}
	
	public static IAttributeType getChannel() {
		String uri = "http://localhost:8001/base1/model1/channel";
		if(resources.containsKey(uri))
			return (IAttributeType)resources.get(uri);
		IAttributeType channel = pojoFactory.createResource(uri, IAttributeType.class);
		resources.put(uri, channel);
		channel.getDomains().add(getOpenChat());
		return channel;
	}

	public static Set<IObsel> getT01Obsels() {
		IObsel obs1 = getObs1();
		IObsel obs2 = getObs2();
		IObsel obs3 = getObs3();
		IObsel obs4 = getObs4();
		
		
		Set<IObsel> obsels = new HashSet<IObsel>();
		obsels.add(obs1);
		obsels.add(obs2);
		obsels.add(obs3);
		obsels.add(obs4);
		
		return obsels;
	}

	
	private static IRelationStatement getRelation(IObsel source, IRelationType relType, IObsel target) {

		IRelationStatement rel = relations.get(source.getUri(), relType.getUri(), target.getUri());
		
		if(rel != null)
			return rel;
		
		rel = new RelationStatement(source, relType, target);
		
		relations.put(source.getUri(), relType.getUri(), target.getUri(), rel);
		
		return rel;
	}
	
	public static IObsel getObs1() {
		String uri = "http://localhost:8001/base1/t01/obs1";
		if(resources.containsKey(uri))
			return (IObsel) resources.get(uri);
		
		IObsel obs1 = pojoFactory.createResource(uri, IObsel.class);
		resources.put(uri, obs1);
		
		
		obs1.setBegin(new BigInteger("1000"));
		obs1.setEnd(new BigInteger("1000"));
		obs1.setBeginDT(cal(28,4,2010,18,9,1).getTime().toString());
		obs1.setEndDT(cal(28,4,2010,18,9,1).getTime().toString());
		obs1.setSubject("béa");
		((KtbsResource)obs1).setParentResource(getStoredTrace1());
		obs1.setObselType(getOpenChat());
		obs1.getIncomingRelations().add(getRelation(getObs2(), getOnChannel(), obs1));
		obs1.getIncomingRelations().add(getRelation(getObs3(), getOnChannel(), obs1));
		obs1.getIncomingRelations().add(getRelation(getObs4(), getOnChannel(), obs1));
		
		return obs1;
	}
	
	public static IObsel getObs2() {
		String uri = "http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f";
		if(resources.containsKey(uri))
			return (IObsel) resources.get(uri);
		IObsel obs2 = pojoFactory.createResource(uri, IObsel.class);
		resources.put(uri, obs2);
		obs2.setBegin(new BigInteger("2000"));
		obs2.setEnd(new BigInteger("4000"));
		obs2.setBeginDT(cal(28,4,2010,18,9,2).getTime().toString());
		obs2.setEndDT(cal(28,4,2010,18,9,4).getTime().toString());
		obs2.setSubject("béa");
		((KtbsResource)obs2).setParentResource(getStoredTrace1());
		obs2.setObselType(getSendMsg());
		obs2.getAttributePairs().add(new AttributePair(getMessage(), "hello world"));
		obs2.getOutgoingRelations().add(getRelation(obs2, getOnChannel(), getObs1()));
		return obs2;
	}
	
	public static IObsel getObs3() {
		String uri = "http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139";
		if(resources.containsKey(uri))
			return (IObsel) resources.get(uri);
		IObsel obs3 = pojoFactory.createResource(uri, IObsel.class);
		resources.put(uri, obs3);
		obs3.setBegin(new BigInteger("5000"));
		obs3.setEnd(new BigInteger("5000"));
		obs3.setBeginDT(cal(28,4,2010,18,9,5).getTime().toString());
		obs3.setEndDT(cal(28,4,2010,18,9,5).getTime().toString());
		obs3.setSubject("béa");
		((KtbsResource)obs3).setParentResource(getStoredTrace1());
		obs3.setObselType(getRecvMsg());
		obs3.getAttributePairs().add(new AttributePair(getMessage(), "hello world"));
		obs3.getAttributePairs().add(new AttributePair(getFrom(), "world"));
		obs3.getOutgoingRelations().add(getRelation(obs3, getOnChannel(), getObs1()));
		return obs3;
	}
	
	public static IObsel getObs4() {
		String uri = "http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575";
		if(resources.containsKey(uri))
			return (IObsel) resources.get(uri);
		IObsel obs4 = pojoFactory.createResource(uri, IObsel.class);
		resources.put(uri, obs4);
		obs4.setBegin(new BigInteger("7000"));
		obs4.setEnd(new BigInteger("7000"));
		obs4.setBeginDT(cal(28,4,2010,18,9,7).getTime().toString());
		obs4.setEndDT(cal(28,4,2010,18,9,7).getTime().toString());
		obs4.setSubject("béa");
		((KtbsResource)obs4).setParentResource(getStoredTrace1());
		obs4.setObselType(getCloseChat());
		obs4.getOutgoingRelations().add(getRelation(obs4, getOnChannel(), getObs1()));
		return obs4;
	}
	
	private static Calendar cal(int d, int m, int y, int h, int min, int s) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, d);
		c.set(Calendar.MONTH, m);
		c.set(Calendar.YEAR, y);
		c.set(Calendar.HOUR_OF_DAY, h);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, s);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}
	
	
}
