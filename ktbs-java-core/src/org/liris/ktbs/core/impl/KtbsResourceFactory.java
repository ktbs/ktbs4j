package org.liris.ktbs.core.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;


public class KtbsResourceFactory {

	public static KtbsRoot createKtbsRoot(String resourceUri, String... strings) {
		return new KTBSRootImpl(resourceUri);
	}

	public static Base createBase(String baseLocalName, String rootURI, String[] traceURIs, String[] traceModelURIs) {
		return new BaseImpl(baseLocalName, rootURI,traceURIs,traceModelURIs);
	}
	
	public static Base createBase(String baseUri, KtbsRoot root) {
		return new BaseImpl(baseUri, root);
	}

	public static Trace createTrace(String resourceUri, String traceModelURI, Date origin, Base base) {
		return new TraceImpl(resourceUri, traceModelURI, origin, base);
	}

	public static Obsel createObsel(String resourceUri, Trace parentTrace, Date begin, Date end, String typeURI, Map<String, Serializable> attributes) {
		if(parentTrace.getObselURIs().contains(resourceUri))
			throw new IllegalStateException("There is already an obsel with the same uri \""+resourceUri+"\" in the trace \""+parentTrace.getURI()+"\".");
		return new ObselImpl(resourceUri, parentTrace, begin, end, typeURI, attributes);
	}

	public static long obselID = 0;
	public static Obsel createObsel(Trace parentTrace, Date begin, Date end, String typeURI, Map<String, Serializable> attributes) {
		
		Collection<String> obselURIs = parentTrace.getObselURIs();
		while(obselURIs.contains(parentTrace.getURI()+ KtbsResourceFactory.obselID + "/"))
			KtbsResourceFactory.obselID++;
		String resourceUri = parentTrace.getURI()+ KtbsResourceFactory.obselID + "/";

		return createObsel(resourceUri, parentTrace, begin, end, typeURI, attributes);
	}

	public static Relation createRelation(Obsel from, String relationName, Obsel to) {
		
		Relation relation = new RelationImpl(from, to, relationName);
		from.addOutgoingRelation(relation);
		to.addIncomingRelation(relation);
		
		return relation;
	}

	/**
	 * Creates a representation of an inter-obsel relation where the source and the target 
	 * {@link Obsel} are represented by their URIs only (Relatiion.getFrom() and relation.getTo() 
	 * should both return null).
	 * @param fromObselURI the URI of the source obsel of this relation
	 * @param relationName the URI of the relation
	 * @param toObselURI the URI of the target obsel of this relation
	 * @return the created relation
	 */
	public static Relation createRelation(String fromObselURI, String relationName, String toObselURI) {
		Relation relation = new RelationImpl(fromObselURI, toObselURI, relationName);
		return relation;
	}

	public static Base createBase(String uri) {
		return createBase(uri, (KtbsRoot)null);
	}

	public static Obsel createObsel(String obselURI, String traceURI,
			Date begin, Date end, String typeURI,
			Map<String, Serializable> attributes) {
		return new ObselImpl(obselURI, traceURI, begin, end, typeURI, attributes);
	}
	
}
