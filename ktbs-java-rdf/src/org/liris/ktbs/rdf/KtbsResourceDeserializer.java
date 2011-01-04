package org.liris.ktbs.rdf;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Relation;
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
import com.hp.hpl.jena.shared.BadBooleanException;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Translate a KTBS resource represented in RDF triples into a {@link KtbsResource}.
 * 
 * @author Damien Cram
 * @see RDFResourceSerializer
 */
public class KtbsResourceDeserializer {

	private static Log log = LogFactory.getLog(KtbsResourceDeserializer.class);

	/**
	 * 
	 * Builds a KTBS resource object in the form of a {@link KtbsResource} from its representation
	 * in RDF triples.
	 * 
	 * <p>
	 * The RDF triples are passed to the method as an input stream.
	 * </p>
	 * 
	 * @param ktbsResourceURI the URI of the KTBS resource represented by RDF triples
	 * @param stream the input stream containing the RDF triples of the KTBS resource
	 * @param jenaSyntax the RDF Syntax used in the input stream to represent the KTBS 
	 * resource (see <a href="http://jena.sourceforge.net/IO/iohowto.html">Jena documentation 
	 * about RDF IO</a> and {@link JenaConstants} for possible values of this parameter)
	 * @param ktbsResourceType the Java class of the {@link KtbsResource} instance to return
	 * @param restAspect the aspect of the KTBS resource (e.g. &#64;obsels 
	 * and &#64;about for KTBS traces)
	 * @return the KTBS resource object that has been built from the RDF input stream
	 * @throws InvalidDeserializationRequest when there is in the RDF model no <code>rdf:type</code>
	 * that corresponds to the parameters ktbsResourceType and restAspect.
	 */
	public KtbsResource deserializeFromStream(String ktbsResourceURI, InputStream stream, String jenaSyntax, Class<?> ktbsResourceType, String restAspect) {

		Model jenaModel = ModelFactory.createDefaultModel();

		jenaModel.read(stream, "", jenaSyntax);


		if(restAspect != null && !restAspect.equals(""))
			ktbsResourceURI = ktbsResourceURI.replaceAll(restAspect, "");
		
		checkRDFType(jenaModel,ktbsResourceURI, ktbsResourceType, restAspect);
		
		KtbsResource resource = createResourceFromRDFModel(ktbsResourceURI, jenaModel, ktbsResourceType, restAspect);
		return resource;
	}


	private void checkRDFType(Model jenaModel, String ktbsResourceURI,
			Class<?> ktbsResourceType, String traceAspect) {
		
		String rdfTypeURI = getRDFTypeURI(jenaModel,ktbsResourceURI) ;
		if(rdfTypeURI == null && !Trace.class.isAssignableFrom(ktbsResourceType))
			throw new InvalidDeserializationRequest(ktbsResourceType, traceAspect, null);
		
		if(KtbsRoot.class.isAssignableFrom(ktbsResourceType)) {
			if(!rdfTypeURI.equals(KtbsConstants.KTBS_KTBSROOT))
				throw new InvalidDeserializationRequest(ktbsResourceType, traceAspect, rdfTypeURI, KtbsConstants.KTBS_KTBSROOT);
		} else if(Base.class.isAssignableFrom(ktbsResourceType)) {
			if(!rdfTypeURI.equals(KtbsConstants.KTBS_BASE))
				throw new InvalidDeserializationRequest(ktbsResourceType, traceAspect, rdfTypeURI, KtbsConstants.KTBS_BASE);
		} else if(Trace.class.isAssignableFrom(ktbsResourceType)) {
			if(traceAspect == null)
				throw new InvalidDeserializationRequest(ktbsResourceType, traceAspect, rdfTypeURI, KtbsConstants.KTBS_STOREDTRACE, KtbsConstants.KTBS_COMPUTEDTRACE);
			boolean obselsCase = traceAspect.equals(KtbsConstants.OBSELS_ASPECT) && rdfTypeURI == null;
			boolean aboutCase = traceAspect.equals(KtbsConstants.ABOUT_ASPECT) 
									&& rdfTypeURI != null 
									&& (rdfTypeURI.equals(KtbsConstants.KTBS_STOREDTRACE) 
											|| rdfTypeURI.equals(KtbsConstants.KTBS_COMPUTEDTRACE));
			if(!obselsCase && !aboutCase)
				throw new InvalidDeserializationRequest(ktbsResourceType, traceAspect, rdfTypeURI, KtbsConstants.KTBS_STOREDTRACE, KtbsConstants.KTBS_COMPUTEDTRACE);
		} else if(Obsel.class.isAssignableFrom(ktbsResourceType)) {
			// Can be of any user-defined type
		}
	}

	private String getRDFTypeURI(Model jenaModel, String ktbsResourceURI) {
		Resource rdfType = jenaModel.getResource(ktbsResourceURI).getPropertyResourceValue(RDF.type);
		return rdfType == null ? null : rdfType.getURI();
	}


