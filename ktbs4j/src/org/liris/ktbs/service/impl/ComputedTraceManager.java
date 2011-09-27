package org.liris.ktbs.service.impl;

import java.util.Collection;

import org.liris.ktbs.client.KtbsConstants;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.dao.rest.KtbsResponse;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.service.ComputedTraceService;
import org.liris.ktbs.service.IRootAwareService;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.ResponseAwareService;
import org.liris.ktbs.utils.KtbsUtils;

public class ComputedTraceManager implements ComputedTraceService, ResponseAwareService, IRootAwareService {

	private boolean lastDelegatedToResourceManager = false;
	@Override
	public KtbsResponse getLastResponse() {
		if(lastDelegatedToResourceManager)
			return resourceService.getLastResponse();
		return dao.getLastResponse();
	}

	private ResourceService resourceService;
	private ResourceDao dao;

	public ComputedTraceManager(ResourceService resourceService, ResourceDao dao, PojoFactory pojoFactory) {
		super();
		this.resourceService = resourceService;
		this.dao = dao;
	}

	

	@Override
	public boolean saveDescription(IComputedTrace trace) {
		this.lastDelegatedToResourceManager = false;

		return dao.save(trace, false);
	}

	@Override
	public Collection<IObsel> listObsels(IComputedTrace computedTrace, long begin, long end) {
		String request = KtbsUtils.addAspect(computedTrace.getUri(), KtbsConstants.OBSELS_ASPECT, KtbsConstants.ABOUT_ASPECT) ;
		request+="?";
		request+="minb="+begin;
		request+="&";
		request+="maxe="+end;
		this.lastDelegatedToResourceManager = false;

		return dao.query(request, IObsel.class);
	}

	@Override
	public Collection<IObsel> listObsels(IComputedTrace computedTrace, long minb, long maxb, long mine,
			long maxe) {
		String request = KtbsUtils.addAspect(computedTrace.getUri(), KtbsConstants.OBSELS_ASPECT, KtbsConstants.ABOUT_ASPECT) ;
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

	@Override
	public String getRootUri() {
		return dao.getRootUri();
	}
}
