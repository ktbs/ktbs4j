package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsParameter;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.ResourceWithParameters;
import org.liris.ktbs.core.Trace;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class KtbsJenaComputedTrace extends KtbsJenaTrace implements ComputedTrace {

	KtbsJenaComputedTrace(String uri, Model rdfModel, ResourceRepository holder) {
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
				repository, true);
	}
	
	@Override
	public Method getMethod() {
		Resource r = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_METHOD);
		if(r==null)
			return null;
		else if(r.getURI().equals(KtbsConstants.FILTER))
			return Method.FILTER;
		else if(r.getURI().equals(KtbsConstants.FUSION))
			return Method.FUSION;
		else
			return repository.getResource(r.getURI(), Method.class);
	}

	@Override
	public void setMethod(Method method) {
		repository.checkExistency(method);
		
		removeAllProperties(KtbsConstants.P_HAS_METHOD);
		rdfModel.getResource(uri).addProperty(
				rdfModel.getProperty(KtbsConstants.P_HAS_METHOD), 
				rdfModel.getResource(method.getURI()));
	}

	@Override
	public void addSourceTrace(Trace sourceTrace) {
		repository.checkExistency(sourceTrace);
		
		rdfModel.getResource(uri).addProperty(
				rdfModel.getProperty(KtbsConstants.P_HAS_SOURCE), 
				rdfModel.getResource(sourceTrace.getURI()));
	}
}
