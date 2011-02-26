package org.liris.ktbs.tests;

import org.liris.ktbs.core.domain.PojoFactory;
import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IRelationType;
import org.liris.ktbs.core.domain.interfaces.IRoot;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;

public class Examples {
	
	private static PojoFactory pojoFactory = new PojoFactory();
	
	public static IRoot getRoot() {
		IRoot root = pojoFactory.createResource("http://locahost:8001/", IRoot.class);
		root.addLabel("Un KTBS Root");
		root.getBases().add(getBase1());
		return root;
	}

	public static IBase getBase1() {
		IBase base1 = pojoFactory.createResource("http://locahost:8001/base1/", IBase.class);
		base1.addLabel("Label de la base 1");
		base1.getStoredTraces().add(getStoredTrace1());
		base1.getTraceModels().add(getTraceModel1());
		return base1;
	}

	public static IBase getBase2() {
		IBase base1 = pojoFactory.createResource("http://locahost:8001/base2/", IBase.class);
		return base1;
	}

	public static IStoredTrace getStoredTrace1() {
		IStoredTrace t01 = pojoFactory.createResource("http://locahost:8001/base1/t01/", IStoredTrace.class);
		t01.addLabel("Label 1");
		t01.addLabel("Label 2");
		t01.addLabel("Label 2");
		
		t01.setTraceModel(getTraceModel1());
		t01.setOrigin("Origine de la trace 1");
		t01.setDefaultSubject("Nestor");
		
		return t01;
	}

	public static ITraceModel getTraceModel1() {
		ITraceModel model1 = pojoFactory.createResource("http://locahost:8001/base1/model1/", ITraceModel.class);
		
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
		
		abstractMsg.getSuperObselTypes().add(channelEvent);
		sendMsg.getSuperObselTypes().add(abstractMsg);
		recvMsg.getSuperObselTypes().add(abstractMsg);
		closeChat.getSuperObselTypes().add(channelEvent);
		
		message.getDomains().add(abstractMsg);
		from.getDomains().add(recvMsg);
		channel.getDomains().add(openChat);
		
		closes.getDomains().add(closeChat);
		closes.getSuperRelationTypes().add(onChannel);
		onChannel.getDomains().add(channelEvent);
		onChannel.getRanges().add(openChat);
		
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
		
		return model1;
	}

	public static IObselType getAbstractMessage() {
		IObselType abstractMsg = pojoFactory.createResource("http://locahost:8001/base1/model1/AbstractMsg", IObselType.class);
		
		return abstractMsg;
	}

	public static IObselType getChannelEvent() {
		IObselType channelEvent = pojoFactory.createResource("http://locahost:8001/base1/model1/ChannelEvent", IObselType.class);
		return channelEvent;
	}

	public static IObselType getRecvMsg() {
		return pojoFactory.createResource("http://locahost:8001/base1/model1/RecvMsg", IObselType.class);
	}
	
	public static IObselType getSendMsg() {
		return pojoFactory.createResource("http://locahost:8001/base1/model1/SendMsg", IObselType.class);
	}

	public static IObselType getCloseChat() {
		return pojoFactory.createResource("http://locahost:8001/base1/model1/CloseChat", IObselType.class);
	}
	
	public static IObselType getOpenChat() {
		return pojoFactory.createResource("http://locahost:8001/base1/model1/OpenChat", IObselType.class);
	}

	public static IRelationType getOnChannel() {
		return pojoFactory.createResource("http://locahost:8001/base1/model1/onChannel", IRelationType.class);
	}
	
	public static IRelationType getCloses() {
		return pojoFactory.createResource("http://locahost:8001/base1/model1/closes", IRelationType.class);
	}

	public static IAttributeType getMessage() {
		return pojoFactory.createResource("http://locahost:8001/base1/model1/message", IAttributeType.class);
	}

	public static IAttributeType getFrom() {
		return pojoFactory.createResource("http://locahost:8001/base1/model1/from", IAttributeType.class);
	}
	public static IAttributeType getChannel() {
		return pojoFactory.createResource("http://locahost:8001/base1/model1/channel", IAttributeType.class);
	}
	
	
}
