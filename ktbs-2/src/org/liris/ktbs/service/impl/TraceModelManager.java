package org.liris.ktbs.service.impl;

import java.io.Reader;
import java.util.Set;

import org.liris.ktbs.dao.DaoException;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.dao.rest.KtbsResponse;
import org.liris.ktbs.dao.rest.ResourceAlreadyExistException;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.ResponseAwareService;
import org.liris.ktbs.service.TraceModelService;
import org.liris.ktbs.utils.KtbsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TraceModelManager extends RootAwareService implements TraceModelService, ResponseAwareService {

	private PojoFactory factory;
	public void setFactory(PojoFactory factory) {
		this.factory = factory;
	}

	private ResourceDao dao;
	public void setDao(ResourceDao dao) {
		this.dao = dao;
	}

	private ResourceService resourceService;
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@Override
	public KtbsResponse getLastResponse() {
		return dao.getLastResponse();
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

	private static final Logger log = LoggerFactory.getLogger(TraceModelManager.class);

	private <T extends IKtbsResource> T addAndSaveAndReturn(ITraceModel model,
			T element, Set<T> elementSet) {
		boolean added = elementSet.add(element);
		if(!added) 
			// already present
			log.warn("The attribute type is already is the local copy of the trace model.");

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

	@Override
	public boolean save(ITraceModel model, boolean createIfDoesntExist) {
		try {
			return dao.save(model, true);
		} catch(ResourceNotFoundException e) {
			if(createIfDoesntExist)
				return createTraceModel(model);
			else
				throw new RuntimeException(e);
		}
	}


	/*
	 * creates a trace model and save it
	 */
	private boolean createTraceModel(ITraceModel model) {
		log.info("The trace model " + model + " may not exist. Trying to create it.");
		createTraceModel(model);
		return dao.save(model, true);
	}

	@Override
	public boolean save(ITraceModel model) {
		return save(model, false);
	}

	@Override
	public ITraceModel createTraceModel(String modelUri) {
		try {
			return dao.createAndGet(factory.createResource(modelUri, ITraceModel.class));
		} catch(ResourceAlreadyExistException e) {
			return dao.get(modelUri, ITraceModel.class);
		} catch(DaoException e) {
			// try to get the resource
			return dao.get(modelUri, ITraceModel.class);
		}
	}
}