	public boolean isTraceCompliantWithModel(String traceURI, InputStream stream, String jenaSyntax) {
		Model jenaModel = ModelFactory.createDefaultModel();

		jenaModel.read(stream, "", jenaSyntax);

		Statement stmt = jenaModel.getProperty(
				jenaModel.getResource(traceURI),
				jenaModel.getProperty(KtbsConstants.KTBS_COMPLIES_WITH_MODEL));

		if(stmt == null || stmt.getObject()==null)
			return false;
		String string = stmt.getObject().asLiteral().getString();
		if(string.equals("yes"))
			return true;
		else
			return false;
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

			String baseURI = null;
			StmtIterator it = jenaModel.listStatements(null, jenaModel.getProperty(KtbsConstants.KTBS_OWNS), thisTraceResource);
			if(it.hasNext())
				baseURI = it.nextStatement().getSubject().asResource().getURI();

			if(restAspect != null && restAspect.equals(KtbsConstants.OBSELS_ASPECT)) {
				// This is a @obsels trace request

				StmtIterator obselResourceIt = jenaModel.listStatements(null, hasTraceProperty, thisTraceResource);
				trace = KtbsResourceFactory.createTrace(ktbsResourceURI, null, null, null, baseURI, false);

				Collection<Statement> obselRelationStatements = new LinkedList<Statement>();

				while (obselResourceIt.hasNext()) {
					Statement statement = (Statement) obselResourceIt.next();
					Resource obselResource = statement.getSubject();
					String obselURI = obselResource.getURI();


					Obsel obsel = createObselFromRDFModel(obselURI, jenaModel, obselRelationStatements, false);
					trace.addObsel(obsel);
				}

				for(Statement stmt:obselRelationStatements) {
					KtbsResourceFactory.createRelation(
							trace.getObsel(stmt.getSubject().getURI()),
							stmt.getPredicate().getURI(), 
							trace.getObsel(stmt.getObject().asResource().getURI()));
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
				boolean compliantWithModel = false;
				Property isCompliantProperty = jenaModel.getProperty(KtbsConstants.KTBS_COMPLIES_WITH_MODEL);
				if(isCompliantProperty != null && thisTraceResource.getProperty(isCompliantProperty) != null) {
					try {
						compliantWithModel = thisTraceResource.getProperty(isCompliantProperty).getObject().asLiteral().getBoolean();
					} catch(BadBooleanException e) {
						/*
						 * This is bug #21 of the KTBS server. 
						 * Try reading this property as a string
						 */
						String string = thisTraceResource.getProperty(isCompliantProperty).getObject().asLiteral().getString();
						compliantWithModel = string.equals("yes");
					}
				}
				trace = KtbsResourceFactory.createTrace(ktbsResourceURI, traceModelURI, label, origin, baseURI, compliantWithModel);

			}

			return trace;
		} else if(Obsel.class.isAssignableFrom(ktbsResourceType)) {
			Collection<Statement> relations = new LinkedList<Statement>();
			Obsel obsel = createObselFromRDFModel(ktbsResourceURI, jenaModel, relations, true);

			return obsel;
		} else {
			throw new UnsupportedOperationException("Cannot read a KTBS resource of type " + ktbsResourceType.getCanonicalName()+ ".");
		}

	}


	/**
	 * Creates an instance of {@link Obsel} from a Jena model and extract suspected 
	 * inter-obsels relations in their weak form (if the parameter suspectedRelationStmts 
	 * is null) or in their string form (if the suspectedRelationStmts is an empty collection).
	 * 
	 * @param ktbsResourceURI the uri of the obsel to extract from the model
	 * @param jenaModel the RDF model that contains the obsel description
	 * @param suspectedRelationStmts the collection passed by the caller (must be modifiable !)
	 * to collect relations between obsels.
	 * @param buildWeakRelations true if this method should build weak relations (i.e. where source 
	 * and target obsels in the relation are identified by their URIs only) between obsels when 
	 * it encounters relation statements in jenaModel, false if this method should not 
	 * build these weak relations (meaning that you may want to  collect relation statement 
	 * with the parameter suspectedRelationStmts and build strongly inter-obsels 
	 * relations from this collection of statements).
	 * @return the created obsel
	 */
	private Obsel createObselFromRDFModel(String ktbsResourceURI, 
			Model jenaModel, 
			Collection<Statement> suspectedRelationStmts, 
			boolean buildWeakRelations) {

		if(buildWeakRelations==false) {
			// The caller wants to build string inter-obsel relations
			if(suspectedRelationStmts==null)
				throw new IllegalStateException("You cannot build an obsel from an RDF model with the parameter buildWeakRelations and the collection suspectedRelationStmts that null, or not modifiable");
		}


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
		long begin = -1l;
		long end= -1l;
		String obselURI = null;
		String subject = null;

		Map<String, Object> attributes = new HashMap<String, Object>();
		List<Relation> relations = new LinkedList<Relation>();
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
				begin = statement.getObject().asLiteral().getLong();
			} else if(predicateURI.equals(KtbsConstants.KTBS_HASEND)) {
				end = statement.getObject().asLiteral().getLong();
			} else if(predicateURI.equals(KtbsConstants.KTBS_HASTRACE))
				traceURI = statement.getObject().asResource().getURI();
			else if(predicateURI.equals(KtbsConstants.KTBS_HASSUBJECT))
				subject = statement.getObject().asLiteral().toString();
			else if (traceModelURI != null && predicateURI.startsWith(traceModelURI)) {
				RDFNode object = statement.getObject();
				if(object.isLiteral()) {
					// this is an attribute
					attributes.put(predicateURI, object.asLiteral().getValue());
				} else if(object.isResource()) {
					// this is a relation
					suspectedRelationStmts.add(statement);

					if(buildWeakRelations) {
						Relation relation = KtbsResourceFactory.createRelation(
								obselURI,
								statement.getPredicate().getURI(), 
								statement.getObject().asResource().getURI());
						relations .add(relation);
					}

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

		if(buildWeakRelations) {
			for(Relation relation:relations)
				obsel.addOutgoingRelation(relation);
		}

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
