package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsParameter;
import org.liris.ktbs.core.KtbsResourceHolder;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.ResourceWithParameters;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class KtbsJenaComputedTrace extends KtbsJenaTrace implements
ComputedTrace {

	KtbsJenaComputedTrace(String uri, Model rdfModel, KtbsResourceHolder holder) {
		super(uri, rdfModel, holder);
		resourceWithParameterAspect = new KtbsJenaResourceWithParameter(uri, rdfModel, holder);
	}
	private ResourceWithParameters resourceWithParameterAspect;
	
	@Override
	public Iterator<KtbsParameter> listParameters() {
		return resourceWithParameterAspect.listParameters();
	}

	@Override
	public void setParameter(String key, String value) {
		resourceWithParameterAspect.setParameter(key, value);
	}

	@Override
	public KtbsParameter getParameter(String key) {
		return resourceWithParameterAspect.getParameter(key);
	}

	@Override
	public void removeParameter(String key) {
		resourceWithParameterAspect.removeParameter(key);
	}

	@Override
	public Iterator<Trace> listSources() {
		StmtIterator it = rdfModel.listStatements(
				rdfModel.getResource(uri), 
				rdfModel.getProperty(KtbsConstants.P_HAS_SOURCE), 
				(RDFNode)null);
		
		return new KtbsResourceObjectIterator<Trace>(
				it, 
				Trace.class,
				holder);
	}
	@Override
	public Method getMethod() {
		Resource r = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_METHOD);
		if(r==null)
			return null;
		else
			return holder.getResource(r.getURI(), Method.class);
	}

	@Override
	public void setTraceModel(TraceModel traceModel) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Method setMethod() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addSourceTrace(Trace sourceTrace) {
		throw new UnsupportedOperationException();
	}
}
