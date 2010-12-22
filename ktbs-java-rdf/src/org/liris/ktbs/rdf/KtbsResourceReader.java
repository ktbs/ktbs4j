package org.liris.ktbs.rdf;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.impl.KtbsResourceFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class KtbsResourceReader {

	private static Log log = LogFactory.getLog(KtbsResourceReader.class);

	public KtbsResource deserializeFromStream(String ktbsResourceURI, InputStream stream, String jenaSyntax, Class<?> ktbsResourceType, String restAspect) {

		/*
		 * TODO the following model creation may be a bit long when executed the first time. Investigate why.
		 */
		Model jenaModel = ModelFactory.createDefaultModel();

		jenaModel.read(stream, "", jenaSyntax);


		if(restAspect != null && !restAspect.equals(""))
			ktbsResourceURI = ktbsResourceURI.replaceAll(restAspect, "");
		KtbsResource resource = createResourceFromRDFModel(ktbsResourceURI, jenaModel, ktbsResourceType, restAspect);
		return resource;
	}


	private KtbsResource createResourceFromRDFModel(String ktbsResourceURI, Model jenaModel,
			Class<?> ktbsResourceType, String restAspect) {
		if(KtbsRoot.class.isAssignableFrom(ktbsResourceType)) {

			Resource ktbsRootType = jenaModel.getResource(KtbsConstants.KTBS_KTBSROOT);
			StmtIterator it = jenaModel.listStatements(null, RDF.type, ktbsRootType);
			if(ktbsRootType == null || !it.hasNext()) {
				log.warn("There is no statement such as \"? rdf:type ktbs:Ktbs\" in the model.");
				return null;
			}

			Resource ktbsRDFResource = it.nextStatement().getSubject();

			Property hasBaseProperty = jenaModel.getProperty(KtbsConstants.KTBS_HASBASE);
			it = jenaModel.listStatements(ktbsRDFResource, hasBaseProperty, (RDFNode)null);
			Collection<String> baseURIs = new LinkedList<String>();
			while (it.hasNext()) {
				Statement statement = (Statement) it.next();
				baseURIs.add(statement.getObject().asResource().getURI());
			}

			String label = getKtbsResourceLabel(ktbsRDFResource);
			KtbsRoot ktbsRoot = KtbsResourceFactory.createKtbsRoot(
					ktbsRDFResource.getURI(),
					label,
					baseURIs.toArray(new String[baseURIs.size()])
			);

			return ktbsRoot;
		} else if(Base.class.isAssignableFrom(ktbsResourceType)) {
			// the RDF resource that represents this base
			Resource ktbsRDFResource = jenaModel.getResource(ktbsResourceURI);
			if(ktbsRDFResource== null) {
				log.warn("There is no resource with URI \""+ktbsResourceURI+"\" in the retrieved model.");
				return null;
			}


			// retrieves the uri of the KtbsRoot that this base belongs to
			String ktbsRootURI = null;


			/*
			 * 	Retrieves the traces uris and the trace model URIs that are contained in this base.
			 * 
			 *  TODO Should perform a complete check with SPARQL requests using ktbs:owns 
			 *  to ensure that selected resources below are actually owned by this base.
			 */
			Collection<String> traceURIs = new LinkedList<String>();
			Collection<String> traceModelURIs = new LinkedList<String>();

			StmtIterator allStmtIt = jenaModel.listStatements(null, RDF.type, (RDFNode)null);
			while (allStmtIt.hasNext()) {
				Statement s = (Statement) allStmtIt.nextStatement();

				String objectURI = s.getObject().asResource().getURI();
				String resourceURI = s.getSubject().getURI();
				if(objectURI.equals(KtbsConstants.KTBS_STOREDTRACE) || objectURI.equals(KtbsConstants.KTBS_COMPUTEDTRACE)) {
					traceURIs.add(resourceURI);
				} else if(objectURI.equals(KtbsConstants.KTBS_TRACEMODEL)) {
					traceModelURIs.add(resourceURI);
				}
			}


			// creates the base object
			String label = getKtbsResourceLabel(ktbsRDFResource);
			Base base = KtbsResourceFactory.createBase(
					ktbsRDFResource.getURI(),
					ktbsRootURI, 
					label,
					traceURIs.toArray(new String[traceURIs.size()]),
					traceModelURIs.toArray(new String[traceModelURIs.size()])
			);


			return base;

		} else if(Trace.class.isAssignableFrom(ktbsResourceType)) {
			Resource thisTraceResource = jenaModel.getResource(ktbsResourceURI);
			Property hasTraceProperty = jenaModel.getProperty(KtbsConstants.KTBS_HASTRACE);

			Trace trace = null;


			if(restAspect != null && restAspect.equals(KtbsConstants.OBSELS_ASPECT)) {
				// This is a @obsels trace request

				StmtIterator obselResourceIt = jenaModel.listStatements(null, hasTraceProperty, thisTraceResource);
				trace = KtbsResourceFactory.createTrace(ktbsResourceURI, null, null, null, null);

				Collection<Statement> obselRelationStatements = new LinkedList<Statement>();

				while (obselResourceIt.hasNext()) {
					Statement statement = (Statement) obselResourceIt.next();
					Resource obselResource = statement.getSubject();
					String obselURI = obselResource.getURI();


					Obsel obsel = createObselFromRDFModel(obselURI, jenaModel, obselRelationStatements);
					trace.addObsel(obsel);
				}

				for(Statement stmt:obselRelationStatements) {
					KtbsResourceFactory.createRelation(
							stmt.getSubject().getURI(),
							stmt.getPredicate().getURI(), 
							stmt.getObject().asResource().getURI());
				}

			} else if(restAspect != null && restAspect.equals(KtbsConstants.ABOUT_ASPECT)) {
				// This is a @about trace request
				Property hasTraceModelProperty = jenaModel.getProperty(KtbsConstants.KTBS_HASMODEL);
				String traceModelURI = null;
				if(hasTraceProperty==null || thisTraceResource.getProperty(hasTraceModelProperty) == null) {
					log.warn("There is no \""+KtbsConstants.KTBS_HASMODEL+"\" property for the trace "+ktbsResourceURI+".");
				} else {
					traceModelURI = thisTraceResource.getProperty(hasTraceModelProperty).getObject().asResource().getURI();
				}
				Property hasOrigin = jenaModel.getProperty(KtbsConstants.KTBS_HASORIGIN);
				Date origin = null;
				if(hasOrigin == null || thisTraceResource.getProperty(hasOrigin) == null) {
					log.warn("There is no \""+KtbsConstants.KTBS_HASORIGIN+"\" property for the trace "+ktbsResourceURI+".");
				} else {
					origin = ((XSDDateTime)thisTraceResource.getProperty(hasOrigin).getObject().asLiteral().getValue()).asCalendar().getTime();
				}
				String label = getKtbsResourceLabel(thisTraceResource);
				trace = KtbsResourceFactory.createTrace(ktbsResourceURI, traceModelURI, label, origin, null);

			}

			return trace;
		} else if(Obsel.class.isAssignableFrom(ktbsResourceType)) {
			Collection<Statement> relations = new LinkedList<Statement>();
			Obsel obsel = createObselFromRDFModel(ktbsResourceURI, jenaModel, relations);

			return obsel;
		} else {
			throw new UnsupportedOperationException("Cannot read a KTBS resource of type " + ktbsResourceType.getCanonicalName()+ ".");
		}

	}


	/**
	 * Creates an instance of {@link Obsel} from a Jena model and extract suspected inter-obsels relations.
	 * 
	 * @param ktbsResourceURI the uri of the obsel to extract from the model
	 * @param jenaModel the RDF model that contains the obsel description
	 * @param suspectedRelations the collection passed by the caller (must be modifiable !)
	 * to collect relations between obsels
	 * @return
	 */
	private Obsel createObselFromRDFModel(String ktbsResourceURI, Model jenaModel, Collection<Statement> suspectedRelations) {
		Resource obselResource = jenaModel.getResource(ktbsResourceURI);

		String typeURI = null;

		String traceModelURI = null;

		Statement typeStatement = obselResource.getProperty(RDF.type);
		if(typeStatement == null)
			log.warn("There is no type defined for the obsel \""+ktbsResourceURI+"\".");
		else {
			Resource typeResource = typeStatement.getObject().asResource();
			typeURI = typeResource.getURI();
			traceModelURI = typeResource.getNameSpace();
		}


		StmtIterator it = jenaModel.listStatements(obselResource, null, (RDFNode)null);
		String traceURI = null;
		Date beginDT = null;
		Date endDT= null;
		int begin = -1;
		int end= -1;
		String obselURI = null;
		String subject = null;

		Map<String, Serializable> attributes = new HashMap<String, Serializable>();
		while(it.hasNext()) {
			Statement statement = (Statement) it.next();
			obselURI = statement.getSubject().getURI();
			String predicateURI = statement.getPredicate().getURI();
			if(predicateURI.equals(KtbsConstants.KTBS_HASBEGIN_DT)) {
				XSDDateTime beginXSD = (XSDDateTime) statement.getObject().asLiteral().getValue();
				beginDT = beginXSD.asCalendar().getTime();
			} else if(predicateURI.equals(KtbsConstants.KTBS_HASEND_DT)) {
				XSDDateTime endXSD = (XSDDateTime) statement.getObject().asLiteral().getValue();
				endDT = endXSD.asCalendar().getTime();
			} else if(predicateURI.equals(KtbsConstants.KTBS_HASBEGIN)) {
				//TODO Attention !!! Changer ceci en getLong() après modification du bug 19
				begin = statement.getObject().asLiteral().getInt();
			} else if(predicateURI.equals(KtbsConstants.KTBS_HASEND)) {
				//TODO Attention !!! Changer ceci en getLong() après modification du bug 19
				end = statement.getObject().asLiteral().getInt();
			} else if(predicateURI.equals(KtbsConstants.KTBS_HASTRACE))
				traceURI = statement.getObject().asResource().getURI();
			else if(predicateURI.equals(KtbsConstants.KTBS_HASSUBJECT))
				subject = statement.getObject().asLiteral().toString();
			else if (traceModelURI != null && predicateURI.startsWith(traceModelURI)) {
				RDFNode object = statement.getObject();
				if(object.isLiteral()) {
					// this is an attribute
					attributes.put(predicateURI, (Serializable)object.asLiteral().getValue());
				} else if(object.isResource()) {
					// this is a relation
					suspectedRelations.add(statement);
				} else {
					log.warn("Unknown type of object in statement " + statement +".");
				}
			}
		}


		String label = getKtbsResourceLabel(obselResource);
		Obsel obsel = KtbsResourceFactory.createObsel(
				obselURI, 
				traceURI, 
				subject, 
				beginDT, 
				endDT, 
				begin, 
				end, 
				typeURI, 
				attributes,
				label);


		return obsel;
	}

	private String getKtbsResourceLabel(Resource ktbsRDFResource) {
		Statement st = ktbsRDFResource.getProperty(RDFS.label);
		if(st!=null && st.getObject() != null)
			return st.getString();
		else
			return null;
	}


	public static void loadJenaClasses() {
		/*
		 * Loads Jena classes required for model creation.
		 */
		ModelFactory.createDefaultModel();
	}

}
