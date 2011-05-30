package org.liris.ktbs.service;

import java.io.Reader;

import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.ITraceModel;

/**
 * Provides convenient methods for handling trace models in a KTBS.
 * 
 * @author Damien Cram
 * @see ResourceService
 */
public interface TraceModelService extends IRootAwareService {

	/**
	 * Creates a trace model in the KTBS and returns it.
	 * 
	 * @param modelUri the trace model uri (either absolute or relative to the root)
	 * @return the created trace model, null if none created
	 */
	public ITraceModel createTraceModel(String modelUri);
	
	/**
	 * Reads the trace model resources that are contained in a reader and 
	 * creates them in the KTBS.
	 * 
	 * @param modelUri the uri of trace model (either absolute or relative to the root)
	 * @param override true if should overwrite the trace model resources that are already present in the KTBS   
	 * @param reader the reader where the trace model resources can be read from
	 * @param mimeType the mime type of the RDF syntax of the serialized RDF triples in the reader
	 * @return true if all resources have been imported successfully, false otherwise
	 */
	public boolean importModel(String modelUri, boolean override, Reader reader, String mimeType);
	
	/**
	 * Saves a trace model that has been changed locally or 
	 * whose contained resources (of types {@link IAttributeType}, 
	 * {@link IRelationType}, and {@link IObselType}) has been changed locally.
	 * 
	 * 
	 * @param model the trace model object to save in the KTBS
	 * @return true if the trace model has been saved in the KTBS successfully
	 */
	public boolean save(ITraceModel model);
	
	/**
	 * Saves a trace model that has been changed locally or 
	 * whose contained resources (of types {@link IAttributeType}, 
	 * {@link IRelationType}, and {@link IObselType}) has been changed locally.
	 * 
	 * @param model the trace model object to save in the KTBS
	 * @param createIfDoesntExist true if the trace model should be created in 
	 * the KTBS in case it does not already exists
	 * @return true if the trace model has been saved in the KTBS successfully
	 */
	public boolean save(ITraceModel model, boolean createIfDoesntExist);

	/**
	 * Creates a new attribute type in the KTBS and returns it.
	 * 
	 * @param model the uri of the parent trace model (either absolute or relative to the root)
	 * @param localName the name of the attribute type (relative to the trace model uri)
	 * @param domain the domain of the attribute type
	 * @return the created attribute type, null if none has been created
	 */
	public IAttributeType newAttributeType(ITraceModel model, String localName, IObselType domain);
	
	/**
	 * Creates a new relation type in the KTBS and returns it.
	 * 
	 * @param model the uri of the parent trace model (either absolute or relative to the root)
	 * @param localName the name of the relation type (relative to the trace model uri)
	 * @param domain the domain of the relation type
	 * @param range the range of the relation type
	 * @return the created relation type, null if none has been created
	 */
	public IRelationType newRelationType(ITraceModel model, String localName, IObselType domain, IObselType range);
	
	/**
	 * Creates a new obsel type in the KTBS and returns it.
	 * 
	 * @param model the uri of the parent trace model (either absolute or relative to the root)
	 * @param localName the name of the obsel type (relative to the trace model uri)
	 * @return the created obsel type, null if none has been created
	 */
	public IObselType newObselType(ITraceModel model, String localName);

	/**
	 * Creates a new obsel type in the KTBS and returns it.
	 * 
	 * @param model the uri of the parent trace model (either absolute or relative to the root)
	 * @param localName the name of the obsel type (relative to the trace model uri)
	 * @param superType the uri of the super obsel type if any, null otherwise
	 * @return the created obsel type, null if none has been created
	 */
	public IObselType newObselType(ITraceModel model, String localName, IObsel superType);
	
}
