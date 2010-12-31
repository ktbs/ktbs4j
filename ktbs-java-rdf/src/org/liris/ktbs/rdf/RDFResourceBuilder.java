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
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

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
		/*
		 * TODO the following model creation may be a bit long when executed the first time. Investigate why.
		 */
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
	 * @param withObselURI true if the URIs of obsels returned by the method Obsel.getURI() should be put
	 * in the RDF representation of each obsel, null if no obsel URI needs to be specified in the RDF model
	 * @param traceURI The URI of the parent trace that will contain all the parameter obsels. 
	 * Can be null, in which case the parent trace URI for each obsel is obtained by calling 
	 * obsel.getTraceURI().
	 * @param obsels the obsels to be added in the model
	 */
	public void addObsels(String traceURI, boolean withObselURI, Obsel... obsels) {

		for(Obsel obsel:obsels)
			createRDFObsel(traceURI, obsel, withObselURI);
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

	public void addTrace(Trace trace, boolean withParent, boolean withObsels) {

		if(jenaModel.containsResource(jenaModel.getResource(trace.getURI())))
			return;

		Resource traceResource = jenaModel.createResource(trace.getURI());

		setType(traceResource, KtbsConstants.KTBS_STOREDTRACE);

		jenaModel.add(
				traceResource, 
				jenaModel.createProperty(KtbsConstants.KTBS_HASMODEL), 
				jenaModel.createResource(trace.getTraceModelUri())
		);

		/*
		 * TODO Uncomment the followings lines when bug #21 is fixed
		 */
//		jenaModel.add(
//				traceResource, 
//				jenaModel.createProperty(KtbsConstants.KTBS_COMPLIES_WITH_MODEL), 
//				jenaModel.createTypedLiteral(trace.isCompliantWithModel())
//		);
		
		/*
		 * TODO Suppress the followings lines when bug #21 is fixed
		 */
		jenaModel.add(
				traceResource, 
				jenaModel.createProperty(KtbsConstants.KTBS_COMPLIES_WITH_MODEL), 
				trace.isCompliantWithModel()?jenaModel.createLiteral("yes"):jenaModel.createLiteral("no")
		);

		// sets the trace origin
		Calendar originCal = Calendar.getInstance();
		originCal.setTime(trace.getOrigin());
		jenaModel.addLiteral(
				traceResource, 
				jenaModel.createProperty(KtbsConstants.KTBS_HASORIGIN), 
				jenaModel.createTypedLiteral(new XSDDateTime(originCal))
		);

		if(withObsels) {
			for(Obsel obsel:trace.getObsels()) {
				addObsels(trace.getURI(), false, obsel);
			}
		}

		if(withParent) {
			Resource baseResource = jenaModel.createResource(trace.getBaseURI());
			baseResource.addProperty(jenaModel.createProperty(KtbsConstants.KTBS_OWNS), traceResource);
		}

		setLabel(traceResource, trace.getLabel());
	}

	/**
	 * Adds an obsel to the Jena Model. The trace URI defined in the
	 * Jena model for the parent trace is given as parameter. If traceURI parameter is
	 * null, the obsel.getTraceURI() is used.
	 * 
	 * @param traceURI the URI of the trace contained the osbel
	 * @param obsel the obsel to be added in the model
	 * @param withObselURI 
	 */
	private void createRDFObsel(String traceURI, Obsel obsel, boolean withObselURI) {


		/*
		 * The following lines crashes when a target obsel was previously 
		 */
		if(jenaModel.contains(jenaModel.getResource(obsel.getURI()),null,(RDFNode)null))
			return;
		Resource obselResource;
		if(withObselURI)
			obselResource = jenaModel.createResource(obsel.getURI());
		else
			obselResource = jenaModel.createResource();

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

		// adds the beginDT date
		if(obsel.getBeginDT() != null) {
			Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(obsel.getBeginDT());
			jenaModel.add(
					obselResource, 
					jenaModel.createProperty(KtbsConstants.KTBS_HASBEGIN_DT), 
					jenaModel.createTypedLiteral(new XSDDateTime(beginCal)));
		}

		// adds the endDT date
		if(obsel.getEndDT() != null) {
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(obsel.getEndDT());
			jenaModel.add(
					obselResource, 
					jenaModel.createProperty(KtbsConstants.KTBS_HASEND_DT), 
					jenaModel.createTypedLiteral(new XSDDateTime(endCal)));
		}
		
		// adds the begin date
		if(obsel.getBegin() != -1) {
			jenaModel.add(
					obselResource, 
					jenaModel.createProperty(KtbsConstants.KTBS_HASBEGIN), 
					jenaModel.createTypedLiteral(obsel.getBegin(), XSD.integer.getURI()));
		}
		
		// adds the end date
		if(obsel.getEnd() != -1) {
			jenaModel.add(
					obselResource, 
					jenaModel.createProperty(KtbsConstants.KTBS_HASEND), 
					jenaModel.createTypedLiteral(obsel.getEnd(), XSD.integer.getURI()));
		}
		
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
			if(rel.getToObselURI()!=null)
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

		setLabel(obselResource, obsel.getLabel());
	}

	public void addTraceModel(String baseURI, String traceModelURI, String label) {

		Resource traceModelResource = jenaModel.createResource(traceModelURI);
		Resource baseResource = jenaModel.createResource(baseURI);
		jenaModel.add(
				baseResource,
				jenaModel.createProperty(KtbsConstants.KTBS_OWNS),
				traceModelResource
		);

		setType(traceModelResource, KtbsConstants.KTBS_TRACEMODEL);

		setLabel(traceModelResource, label);
	}

	private void setLabel(Resource rdfResource, String label) {
		if(label != null)
			jenaModel.add(
					rdfResource,
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

		setLabel(baseResource, base.getLabel());
	}

	public void setType(Resource rdfResource, String typeURI) {
		rdfResource.addProperty(
				RDF.type, 
				jenaModel.getResource(typeURI));
	}

}
