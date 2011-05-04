package org.liris.ktbs.service.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.client.KtbsConstants;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.domain.Obsel;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.IAttributePair;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.StoredTraceService;
import org.liris.ktbs.utils.KtbsUtils;

public class StoredTraceManager extends RootAwareService implements StoredTraceService {

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

	private Map<String, IStoredTrace> bufferedTraces = new HashMap<String, IStoredTrace>();
	private Map<String, Deque<IObsel>> bufferedTraceObsels = new HashMap<String, Deque<IObsel>>();


	@Override
	public void startBufferedCollect(IStoredTrace trace) {
		if(bufferedTraces.containsKey(trace.getUri()))
			throw new RuntimeException("The trace " + trace.getUri() + " is already under a buffered collect");
		else {
			bufferedTraces.put(trace.getUri(), trace);
			bufferedTraceObsels.put(trace.getUri(), new LinkedList<IObsel>());
		}
	}

	@Override
	public void postBufferedObsels(IStoredTrace trace) {
		if(!bufferedTraces.containsKey(trace.getUri()))
			throw new RuntimeException("The trace " + trace.getUri() + " is not under a buffered collect");
		else {
			bufferedTraces.remove(trace.getUri());
			Deque<IObsel> obsels = bufferedTraceObsels.remove(trace.getUri());

			// TODO to be improved when multiple post is allowed
			for(IObsel obsel:obsels) {
				if(obsel.getParentResource() == null)
					((Obsel)obsel).setParentResource(trace);
				dao.createAndGet(obsel);
			}
		}
	}

	@Override
	public boolean saveDescription(IStoredTrace trace) {
		return dao.save(trace, false);
	}

	@Override
	public boolean saveObsels(IStoredTrace trace) {
		String uriWithAspect = KtbsUtils.addAspect(trace.getUri(), KtbsConstants.OBSELS_ASPECT, KtbsConstants.ABOUT_ASPECT);
		return dao.saveCollection(uriWithAspect, trace.getObsels());
	}

	@Override
	public IObsel newObsel(IStoredTrace storedTrace, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Set<IAttributePair> attributes) {
		if(bufferedTraces.containsKey(storedTrace.getUri())) {
			IObsel obsel = factory.createObsel(
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
			return obsel;
		} else {
			String obselUri = createObsel(storedTrace, obselLocalName, typeUri, beginDT, endDT,
					begin, end, subject, attributes);
			if(obselUri != null)
				return resourceService.getResource(obselUri, IObsel.class);
			else
				return null;
		}
	}

	String createObsel(IStoredTrace storedTrace, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Set<IAttributePair> attributes) {

		if(begin != null && end == null)
			end = begin;

		if(beginDT != null && endDT == null)
			endDT = beginDT;

		return resourceService.newObsel(
				storedTrace.getUri(), 
				obselLocalName, 
				typeUri,
				beginDT, 
				endDT, 
				begin, 
				end, 
				subject, 
				attributes);
	}

	@Override
	public IObsel newObsel(IStoredTrace storedTrace, String typeUri,
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
	public IObsel newObsel(IStoredTrace storedTrace, String typeUri, long begin) {
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
			name = KtbsUtils.makeChildURI(baseUri,"storedTrace" + cnt++, false);
		while(uris.contains(name));

		return name;
	}

	@Override
	public IStoredTrace newStoredTrace(String baseUri, String traceModelUri,
			String defaultSubject) {

		String traceUri = resourceService.newStoredTrace(baseUri, 
						generateTraceId(baseUri), 
						traceModelUri, 
						KtbsUtils.now(), 
						defaultSubject);
		if(traceUri == null)
			return null;
		else
			return resourceService.getStoredTrace(traceUri);
	}

	@Override
	public ObselBuilder newObselBuilder(IStoredTrace trace) {
		return new ObselBuilder(this, trace, factory);
	}
}