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

	public static KtbsRoot createKtbsRoot(String resourceUri, String label, String... baseURIs) {
		KtbsRootImpl ktbsRootImpl = new KtbsRootImpl(resourceUri, baseURIs);
		ktbsRootImpl.setLabel(label);
		return ktbsRootImpl;
	}

	public static Base createBase(String baseLocalName, String rootURI, String label, String[] traceURIs, String[] traceModelURIs) {
		BaseImpl baseImpl = new BaseImpl(baseLocalName, rootURI,traceURIs,traceModelURIs);
		baseImpl.setLabel(label);
		return baseImpl;
	}
	
	public static Base createBase(String baseUri, KtbsRoot root, String label) {
		BaseImpl baseImpl = new BaseImpl(baseUri, root);
		baseImpl.setLabel(label);
		return baseImpl;
	}

	public static Trace createTrace(String resourceUri, String traceModelURI, String label, Date origin, Base base) {
		TraceImpl traceImpl = new TraceImpl(resourceUri, traceModelURI, origin, base);
		traceImpl.setLabel(label);
		return traceImpl;
	}

	public static Obsel createObsel(String resourceUri, Trace parentTrace, String subject, Date beginDT, Date endDT, String typeURI, Map<String, Serializable> attributes, String label) {
		if(parentTrace.getObselURIs().contains(resourceUri))
			throw new IllegalStateException("There is already an obsel with the same uri \""+resourceUri+"\" in the trace \""+parentTrace.getURI()+"\".");
		ObselImpl obselImpl = new ObselImpl(resourceUri, parentTrace, subject, beginDT, endDT, -1l, -1l, typeURI, attributes);
		obselImpl.setLabel(label);
		return obselImpl;
	}

	public static long obselID = 0;
	
	public static Obsel createObsel(Trace parentTrace, String subject, String label, Date beginDT, Date endDT, String typeURI, Map<String, Serializable> attributes) {
		
		Collection<String> obselURIs = parentTrace.getObselURIs();
		while(obselURIs.contains(parentTrace.getURI()+ KtbsResourceFactory.obselID + "/"))
			KtbsResourceFactory.obselID++;
		String resourceUri = parentTrace.getURI()+ KtbsResourceFactory.obselID + "/";

		return createObsel(resourceUri, parentTrace, subject, beginDT, endDT, typeURI, attributes, label);
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

	public static Base createBase(String uri, String label) {
		return createBase(uri, (KtbsRoot)null, label);
	}

	public static Obsel createObsel(String obselURI, String traceURI,  String subject,
			Date beginDT, 
			Date endDT,
			String typeURI,
			Map<String, Serializable> attributes, String label) {
		ObselImpl obselImpl = new ObselImpl(obselURI, traceURI, subject, beginDT, endDT, -1l, -1l, typeURI, attributes);
		obselImpl.setLabel(label);
		return obselImpl;
	}

	public static Obsel createObsel(String obselURI, String traceURI,  String subject,
			Date beginDT, 
			Date endDT,
			long begin, 
			long end,
			String typeURI,
			Map<String, Serializable> attributes, String label) {
		ObselImpl obselImpl = new ObselImpl(obselURI, traceURI, subject, beginDT, endDT, begin, end, typeURI, attributes);
		obselImpl.setLabel(label);
		return obselImpl;
	}

	public static Obsel createObsel(String obselURI, String traceURI,  String subject,
			long begin, 
			long end,
			String typeURI,
			Map<String, Serializable> attributes, String label) {
		ObselImpl obselImpl = new ObselImpl(obselURI, traceURI, subject, null, null, begin, end, typeURI, attributes);
		obselImpl.setLabel(label);
		return obselImpl;
	}
	
}
