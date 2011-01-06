package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class KtbsJenaComputedTrace extends KtbsJenaTrace implements
ComputedTrace {

	KtbsJenaComputedTrace(String uri, Model rdfModel) {
		super(uri, rdfModel);
	}

	@Override
	public Method getMethod() {
		Resource r = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_METHOD);
		if(r==null)
			return null;
		else
			return EmptyResourceFactory.getInstance().createMethod(r.getURI());
	}

	@Override
	public Iterator<String> listParameterKeys() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getParameter(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setParameter(String key, String value) {
		throw new ReadOnlyObjectException(this);
	}

	@Override
	public void removeParameter(String key) {
		throw new ReadOnlyObjectException(this);
	}

}
