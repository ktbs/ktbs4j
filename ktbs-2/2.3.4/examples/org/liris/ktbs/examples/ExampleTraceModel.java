package org.liris.ktbs.examples;

import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.ITraceModel;

public class ExampleTraceModel extends TraceModelFiller {

	@Override
	public void fill(ITraceModel model, PojoFactory fac) {
		String mUri = model.getUri();
		
		IObselType abstractMsg = fac.createObselType(mUri, "AbstractMsg");
		IObselType channelEvent = fac.createObselType(mUri, "ChannelEvent");
		IObselType sendMsg = fac.createObselType(mUri, "SendMsg");
		IObselType recvMsg = fac.createObselType(mUri, "RecvMsg");
		IObselType openChat = fac.createObselType(mUri, "OpenChat");
		IObselType closeChat = fac.createObselType(mUri, "CloseChat");
		
		IAttributeType message = fac.createAttributeType(mUri, "message");
		IAttributeType from =fac.createAttributeType(mUri, "from");
		IAttributeType channel = fac.createAttributeType(mUri, "channel");
		
		IRelationType closes = fac.createRelationType(mUri, "closes");
		IRelationType onChannel = fac.createRelationType(mUri, "onChannel");
		
		model.getObselTypes().add(abstractMsg);
		model.getObselTypes().add(channelEvent);
		model.getObselTypes().add(sendMsg);
		model.getObselTypes().add(recvMsg);
		model.getObselTypes().add(openChat);
		model.getObselTypes().add(closeChat);
		
		model.getAttributeTypes().add(message);
		model.getAttributeTypes().add(from);
		model.getAttributeTypes().add(channel);
		
		model.getRelationTypes().add(closes);
		model.getRelationTypes().add(onChannel);
	}
}
