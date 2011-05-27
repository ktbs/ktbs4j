package org.liris.ktbs.service.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.client.KtbsConstants;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.dao.rest.KtbsResponse;
import org.liris.ktbs.domain.AttributePair;
import org.liris.ktbs.domain.Obsel;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.IAttributePair;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.service.IRootAwareService;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.ResponseAwareService;
import org.liris.ktbs.service.StoredTraceService;
import org.liris.ktbs.utils.KtbsUtils;

public class StoredTraceManager implements StoredTraceService, ResponseAwareService, IRootAwareService {

	private boolean lastDelegatedToResourceManager = false;
	@Override
	public KtbsResponse getLastResponse() {
		if(lastDelegatedToResourceManager)
			return resourceService.getLastResponse();
		return dao.getLastResponse();
	}

	private ResourceService resourceService;
	private ResourceDao dao;
	private PojoFactory pojoFactory;

	private Map<String, IStoredTrace> bufferedTraces = new HashMap<String, IStoredTrace>();
	private Map<String, Deque<IObsel>> bufferedTraceObsels = new HashMap<String, Deque<IObsel>>();

	public StoredTraceManager(ResourceService resourceService, ResourceDao dao, PojoFactory pojoFactory) {
		super();
		this.resourceService = resourceService;
		this.pojoFactory = pojoFactory;
		this.dao = dao;
	}

