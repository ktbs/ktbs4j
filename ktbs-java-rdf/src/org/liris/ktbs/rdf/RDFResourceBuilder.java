package org.liris.ktbs.rdf;

import java.io.StringWriter;
import java.util.Calendar;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * 
 * @author Damien Cram
 *
 */
public class RDFResourceBuilder {

	private Model jenaModel;
	private String jenaSyntax;
	
	/**
	 * A static constructor that creates a new KTBS resource builder. 
	 * <p>
	 * Please make sure you use a new KTBS resource builder for each resource.
	 * </p>
	 * @param jenaSyntax The RDF syntax when writing the RDF graph to a string. Possible values
	 * are given by the class {@link JenaConstants}. 
	 * @return the created KTBS resource builder
	 */
	public static RDFResourceBuilder newBuilder(String jenaSyntax) {
		return new RDFResourceBuilder(jenaSyntax);
	}
	
	private RDFResourceBuilder(String jenaSyntax) {
		super();
		this.jenaModel = ModelFactory.createDefaultModel();
		this.jenaModel.setNsPrefix("ktbs", KtbsConstants.KTBS_NAMESPACE);
		this.jenaModel.setNsPrefix("rdfs", RDFS.getURI());
		this.jenaSyntax = jenaSyntax;
	}
	
	/**
	 * Adds one or several obsels to the RDF internal (Jena) model, with a URI given as parameter 
	 * the defines the parent trace that contains all the obsels. 
	 * 
	 * <p>
	 * The traceURI parameter has been introduced in case some obsels in the array 
	 * would reference parent traces that are different to each other, which is not supposed 
	 * to be allowed. The parameter traceURI will override the uris of each parent trace that are
	 * obtained by obsel.getTraceURI().
	 * 
	 * </p>
	 * the parent trace
	 * @param traceURI The URI of the parent trace that will contain all the parameter obsels. 
	 * Can be null, in which case the parent trace URI for each obsel is obtained by calling 
	 * obsel.getTraceURI().
	 * @param obsels the obsels to be added in the model
	 */
	public void addObsel(String traceURI, Obsel... obsels) {
		
		for(Obsel obsel:obsels)
			createRDFObsel(traceURI, obsel);
	}

	public String getRDFResourceAsString() {
		StringWriter writer = new StringWriter();
		jenaModel.write(writer, jenaSyntax);
		return writer.toString();
	}

	public String getRDFResourceAsString(String base) {
		StringWriter writer = new StringWriter();
		jenaModel.write(writer, jenaSyntax, base);
		return writer.toString();
	}
	
	public void baseToString(Base base, boolean withParent) {

		if(jenaModel.containsResource(jenaModel.getResource(base.getURI())))
			return;
		
		Resource baseResource = jenaModel.createResource();
			
		if(withParent) {
			Resource rootResource = jenaModel.createResource(base.getKtbsRoot().getURI());
			rootResource.addProperty(jenaModel.createProperty(KtbsConstants.KTBS_HASBASE), baseResource);
		}
		setType(baseResource, KtbsConstants.KTBS_BASE);
		setLabel(base.getURI(), base.getLabel());
	}

	
	public void addTrace(Trace trace, boolean withParent) {

		if(jenaModel.containsResource(jenaModel.getResource(trace.getURI())))
			return;

		Resource traceResource = jenaModel.createResource(trace.getURI());
		
		setType(traceResource, KtbsConstants.KTBS_STOREDTRACE);

		jenaModel.add(
				traceResource, 
				jenaModel.createProperty(KtbsConstants.KTBS_HASMODEL), 
				jenaModel.createResource(trace.getTraceModelUri())
		);

		// sets the trace origin
		Calendar originCal = Calendar.getInstance();
		originCal.setTime(trace.getOrigin());
		jenaModel.addLiteral(
				traceResource, 
				jenaModel.createProperty(KtbsConstants.KTBS_HASORIGIN), 
				jenaModel.createTypedLiteral(new XSDDateTime(originCal))
		);

		for(Obsel obsel:trace.getObsels()) {
			addObsel(trace.getURI(), obsel);
		}

		if(withParent) {
			Resource baseResource = jenaModel.createResource(trace.getBaseURI());
			baseResource.addProperty(jenaModel.createProperty(KtbsConstants.KTBS_OWNS), traceResource);
		}
		
		setLabel(trace.getURI(), trace.getLabel());
	}

