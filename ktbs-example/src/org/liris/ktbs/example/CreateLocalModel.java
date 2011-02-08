package org.liris.ktbs.example;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.SerializableResource;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.Mode;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.rdf.resource.RdfResourceRepository;

public class CreateLocalModel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceRepository repository = new RdfResourceRepository();
		
		Base base = repository.createBase("http://localhost:8001/base1/");
		
		TraceModel tm = base.newTraceModel("http://localhost:8001/base1/model1/");
		
		/*
		 * Names of new trace model elements are given as local names, because 
		 * they must be prefixed by their trace model's URI.
		 */
		ObselType channelEvent = tm.newObselType("ChannelEvent");
		ObselType openChat = tm.newObselType("OpenChat");
		
		ObselType closeChat = tm.newObselType("CloseChat");
		closeChat.setSuperObselType(channelEvent);
		
		ObselType abstractMsg = tm.newObselType("AbstractMsg");
		abstractMsg.setSuperObselType(channelEvent);
		
		ObselType sendMsg = tm.newObselType("SendMsg");
		sendMsg.setSuperObselType(abstractMsg);
		
		ObselType recvMsg = tm.newObselType("RecvMsg");
		recvMsg.setSuperObselType(abstractMsg);

		AttributeType message = tm.newAttributeType("message", abstractMsg);
		AttributeType from = tm.newAttributeType("from", recvMsg);
		AttributeType channel = tm.newAttributeType("channel", openChat);
		
		RelationType onChannel = tm.newRelationType("onChannel", channelEvent, openChat);
		RelationType closes = tm.newRelationType("closes", closeChat, null);
		closes.setSuperRelationType(onChannel);
		
		/*
		 * Write this model to the console in turtle 
		 */
		System.out.println(((SerializableResource)tm).toSerializedString(KtbsConstants.MIME_TURTLE));

		
		System.out.println("------------------------------------");

		/*
		 * Navigate in attribute types
		 */
		Iterator<AttributeType> attTypes = tm.listAttributeTypes();
		while (attTypes.hasNext()) {
			AttributeType attType = attTypes.next();
			System.out.println("The domain of " + attType.getURI() + " is: " + attType.getDomain());
		}
		
		
		System.out.println("------------------------------------");
		/*
		 * Navigate in relation types
		 */
		Iterator<RelationType> relTypes = tm.listRelationTypes();
		while (relTypes.hasNext()) {
			RelationType relType = relTypes.next();
			System.out.println("The domain of " + relType.getURI() + " is: " + relType.getDomain() + ", the range is: " + relType.getRange());
			
			System.out.println("Inferred domains: ");
			for(ObselType domain:relType.getDomainsInferred()) 
				System.out.println("\t - " + domain.getURI());
			
			System.out.println("Inferred ranges: ");
			for(ObselType range:relType.getRangesInferred()) 
				System.out.println("\t - " + range.getURI());
			System.out.println("------------------------------------");
		}

		/*
		 * Navigate in relation types
		 */
		Iterator<ObselType> obsTypes = tm.listObselTypes();
		while (obsTypes.hasNext()) {
			ObselType obsType = obsTypes.next();
			
			System.out.println("Declared attributes of the obsel type " + obsType.getURI() + " are: ");
			Iterator<AttributeType> atts = obsType.listAttributes(Mode.ASSERTED);
			while (atts.hasNext()) {
				AttributeType a = (AttributeType) atts.next();
				System.out.println("\t - " + a.getURI());
				
			}
			
			System.out.println("Inferred attributes of the obsel type " + obsType.getURI() + " are: ");
			Iterator<AttributeType> attsInf = obsType.listAttributes(Mode.INFERRED);
			while (attsInf.hasNext()) {
				AttributeType a = (AttributeType) attsInf.next();
				System.out.println("\t - " + a.getURI());
			}

			/*
			 * The same can be done with incoming relations and outgoing relations
			 */
			
			System.out.println("------------------------------------");
			
		}
		
	}
}
