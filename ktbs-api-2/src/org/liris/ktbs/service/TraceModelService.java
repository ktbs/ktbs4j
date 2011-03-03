package org.liris.ktbs.service;

import java.io.Reader;

import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IRelationType;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;

public interface TraceModelService {
	
	public ITraceModel createTraceModel(String modelUri);
	
	public boolean importModel(String modelUri, boolean override, Reader reader, String mimeType);
	
	public boolean save(ITraceModel model);
	
	public boolean save(ITraceModel model, boolean createIfDoesntExist);

	public IAttributeType newAttributeType(ITraceModel model, String localName, IObselType domain);
	
	public IRelationType newRelationType(ITraceModel model, String localName, IObselType domain, IObselType range);
	
	public IObselType newObselType(ITraceModel model, String localName);

	public IObselType newObselType(ITraceModel model, String localName, IObsel superType);
	
}
