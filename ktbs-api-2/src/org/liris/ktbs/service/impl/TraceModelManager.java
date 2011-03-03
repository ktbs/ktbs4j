package org.liris.ktbs.service.impl;

import java.io.Reader;
import java.util.Set;

import org.liris.ktbs.core.domain.PojoFactory;
import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IRelationType;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.service.TraceModelService;
import org.liris.ktbs.utils.KtbsUtils;

public class TraceModelManager implements TraceModelService {

	private PojoFactory factory;
	public void setFactory(PojoFactory factory) {
		this.factory = factory;
	}

	private ResourceDao dao;
	public void setDao(ResourceDao dao) {
		this.dao = dao;
	}

	@Override
	public boolean save(ITraceModel model) {
		return dao.save(model, true);
	}

	@Override
	public IAttributeType newAttributeType(ITraceModel model, String localName,
			IObselType domain) {
		IAttributeType attType = factory.createResource(KtbsUtils.makeChildURI(model.getUri(), localName, true), IAttributeType.class);

		if(domain != null)
			attType.getDomains().add(domain);

		Set<IAttributeType> elementSet = model.getAttributeTypes();
		
		return addAndSaveAndReturn(model, attType, elementSet);
	}

	private <T extends IKtbsResource> T addAndSaveAndReturn(ITraceModel model,
			T element, Set<T> elementSet) {
		boolean added = elementSet.add(element);
		if(!added)
			// already present
			return null;

		boolean save = save(model);
		if(save)
			return element;
		else {
			boolean removed = elementSet.remove(element);
			if(removed)
				return null;
			else 
				throw new RuntimeException("Could not save the modifed trace model nor roll back to " +
						"its previous state. The trace model " + model.getUri() + " " +
				"is in an inconsistent state.");
		}
	}

	@Override
	public IRelationType newRelationType(ITraceModel model, String localName,
			IObselType domain, IObselType range) {
		IRelationType relType = factory.createResource(KtbsUtils.makeChildURI(model.getUri(), localName, true), IRelationType.class);
		
		if(domain != null)
			relType.getDomains().add(domain);
		if(range != null)
			relType.getRanges().add(range);
		
		
		return addAndSaveAndReturn(model, relType, model.getRelationTypes());
	}

	@Override
	public IObselType newObselType(ITraceModel model, String localName) {
		return newObselType(model, localName, null);
	}

	@Override
	public IObselType newObselType(ITraceModel model, String localName,
			IObsel superType) {
		IObselType obsType = factory.createResource(KtbsUtils.makeChildURI(model.getUri(), localName, true), IObselType.class);
		
		if(superType != null)
			obsType.getSuperObselTypes().add(obsType);
		
		return addAndSaveAndReturn(model, obsType, model.getObselTypes());
	}

	@Override
	public boolean importModel(String modelUri, boolean override,
			Reader reader, String mimeType) {
		// TODO Auto-generated method stub
		return false;
	}
}
