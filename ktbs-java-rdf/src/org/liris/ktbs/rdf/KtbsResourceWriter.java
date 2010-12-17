package org.liris.ktbs.rdf;

import java.io.StringWriter;
import java.util.Calendar;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class KtbsResourceWriter {

	private KtbsResource ktbsResource;

	public KtbsResourceWriter(KtbsResource ktbsResource) {
		super();
		this.ktbsResource = ktbsResource;
	}

	public String serializeToString(String jenaSyntax, boolean withParent) {
		Model jenaModel = ModelFactory.createDefaultModel();

		KtbsResourceWriter.addResourceToModel(ktbsResource, jenaModel, withParent);

		StringWriter writer = new StringWriter();

		jenaModel.write(writer, jenaSyntax);

		return writer.toString();
	}

	/**
	 * Transforms a {@link KtbsResource} into a RDF {@link Resource} and add it to
	 * a Jena model, or returns the RDF resource if it already exists in the Jena model.
	 * @param ktbsResource the KTBS resource to be transformed
	 * @param jenaModel the model that will contain the created RDF resource
	 * @param withParent a flag that indicates if parents of the KTBS resource should be 
	 * recursively added to the model 
	 * @return the created RDF resource that represents the KTBS resource
	 */
	private static Resource addResourceToModel(KtbsResource ktbsResource, Model jenaModel, boolean withParent) {

		if(jenaModel.containsResource(jenaModel.getResource(ktbsResource.getURI())))
			return jenaModel.getResource(ktbsResource.getURI());

		Resource rdfResource = jenaModel.createResource(ktbsResource.getURI());

		if (ktbsResource instanceof Trace) {
			Trace trace = (Trace) ktbsResource;

			rdfResource.addProperty(RDF.type, jenaModel.getResource(KtbsConstants.KTBS_STOREDTRACE));

			jenaModel.add(
					rdfResource, 
					jenaModel.createProperty(KtbsConstants.KTBS_HASMODEL), 
					jenaModel.createResource(trace.getTraceModelUri())
			);

			// sets the trace origin
			Calendar originCal = Calendar.getInstance();
			originCal.setTime(trace.getOrigin());
			jenaModel.addLiteral(
					rdfResource, 
					jenaModel.createProperty(KtbsConstants.KTBS_HASORIGIN), 
					jenaModel.createTypedLiteral(new XSDDateTime(originCal))
			);

			for(Obsel obsel:trace.getObsels()) {
				addResourceToModel(obsel, jenaModel, false);
			}

			return rdfResource;
		} else if (ktbsResource instanceof Obsel) {
			Obsel obsel = (Obsel) ktbsResource;

			rdfResource.addProperty(RDF.type, jenaModel.getResource(obsel.getTypeURI()));

			jenaModel.add(
					rdfResource, 
					jenaModel.createProperty(KtbsConstants.KTBS_HASTRACE), 
					jenaModel.createResource(obsel.getTraceURI())
			);

			// adds the begin date
			Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(obsel.getBegin());
			jenaModel.add(
					rdfResource, 
					jenaModel.createProperty(KtbsConstants.KTBS_HASBEGIN_DT), 
					jenaModel.createTypedLiteral(new XSDDateTime(beginCal)));

			// adds the end date
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(obsel.getEnd());
			jenaModel.add(
					rdfResource, 
					jenaModel.createProperty(KtbsConstants.KTBS_HASEND_DT), 
					jenaModel.createTypedLiteral(new XSDDateTime(endCal)));

			// add attributes to the Jena model
			for(String att:obsel.getAttributes().keySet()) {
				jenaModel.add(
						rdfResource, 
						jenaModel.createProperty(att), 
						jenaModel.createLiteral(obsel.getAttributeValue(att).toString()));

			}

			// add relation to the Jena model
			for(Relation rel:obsel.getOutgoingRelations()) {
				Resource targetResource = null;
				if(rel.getToObsel() != null)
					targetResource = addResourceToModel(rel.getToObsel(), jenaModel,false);
				else if(rel.getToObselURI()!=null)
					targetResource = jenaModel.createResource(rel.getToObselURI());
				else
					/*
					 *  should nerver happen. Relation should have at least one non null
					 *  information about the target obsel (and source obsel too), either
					 *  the java object, or the URI.
					 */		
					continue;
				jenaModel.add(
						rdfResource, 
						jenaModel.createProperty(rel.getRelationName()), 
						targetResource);
			}

			return rdfResource;
		} else if (ktbsResource instanceof Base) {
			Base base = (Base) ktbsResource;

			if(withParent) {
				Resource rootResource = jenaModel.createResource(base.getKtbsRoot().getURI());
				rootResource.addProperty(jenaModel.createProperty(KtbsConstants.KTBS_HASBASE), rdfResource);
			}
			rdfResource.addProperty(RDF.type, jenaModel.getResource(KtbsConstants.KTBS_BASE));

			return rdfResource;
		} else if (ktbsResource instanceof KtbsRoot) {
			rdfResource.addProperty(RDF.type, jenaModel.getResource(KtbsConstants.KTBS_KTBSROOT));

			return rdfResource;
		} else {
			throw new UnsupportedOperationException("Cannot serialize the resource \""+ktbsResource.getURI()+"\".");
		}
	}

	public static String relationToString(String fromObselURI,
			String relationName, String toObselURI, String jenaSyntax) {
		Model jenaModel = ModelFactory.createDefaultModel();


		jenaModel.add(
				jenaModel.createResource(fromObselURI),
				jenaModel.createProperty(relationName),
				jenaModel.createResource(toObselURI)
		);

		StringWriter writer = new StringWriter();
		jenaModel.write(writer, jenaSyntax);

		return writer.toString();
	}
}
