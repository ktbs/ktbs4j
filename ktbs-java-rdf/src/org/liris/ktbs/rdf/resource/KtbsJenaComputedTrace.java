package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsParameter;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.ResourceWithParameters;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class KtbsJenaComputedTrace extends KtbsJenaTrace implements
ComputedTrace {

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

	KtbsJenaComputedTrace(String uri, Model rdfModel) {
		super(uri, rdfModel);
		resourceWithParameterAspect = new KtbsJenaResourceWithParameter(uri, rdfModel);
	}

	@Override
	public Method getMethod() {
		Resource r = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_METHOD);
		if(r==null)
			return null;
		else
			return EmptyResourceFactory.getInstance().createMethod(r.getURI());
	}
}