	/**
	 * Adds an obsel to the Jena Model. The trace URI defined in the
	 * Jena model for the parent trace is given as parameter. If traceURI parameter is
	 * null, the obsel.getTraceURI() is used.
	 * 
	 * @param traceURI the URI of the trace contained the osbel
	 * @param obsel the obsel to be added in the model
	 */
	private void createRDFObsel(String traceURI, Obsel obsel) {
		
		
		if(jenaModel.containsResource(jenaModel.getResource(obsel.getURI())))
			return;

		Resource obselResource = jenaModel.createResource(obsel.getURI());

		setType(obselResource, obsel.getTypeURI());

		jenaModel.add(
				obselResource, 
				jenaModel.createProperty(KtbsConstants.KTBS_HASTRACE), 
				jenaModel.createResource(traceURI!=null?traceURI:obsel.getTraceURI())
		);

		jenaModel.add(
				obselResource, 
				jenaModel.createProperty(KtbsConstants.KTBS_HASSUBJECT), 
				jenaModel.createLiteral(obsel.getSubject())
		);
		
		// adds the begin date
		Calendar beginCal = Calendar.getInstance();
		beginCal.setTime(obsel.getBegin());
		jenaModel.add(
				obselResource, 
				jenaModel.createProperty(KtbsConstants.KTBS_HASBEGIN_DT), 
				jenaModel.createTypedLiteral(new XSDDateTime(beginCal)));

		// adds the end date
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(obsel.getEnd());
		jenaModel.add(
				obselResource, 
				jenaModel.createProperty(KtbsConstants.KTBS_HASEND_DT), 
				jenaModel.createTypedLiteral(new XSDDateTime(endCal)));

		// add attributes to the Jena model
		for(String att:obsel.getAttributes().keySet()) {
			jenaModel.add(
					obselResource, 
					jenaModel.createProperty(att), 
					jenaModel.createLiteral(obsel.getAttributeValue(att).toString()));

		}

		// add relation to the Jena model
		for(Relation rel:obsel.getOutgoingRelations()) {
			Resource targetResource = null;
			if(rel.getToObsel() != null) {
				addObsel(traceURI, rel.getToObsel());
				jenaModel.getResource(rel.getToObselURI());
			}
			else if(rel.getToObselURI()!=null)
				targetResource = jenaModel.createResource(rel.getToObselURI());
			else
				/*
				 *  should never happen. Relation should have at least one non null
				 *  information about the target obsel (and source obsel too), either
				 *  the java object, or the URI.
				 */		
				continue;
			jenaModel.add(
					obselResource, 
					jenaModel.createProperty(rel.getRelationName()), 
					targetResource);
		}
		
		setLabel(obsel.getURI(), obsel.getLabel());
	}

	public void addTraceModel(String baseURI, String traceModelURI, String label) {
		
		Resource traceModelResource = jenaModel.createResource(traceModelURI);
		jenaModel.add(
				jenaModel.createResource(baseURI),
				jenaModel.createProperty(KtbsConstants.KTBS_OWNS),
				traceModelResource
		);

		setType(traceModelResource, KtbsConstants.KTBS_TRACEMODEL);

		setLabel(traceModelURI, label);
	}

	private void setLabel(String baseURI, String label) {
		jenaModel.add(
				jenaModel.createResource(baseURI),
				RDFS.label,
				jenaModel.createLiteral(label)
		);
	}
	
	
	public void addRelation(String fromObselURI,
			String relationName, String toObselURI) {

		jenaModel.add(
				jenaModel.createResource(fromObselURI),
				jenaModel.createProperty(relationName),
				jenaModel.createResource(toObselURI)
		);
	}

	public void addBase(Base base) {
		if(jenaModel.containsResource(jenaModel.getResource(base.getURI())))
			return;

		Resource baseResource = jenaModel.createResource(base.getURI());

		setType(baseResource,KtbsConstants.KTBS_BASE);
		
		jenaModel.add(
				jenaModel.createResource(base.getKtbsRootURI()),
				jenaModel.createProperty(KtbsConstants.KTBS_HASBASE),
				baseResource);
		
		setLabel(base.getURI(), base.getLabel());
	}

	public void setType(Resource rdfResource, String typeURI) {
		rdfResource.addProperty(
				RDF.type, 
				jenaModel.getResource(typeURI));
	}

}