	@Override
	public void startBufferedCollect(IStoredTrace trace) {
		if(bufferedTraces.containsKey(trace.getUri()))
			throw new RuntimeException("The trace " + trace.getUri() + " is already under a buffered collect");
		else {
			bufferedTraces.put(trace.getUri(), trace);
			bufferedTraceObsels.put(trace.getUri(), new LinkedList<IObsel>());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postBufferedObsels(IStoredTrace trace) {
		if(!bufferedTraces.containsKey(trace.getUri()))
			throw new RuntimeException("The trace " + trace.getUri() + " is not under a buffered collect");
		else {
			bufferedTraces.remove(trace.getUri());
			Deque<IObsel> obsels = bufferedTraceObsels.remove(trace.getUri());

			// sort the collection
			Collections.sort((List<IObsel>)obsels, new Comparator<IObsel>() {
				@Override
				public int compare(IObsel o1, IObsel o2) {
					if(o1.getEndDT() == null && o2.getEndDT() == null) {
						if(o1.getEnd() == null || o2.getEnd() == null)
							return 0;
						else {
							return o1.getEnd().compareTo(o2.getEnd());
						}
					} else if(o1.getEndDT() == null ||o2.getEndDT() == null)
						return 0;
					else {
						try {
							Date d1 = KtbsUtils.parseXsdDate(o1.getEndDT());
							Date d2 = KtbsUtils.parseXsdDate(o2.getEndDT());
							return d1.compareTo(d2);
						} catch (ParseException e) {
							return 0;
						}
					}
				}
			});

			// TODO to be improved when multiple post is allowed
			for(IObsel obsel:obsels) {
				if(obsel.getParentResource() == null)
					((Obsel)obsel).setParentResource(trace);
				this.lastDelegatedToResourceManager = false;
				dao.create(obsel);
			}
		}
	}

	@Override
	public boolean saveDescription(IStoredTrace trace) {
		this.lastDelegatedToResourceManager = false;

		return dao.save(trace, false);
	}

	@Override
	public boolean saveObsels(IStoredTrace trace) {
		String uriWithAspect = KtbsUtils.addAspect(trace.getUri(), KtbsConstants.OBSELS_ASPECT, KtbsConstants.ABOUT_ASPECT);
		this.lastDelegatedToResourceManager = false;
		return dao.saveCollection(uriWithAspect, trace.getObsels());
	}

	@Override
	public String newObsel(IStoredTrace storedTrace, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Set<IAttributePair> attributes) {
		if(bufferedTraces.containsKey(storedTrace.getUri())) {
			IObsel obsel = pojoFactory.createObsel(
					storedTrace, 
					obselLocalName, 
					typeUri,
					beginDT, 
					endDT, 
					begin, 
					end, 
					subject, 
					attributes);
			bufferedTraceObsels.get(storedTrace.getUri()).add(obsel);
			return obsel.getUri();
		} else {
			this.lastDelegatedToResourceManager = true;
			String newObsel = resourceService.newObsel(storedTrace.getUri(), obselLocalName, typeUri, beginDT, endDT,
					begin, end, subject, attributes);
			return newObsel;
		}
	}

	String createObsel(IStoredTrace storedTrace, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Set<IAttributePair> attributes) {
		return newObsel(storedTrace, obselLocalName, typeUri, beginDT, endDT, begin, end, subject, attributes);
	}

	@Override
	public String newObsel(IStoredTrace storedTrace, String typeUri,
			long begin, Set<IAttributePair> attributes) {
		return newObsel(
				storedTrace, 
				null,
				typeUri,
				null,
				null,
				KtbsUtils.longToBigInt(begin),
				KtbsUtils.longToBigInt(begin),
				null,
				attributes);
	}

	@Override
	public String newObsel(IStoredTrace storedTrace, String typeUri, long begin) {
		return newObsel(
				storedTrace, 
				null,
				typeUri,
				null,
				null,
				KtbsUtils.longToBigInt(begin),
				KtbsUtils.longToBigInt(begin),
				null,
				null);
	}

	@Override
	public Collection<IObsel> listObsels(IStoredTrace storedTrace, long begin, long end) {
		String request = KtbsUtils.addAspect(storedTrace.getUri(), KtbsConstants.OBSELS_ASPECT, KtbsConstants.ABOUT_ASPECT) ;
		request+="?";
		request+="minb="+begin;
		request+="&";
		request+="maxe="+end;
		this.lastDelegatedToResourceManager = false;

		return dao.query(request, IObsel.class);
	}

	@Override
	public Collection<IObsel> listObsels(IStoredTrace storedTrace, long minb, long maxb, long mine,
			long maxe) {
		String request = KtbsUtils.addAspect(storedTrace.getUri(), KtbsConstants.OBSELS_ASPECT, KtbsConstants.ABOUT_ASPECT) ;
		request+="?";
		request+="minb="+minb;
		request+="&";
		request+="maxb="+maxb;
		request+="&";
		request+="mine="+mine;
		request+="&";
		request+="maxe="+maxe;
		this.lastDelegatedToResourceManager = false;

		return dao.query(request, IObsel.class);
	}


	private static int cnt = 0;
	private String generateTraceId(String baseUri) {
		IBase base = resourceService.getResource(baseUri, IBase.class);
		Set<String> uris = new HashSet<String>();
		for(IKtbsResource baseResource:base)
			uris.add(baseResource.getUri());

		String name;
		do 
			name = KtbsUtils.makeAbsoluteURI(baseUri,"storedTrace" + cnt++, false);
		while(uris.contains(name));

		return name;
	}

	@Override
	public String newStoredTrace(String baseUri, String traceModelUri,
			String defaultSubject) {

		this.lastDelegatedToResourceManager = true;

		String now = KtbsUtils.now();
		String traceUri = resourceService.newStoredTrace(baseUri, 
				generateTraceId(baseUri), 
				traceModelUri, 
				now,
				null,
				now,
				null,
				null,
				defaultSubject);

		return traceUri;
	}

	@Override
	public ObselBuilder newObselBuilder(IStoredTrace trace) {
		return new ObselBuilder(this, trace, pojoFactory);
	}

	@Override
	public ObselBuilder newObselBuilder(String storedTraceUri) {
		return new ObselBuilder(this, storedTraceUri, pojoFactory);
	}

	@Override
	public String getRootUri() {
		return dao.getRootUri();
	}

	@Override
	public String newObsel(IStoredTrace trace, String typeUri, long begin,
			Object[] attributes) {

		Set<IAttributePair> att = new HashSet<IAttributePair>();
		if(attributes != null) {
			if(attributes.length%2==1)
				throw new IllegalArgumentException("Must provide an even number of strings.");

			for(int i=0; i<attributes.length; i+=2) {
				Object attTypeObject = attributes[i];
				if(attTypeObject == null)
					throw new IllegalArgumentException("Must provide a non null attribut type");

				IAttributeType attType;
				if(attTypeObject instanceof String)
					attType = this.pojoFactory.createAttributeType((String)attTypeObject);
				else if(attTypeObject instanceof IAttributeType)
					attType = (IAttributeType)attTypeObject;
				else
					throw new IllegalArgumentException("Must provide a String or a IAttributeType as attribute types.");

				Object attValue = attributes[i+1];
				att.add(new AttributePair(attType, attValue));
			}
		}

		return newObsel(trace, typeUri, begin, att);

	}

}
