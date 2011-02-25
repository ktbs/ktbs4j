package org.liris.ktbs.rdf;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.domain.AttributePair;
import org.liris.ktbs.core.domain.AttributeType;
import org.liris.ktbs.core.domain.Base;
import org.liris.ktbs.core.domain.ComputedTrace;
import org.liris.ktbs.core.domain.KtbsResource;
import org.liris.ktbs.core.domain.Method;
import org.liris.ktbs.core.domain.MethodParameter;
import org.liris.ktbs.core.domain.Obsel;
import org.liris.ktbs.core.domain.ObselType;
import org.liris.ktbs.core.domain.PropertyStatement;
import org.liris.ktbs.core.domain.RelationStatement;
import org.liris.ktbs.core.domain.RelationType;
import org.liris.ktbs.core.domain.Root;
import org.liris.ktbs.core.domain.StoredTrace;
import org.liris.ktbs.core.domain.Trace;
import org.liris.ktbs.core.domain.TraceModel;
import org.liris.ktbs.core.domain.WithParametersDelegate;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IMethodParameter;
import org.liris.ktbs.core.domain.interfaces.WithParameters;
import org.liris.ktbs.serial.SerializationOptions;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class Rdf2Java {

	private static final Log log = LogFactory.getLog(Rdf2Java.class);

	private Model model;
	private SerializationOptions options = new SerializationOptions();

	public Rdf2Java(Model model) {
		super();
		this.model = model;
	}

	public Rdf2Java(Model model, SerializationOptions options) {
		super();
		this.model = model;
		this.options = options;
	}

	public IKtbsResource getResource(String uri) {
		alreadyReadResources.clear();
		Class<?> cls = guessType(uri);
		return readResource(uri, cls);
	}

	public IKtbsResource readResource(String uri, Class<?> cls) {
		if(Root.class.isAssignableFrom(cls)) 
			return readRoot(uri);
		else if(Base.class.isAssignableFrom(cls)) 
			return readBase(uri);
		else if(StoredTrace.class.isAssignableFrom(cls)) 
			return readStoredTrace(uri);
		else if(ComputedTrace.class.isAssignableFrom(cls)) 
			return readComputedTrace(uri);
		else if(Obsel.class.isAssignableFrom(cls)) 
			return readObsel(uri);
		else if(ObselType.class.isAssignableFrom(cls)) 
			return readObselType(uri);
		else if(AttributeType.class.isAssignableFrom(cls)) 
			return readAttributeType(uri);
		else if(RelationType.class.isAssignableFrom(cls)) 
			return readRelationType(uri);
		else if(TraceModel.class.isAssignableFrom(cls)) 
			return readTraceModel(uri);
		else if(Method.class.isAssignableFrom(cls)) 
			return readMethod(uri);
		else if(Trace.class.isAssignableFrom(cls)) 
			return readTrace(uri);
		else 
			throw new IllegalStateException("Should never be invoked since specified method are defined for each resource");
	}

	// Read a method
	private Method readMethod(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (Method) alreadyReadResources.get(uri);

		Method method = new Method();
		method.setURI(uri);
		method.setWithMethodParameterDelegate(readResourceWithParameters(method.getUri()));

		// etag
		Object etag = getValue(method.getUri(), KtbsConstants.P_HAS_ETAG);
		if(etag != null)
			method.setEtag((String)etag);

		// inherits
		Object inherits = getValue(method.getUri(), KtbsConstants.P_INHERITS);
		if(inherits != null)
			method.setInherits((String)inherits);

		return method;
	}

	// Read a root
	private Root readRoot(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (Root) alreadyReadResources.get(uri);

		Root root = new Root();
		root.setURI(uri);
		fillResource(root);

		StmtIterator it = model.listStatements(
				model.getResource(uri), 
				model.getProperty(KtbsConstants.P_HAS_BASE), 
				(RDFNode)null);

		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			root.getBases().add(readBase(statement.getObject().asResource().getURI()));
		}

		return root;
	}

	private Base readBase(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (Base) alreadyReadResources.get(uri);

		Base base = new Base();
		base.setURI(uri);
		fillResource(base);

		StmtIterator it = model.listStatements(
				model.getResource(uri), 
				model.getProperty(KtbsConstants.P_OWNS), 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			String uri2 = statement.getResource().getURI();
			if (getRdfType(uri2) == null) {
				log.warn("The base " + uri + " owns a resource of an unknown type: " + uri2);
				continue;
			} else if(getRdfType(uri2).equals(KtbsConstants.TRACE_MODEL)) {
				base.getTraceModels().add(readTraceModel(uri2));
			} else if(getRdfType(uri2).equals(KtbsConstants.STORED_TRACE)) {
				base.getStoredTraces().add(readStoredTrace(uri2));
			} else if(getRdfType(uri2).equals(KtbsConstants.COMPUTED_TRACE)) {
				base.getComputedTraces().add(readComputedTrace(uri2));
			} else if(getRdfType(uri2).equals(KtbsConstants.METHOD)) {
				base.getMethods().add(readMethod(uri2));
			}
		}

		return base;
	}

	private ComputedTrace readComputedTrace(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (ComputedTrace) alreadyReadResources.get(uri);

		ComputedTrace trace = new ComputedTrace();
		trace.setURI(uri);
		fillTrace(trace);
		trace.setWithMethodParameterDelegate(readResourceWithParameters(trace.getUri()));

		StmtIterator it = model.listStatements(
				model.getResource(uri), 
				model.getProperty(KtbsConstants.P_HAS_SOURCE), 
				(RDFNode)null);

		while (it.hasNext()) {
			Statement stmt = it.next();
			trace.getSourceTraces().add(readTrace(stmt.getObject().asResource().getURI()));
		}


		return trace;
	}

	private WithParameters readResourceWithParameters(String uri) {
		WithParameters withParametersPojo = new WithParametersDelegate();

		StmtIterator it = model.listStatements(
				model.getResource(uri), 
				model.getProperty(KtbsConstants.P_HAS_PARAMETER), 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			IMethodParameter methodParameter = KtbsUtils.parseMethodParameter(statement.getObject().asLiteral().getString());
			withParametersPojo.getMethodParameters().add(new MethodParameter(
					methodParameter.getName(),
					methodParameter.getValue()
			));
		}
		return withParametersPojo;
	}

	private Map<String, KtbsResource> alreadyReadResources = new HashMap<String, KtbsResource>();

	private void fillResource(KtbsResource resource) {

		alreadyReadResources.put(resource.getUri(), resource);

		StmtIterator it = model.listStatements(
				model.getResource(resource.getUri()), 
				RDFS.label, 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			resource.getLabels().add(statement.getObject().asLiteral().getString());
		}


		it = model.listStatements(
				model.getResource(resource.getUri()), 
				null, 
				(RDFNode)null);

		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			if(KtbsUtils.hasReservedNamespace(statement.getPredicate().getURI())
			)
				continue;
			else
				resource.getProperties().add(new PropertyStatement(
						getRdfObjectAsJavaObject(statement.getObject()), 
						statement.getPredicate().getURI()));
		}
	}

	private Object getRdfObjectAsJavaObject(RDFNode object) {
		if(object == null)
			return null;
		else if(object.isLiteral())
			return object.asLiteral().getValue();
		else if(object.isResource())
			return object.asResource().getURI();
		return null;
	}

	private void fillTrace(Trace trace) {
		fillResource(trace);

		// the traceformed traces
		StmtIterator it = model.listStatements(
				null, 
				model.getProperty(KtbsConstants.P_HAS_SOURCE), 
				model.getResource(trace.getUri()));
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			trace.getTransformedTraces().add(readComputedTrace(statement.getSubject().getURI()));
		}

		// the obsels
		it = model.listStatements(
				null, 
				model.getProperty(KtbsConstants.P_HAS_TRACE), 
				model.getResource(trace.getUri()));
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			trace.getObsels().add(readObsel(statement.getSubject().asResource().getURI()));
		}

		// origin
		Object origin = getValue(trace.getUri(), KtbsConstants.P_HAS_ORIGIN);
		if(origin != null)
			trace.setOrigin(origin.toString());

		// complies with model
		Object yesNoValue = getValue(trace.getUri(), KtbsConstants.P_COMPLIES_WITH_MODEL);
		if(yesNoValue != null)
			trace.setCompliesWithModel((String)yesNoValue);

		// the trace model
		String traceModelUri = getResourceUri(trace.getUri(), KtbsConstants.P_HAS_MODEL);
		if(traceModelUri != null)
			trace.setTraceModel(readTraceModel(traceModelUri));

	}

	private Obsel readObsel(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (Obsel) alreadyReadResources.get(uri);

		Obsel obsel = new Obsel();
		obsel.setURI(uri);
		fillResource(obsel);

		// the obsel type
		String obselTypeUri = getResourceUri(uri, RDF.type.getURI());
		if(obselTypeUri != null)
			obsel.setObselType(readObselType(obselTypeUri));

		// begin
		Object begin = getValue(obsel.getUri(), KtbsConstants.P_HAS_BEGIN);
		if(begin != null)
			obsel.setBegin(new BigInteger(begin.toString()));

		// end
		Object end = getValue(obsel.getUri(), KtbsConstants.P_HAS_END);
		if(end != null)
			obsel.setEnd(new BigInteger(end.toString()));

		// beginDT
		Object beginDT = getValue(obsel.getUri(), KtbsConstants.P_HAS_BEGIN_DT);
		if(beginDT != null)
			obsel.setBeginDT(beginDT.toString());

		// endDT
		Object endDT = getValue(obsel.getUri(), KtbsConstants.P_HAS_END_DT);
		if(endDT != null)
			obsel.setEndDT(endDT.toString());

		// subject
		Object subject = getValue(obsel.getUri(), KtbsConstants.P_HAS_SUBJECT);
		if(subject!= null)
			obsel.setSubject((String)subject);

		// parent trace
		obsel.setTrace(readTrace(getResourceUri(uri, KtbsConstants.P_HAS_TRACE)));


		// source obsels (transformation source obsels)
		StmtIterator it = model.listStatements(
				model.getResource(obsel.getUri()), 
				model.getProperty(KtbsConstants.P_HAS_SOURCE_OBSEL), 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			obsel.getSourceObsels().add(readObsel(statement.getObject().asResource().getURI()));
		}


		// incoming relations
		Set<Statement> incomingSourceObselStatement = findIncomingSourceObselStatements(obsel.getUri());
		for(Statement stmt:incomingSourceObselStatement) {
			obsel.getIncomingRelations().add(new RelationStatement(
					readObsel(stmt.getSubject().getURI()), 
					readRelationType(stmt.getPredicate().getURI()), 
					obsel));
		}

		// outgoing relations
		Set<Statement> outgoingTargetObselStatements = findOutgoingTargetObselStatements(obsel.getUri());
		for(Statement stmt:outgoingTargetObselStatements) {
			obsel.getOutgoingRelations().add(new RelationStatement(
					obsel, 
					readRelationType(stmt.getPredicate().getURI()),
					readObsel(stmt.getObject().asResource().getURI())));
		}


		// attributes
		Set<Statement> attributeStatements = findAttributeStatements(obsel.getUri());
		for(Statement stmt:attributeStatements) {
			obsel.getAttributePairs().add(new AttributePair(
					readAttributeType(stmt.getPredicate().getURI()),
					stmt.getObject().asLiteral().getValue()
			));
		}

		return obsel;
	}




	/*
	 * Find in the model all the attribute statements of an obsel.
	 * 
	 * Such attribute statements are triple that have the obsel as subject,
	 * whose predicate is not in any KTBS reserved namespace, and whose object is a literal.
	 */
	private Set<Statement> findAttributeStatements(String obselUri) {
		Set<Statement> stmts = new HashSet<Statement>();

		StmtIterator it = model.listStatements(
				model.getResource(obselUri),
				null, 
				(RDFNode)null 
		);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			if(statement.getObject().isLiteral() && !KtbsUtils.hasReservedNamespace(statement.getPredicate().getURI()))
				stmts.add(statement);
		}

		return stmts;
	}

	/*
	 * Find the set of all obsel uris that are the target of 
	 * an inter obsel relation with the source obsel whose uri is given as parameter.
	 * 
	 * Such target obsels are RDF resources (no literal) and whose triple predicate 
	 * in any KTBS reserve namespace.
	 * 
	 */
	private Set<Statement> findOutgoingTargetObselStatements(String obselUri) {
		Set<Statement> targetObselUris = new HashSet<Statement>();

		StmtIterator it = model.listStatements(
				model.getResource(obselUri),
				null, 
				(RDFNode)null 
		);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			if(statement.getObject().isResource() && !KtbsUtils.hasReservedNamespace(statement.getPredicate().getURI()))
				targetObselUris.add(statement);
		}

		return targetObselUris;
	}


	/*
	 * Find the set of all obsel uris that are the source of 
	 * an inter obsel relation with the target whose uri is given as parameter.
	 * 
	 * Such source obsels are RDF resources that are the subject of at least
	 * one triple pointing to the param obsel.
	 * 
	 */
	private Set<Statement> findIncomingSourceObselStatements(String obselUri) {

		Set<Statement> incomingSourceObselUris = new HashSet<Statement>();

		StmtIterator it = model.listStatements(
				null, 
				null, 
				model.getResource(obselUri));
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			incomingSourceObselUris.add(statement);
		}

		return incomingSourceObselUris;

	}

	private AttributeType readAttributeType(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (AttributeType) alreadyReadResources.get(uri);

		AttributeType attType = new AttributeType();
		attType.setURI(uri);
		fillResource(attType);

		// domains
		StmtIterator it = model.listStatements(
				model.getResource(attType.getUri()), 
				model.getProperty(KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN), 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			attType.getDomains().add(readObselType(statement.getObject().asResource().getURI()));
		}
		
		
		// ranges
		it = model.listStatements(
				model.getResource(attType.getUri()), 
				model.getProperty(KtbsConstants.P_HAS_ATTRIBUTE_RANGE), 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			attType.getRanges().add(statement.getObject().asLiteral().getString());
		}
		
		return attType;
	}

	private RelationType readRelationType(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (RelationType) alreadyReadResources.get(uri);

		RelationType relType = new RelationType();
		relType.setURI(uri);
		fillResource(relType);

		// super relation types
		StmtIterator it = model.listStatements(
				model.getResource(relType.getUri()), 
				model.getProperty(KtbsConstants.P_HAS_SUPER_RELATION_TYPE), 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			relType.getSuperRelationTypes().add(readRelationType(statement.getObject().asResource().getURI()));
		}

		// domains
		it = model.listStatements(
				model.getResource(relType.getUri()), 
				model.getProperty(KtbsConstants.P_HAS_RELATION_DOMAIN), 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			relType.getDomains().add(readObselType(statement.getObject().asResource().getURI()));
		}
		
		// domains
		it = model.listStatements(
				model.getResource(relType.getUri()), 
				model.getProperty(KtbsConstants.P_HAS_RELATION_RANGE), 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			relType.getRanges().add(readObselType(statement.getObject().asResource().getURI()));
		}


		return relType;
	}


	private ObselType readObselType(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (ObselType) alreadyReadResources.get(uri);

		ObselType obselType = new ObselType();
		obselType.setURI(uri);
		fillResource(obselType);

		// source obsels (transformation source obsels)
		StmtIterator it = model.listStatements(
				model.getResource(obselType.getUri()), 
				model.getProperty(KtbsConstants.P_HAS_SUPER_OBSEL_TYPE), 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			obselType.getSuperObselTypes().add(readObselType(statement.getObject().asResource().getURI()));
		}


		return obselType;
	}

	/*
	 * Read a trace from the model without knowing in advance if 
	 * its a stored trace or a computed trace
	 */
	private Trace readTrace(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (Trace) alreadyReadResources.get(uri);
		String rdfType = getRdfType(uri);
		if(rdfType == null) {
			Trace trace = new Trace();
			fillTrace(trace);
			return trace;
		} else if(rdfType.equals(KtbsConstants.STORED_TRACE))
			return readStoredTrace(uri);
		else if(rdfType.equals(KtbsConstants.COMPUTED_TRACE))
			return readComputedTrace(uri);
		else 
			throw new IllegalStateException("The resource "+uri+" was expected to be a trace, but is a " + rdfType + " instead.");

	}

	private StoredTrace readStoredTrace(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (StoredTrace) alreadyReadResources.get(uri);

		StoredTrace trace = new StoredTrace();
		trace.setURI(uri);
		fillTrace(trace);

		Object value = getValue(uri, KtbsConstants.P_HAS_SUBJECT);
		if(value != null)
			trace.setDefaultSubject((String) value);

		return trace;
	}

	private TraceModel readTraceModel(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (TraceModel) alreadyReadResources.get(uri);


		TraceModel traceModel = new TraceModel();
		traceModel.setURI(uri);
		fillResource(traceModel);

		StmtIterator it = model.listStatements(
				null,
				RDF.type, 
				(RDFNode)null 
		);
		while (it.hasNext()) {
			Statement stmt = it.next();
			String uri2 = stmt.getSubject().getURI();
			if(!uri2.startsWith(traceModel.getUri()) || uri2.equals(traceModel.getUri()))
				// triple is not a resource of this trace model
				continue;

			String objectResource = stmt.getObject().asResource().getURI();
			if(objectResource.equals(KtbsConstants.RELATION_TYPE))
				traceModel.getRelationTypes().add(readRelationType(uri2));
			else if(objectResource.equals(KtbsConstants.OBSEL_TYPE))
				traceModel.getObselTypes().add(readObselType(uri2));
			else if(objectResource.equals(KtbsConstants.ATTRIBUTE_TYPE))
				traceModel.getAttributeTypes().add(readAttributeType(uri2));
			else
				log.warn("The resource "+uri2+" has the same prefix than the trace model but is of unkown type: " + objectResource);
		}

		return traceModel;
	}

	private String getRdfType(String uri) {
		String type = model.getResource(uri).getPropertyResourceValue(RDF.type).getURI();
		if(type == null)
			return null;
		else 
			return type;
	}

	private String getResourceUri(String uri, String pName) {
		Resource object = model.getResource(uri).getPropertyResourceValue(model.getProperty(pName));
		if(object!=null)
			return object.getURI();
		else
			return null;
	}

	/*
	 * Return null if there is not such property set for that resource
	 */
	private Object getValue(String uri, String pName) {
		Statement stmt = model.getResource(uri).getProperty(model.getProperty(pName));
		if(stmt == null)
			return null;
		RDFNode object = stmt.getObject();
		if(object!=null && object.isLiteral())
			return object.asLiteral().getValue();
		else
			return null;
	}

	private Class<?> guessType(String uri) {
		Class<?> cls = null;

		StmtIterator it = model.listStatements(model.getResource(uri), RDF.type, (RDFNode)null);
		if(!it.hasNext())
			throw new IllegalStateException("There is no rdf:type defined for the resource " + uri);
		if(it.hasNext()) {
			Statement statement = (Statement) it.next();
			String rdfType = statement.getObject().asResource().getURI();
			cls = KtbsUtils.getJavaClass(rdfType);
			if(cls == null) {
				// maybe an obsel
				StmtIterator it2 = model.listStatements(
						model.getResource(uri), 
						model.getProperty(KtbsConstants.P_HAS_TRACE), 
						(RDFNode)null);
				if(!it2.hasNext())
					throw new IllegalStateException("The rdf:type of the resource " + uri + " is neither a ktbs known type nor an obsel type (no ktbs:hasTrace property for that resource)");
				it2.next();
				if(it2.hasNext())
					throw new IllegalStateException("There are more than one ktbs:hasTrace property for the resource " + uri);

				// this is an obsel
				cls = Obsel.class;
			} 
		}

		if(it.hasNext())
			throw new IllegalStateException("There are more than one rdf:type defined for the resource " + uri);

		return cls;
	}
}
