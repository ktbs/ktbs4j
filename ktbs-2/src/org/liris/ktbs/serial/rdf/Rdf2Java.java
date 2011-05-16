package org.liris.ktbs.serial.rdf;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.client.KtbsConstants;
import org.liris.ktbs.dao.ProxyFactory;
import org.liris.ktbs.dao.ResultSet;
import org.liris.ktbs.domain.AttributePair;
import org.liris.ktbs.domain.AttributeType;
import org.liris.ktbs.domain.Base;
import org.liris.ktbs.domain.ComputedTrace;
import org.liris.ktbs.domain.KtbsResource;
import org.liris.ktbs.domain.Method;
import org.liris.ktbs.domain.MethodParameter;
import org.liris.ktbs.domain.Obsel;
import org.liris.ktbs.domain.ObselType;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.PropertyStatement;
import org.liris.ktbs.domain.RelationStatement;
import org.liris.ktbs.domain.RelationType;
import org.liris.ktbs.domain.Root;
import org.liris.ktbs.domain.StoredTrace;
import org.liris.ktbs.domain.Trace;
import org.liris.ktbs.domain.TraceModel;
import org.liris.ktbs.domain.WithParametersDelegate;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IMethodParameter;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationStatement;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.domain.interfaces.WithParameters;
import org.liris.ktbs.serial.DeserializationConfig;
import org.liris.ktbs.serial.DeserializationMode;
import org.liris.ktbs.serial.LinkAxis;
import org.liris.ktbs.utils.KtbsUtils;
import org.liris.ktbs.utils.ThreeKeyedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;



public class Rdf2Java {

	private static final Logger logger = LoggerFactory.getLogger(Rdf2Java.class);

	private ProxyFactory proxyFactory;
	private PojoFactory pojoFactory;

	private DeserializationConfig config;

	private Model model;
	private Map<String, IKtbsResource> alreadyReadResources = new HashMap<String, IKtbsResource>();

	public Rdf2Java(Model model, DeserializationConfig config, PojoFactory pojoFactory, ProxyFactory proxyFactory) {
		super();
		this.model = model;
		this.config = config;
		this.pojoFactory = pojoFactory;
		this.proxyFactory = proxyFactory;
	}

	public <T extends IKtbsResource> T getResource(String uri, Class<T> cls) {
		reset();
		return readResource(uri, cls);
	}

	private void reset() {
		alreadyReadResources.clear();
		obselRelations.clear();
	}

	public IKtbsResource getResource(String uri) {
		reset();
		Class<? extends IKtbsResource> cls = KtbsUtils.guessResourceType(model,uri);
		return readResource(uri, cls);
	}

	public <T extends IKtbsResource> ResultSet<T> getResourceSet(Class<T> cls) {
		reset();
		return readAllResources(cls);
	}

	private <T extends IKtbsResource> ResultSet<T> readAllResources(Class<T> cls) {
		ResultSet<T> resultSet = new ResultSet<T>();

		Set<String> uris = guessResultUris(cls);

		for(String uri:uris) {
			resultSet.add(readResource(uri, cls));
		}

		return resultSet;
	}

	private Set<String> guessResultUris(Class<?> cls) {

		Set<String> uris = new HashSet<String>();

		String rdfType = KtbsUtils.getRDFType(cls);

		StmtIterator it;
		if(rdfType == null) {
			// we assume this is an obsel
			it = model.listStatements(null, model.getProperty(KtbsConstants.P_HAS_TRACE), (RDFNode)null);
		} else {
			it = model.listStatements(null, RDF.type, model.getResource(rdfType));
		}

		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			uris.add(statement.getSubject().getURI());
		}

		return uris;
	}

	@SuppressWarnings("unchecked")
	private <T extends IKtbsResource> T readResource(String uri, Class<T> cls) {
		if(IRoot.class.isAssignableFrom(cls)) 
			return (T) readRoot(uri);
		else if(IBase.class.isAssignableFrom(cls)) 
			return (T) readBase(uri);
		else if(IStoredTrace.class.isAssignableFrom(cls)) 
			return (T) readStoredTrace(uri);
		else if(IComputedTrace.class.isAssignableFrom(cls)) 
			return (T) readComputedTrace(uri);
		else if(IObsel.class.isAssignableFrom(cls)) 
			return (T) readObsel(uri);
		else if(IObselType.class.isAssignableFrom(cls)) 
			return (T) readObselType(uri);
		else if(IAttributeType.class.isAssignableFrom(cls)) 
			return (T) readAttributeType(uri);
		else if(IRelationType.class.isAssignableFrom(cls)) 
			return (T) readRelationType(uri);
		else if(ITraceModel.class.isAssignableFrom(cls)) 
			return (T) readTraceModel(uri);
		else if(IMethod.class.isAssignableFrom(cls)) 
			return (T) readMethod(uri);
		else if(ITrace.class.isAssignableFrom(cls)) 
			return (T) readTrace(uri);
		else 
			throw new IllegalStateException("Should never be invoked since specified method are defined for each resource");
	}

	// Read a method
	private IMethod readMethod(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (Method) alreadyReadResources.get(uri);

		Method method = new Method();
		method.setUri(uri);
		method.setWithMethodParameterDelegate(readResourceWithParameters(method.getUri()));

		method.setEtag(getLiteralOrNull(method.getUri(), KtbsConstants.P_HAS_ETAG, String.class));

		Statement stmt = model.getResource(method.getUri()).getProperty(model.getProperty(KtbsConstants.P_INHERITS));
		if(stmt != null)
			method.setInherits(getRdfObjectAsJavaObject(stmt.getObject()).toString());

		((KtbsResource)method).setParentResource(readParent(method, KtbsConstants.P_CONTAINS, true, IBase.class));

		return method;
	}

	// Read a root
	private IRoot readRoot(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (Root) alreadyReadResources.get(uri);

		IRoot root = pojoFactory.createResource(uri, IRoot.class);

		fillGenericResource(root);
		fillChildren(false, uri, KtbsConstants.P_HAS_BASE, root.getBases(), false, IBase.class);

		return root;
	}

	private <T extends IKtbsResource> void fillChildren(
			boolean asBaseChildren, 
			String parentUri, 
			String parentChildRelation, 
			Set<T> childrenSet, 
			boolean inverse, 
			Class<T> cls) {
		// Never invoked on a trace model, so the parent-child relation name should never be null


		if(config.getMode(LinkAxis.CHILD) == DeserializationMode.NULL)
			return;

		StmtIterator it;
		if(inverse) {
			it = model.listStatements(
					null, 
					model.getProperty(parentChildRelation), 
					model.getResource(parentUri)
			);
		} else {
			it = model.listStatements(
					model.getResource(parentUri), 
					model.getProperty(parentChildRelation), 
					(RDFNode)null);
		}

		Set<String> childrenUris = new HashSet<String>();

		while (it.hasNext()) {
			Statement statement = (Statement) it.next();

			String childUri = inverse?
					statement.getSubject().getURI():
						statement.getObject().asResource().getURI();

					if(asBaseChildren) {
						// must check the child type first
						Statement typeStmt = model.getResource(childUri).getProperty(RDF.type);
						if(typeStmt != null && typeStmt.getObject().asResource().getURI().equals(KtbsUtils.getRDFType(cls)))
							childrenUris.add(childUri);	
					} else 
						childrenUris.add(childUri);	
		}

		for(String childUri:childrenUris) {
			T resource = null;
			if(config.getMode(LinkAxis.CHILD) == DeserializationMode.PROXY) {
				resource = proxyFactory.createResource(childUri, cls);
			} else if(config.getMode(LinkAxis.CHILD) == DeserializationMode.URI_IN_PLAIN) {
				resource = pojoFactory.createResource(childUri, cls);
			} else if(config.getMode(LinkAxis.CHILD) == DeserializationMode.CASCADE) {
				resource = readResource(childUri, cls);
			}

			childrenSet.add(resource);
		}
	}


	private <T extends IKtbsResource> T readLinkedResource(String uri, String pName, boolean inverse, Class<T> cls) {
		LinkAxis axis = LinkAxis.LINKED;
		return readLinkedResourceOnAxis(uri, pName, inverse, cls, axis);
	}

	private <T extends IKtbsResource> Set<T> readLinkedResourceSet(String uri, String pName,
			boolean inverse, Class<T> cls) {
		return readLinkedResourceSetOnAxis(uri, pName, inverse, cls, LinkAxis.LINKED, null);
	}

	private <T extends IKtbsResource> Set<T> readLinkedResourceSetSameType(String uri, String pName,
			boolean inverse, Class<T> cls) {
		return readLinkedResourceSetOnAxis(uri, pName, inverse, cls, LinkAxis.LINKED_SAME_TYPE, null);
	}

	private <T extends IKtbsResource> Set<T> readLinkedResourceSetOnAxis(String uri, String pName,
			boolean inverse, Class<T> cls, LinkAxis axis, String resourceSetQuery) {

		if(config.getMode(axis) == DeserializationMode.NULL)
			return null;

		Set<String> linkedResourceUris = new HashSet<String>();
		if(inverse) {
			StmtIterator it = model.listStatements(null, model.getProperty(pName), model.getResource(uri));
			while(it.hasNext()) {
				linkedResourceUris.add(it.next().getSubject().getURI());
			}
		} else {
			StmtIterator it = model.listStatements(model.getResource(uri), model.getProperty(pName), (RDFNode)null);
			while(it.hasNext()) {
				linkedResourceUris.add(it.next().getObject().asResource().getURI());
			}
		}

		
		Set<T> resourceSet;
		if(resourceSetQuery != null) {
			logger.debug("Creating a ResourceSetProxy, query: {}, class: {}", resourceSetQuery, cls);
			return proxyFactory.createResourceSetProxy(resourceSetQuery, cls);
		} else {
			// Creates a naive resource set
		resourceSet = new HashSet<T>();
		for(String linkedResourceUri:linkedResourceUris) {
			if(config.getMode(axis) == DeserializationMode.PROXY) 
				resourceSet.add(proxyFactory.createResource(linkedResourceUri, cls));
			else if(config.getMode(axis) == DeserializationMode.URI_IN_PLAIN)
				resourceSet.add(pojoFactory.createResource(linkedResourceUri, cls));
			else if(config.getMode(axis) == DeserializationMode.CASCADE)
				resourceSet.add(readResource(linkedResourceUri, cls));
			else 
				logger.warn("No action was performed for deserializing the resource linked by the property " + pName + " to the resource " + uri + ".");
		} 
		}

		return resourceSet;
	}

	private <T extends IKtbsResource> T readLinkedResourceOnAxis(String uri, String pName,
			boolean inverse, Class<T> cls, LinkAxis axis) {
		if(config.getMode(axis) == DeserializationMode.NULL)
			return null;

		String linkedResourceuri;
		if(inverse) {
			StmtIterator it = model.listStatements(null, model.getProperty(pName), model.getResource(uri));
			if(!it.hasNext())
				return null;
			linkedResourceuri = it.next().getSubject().getURI();
		} else {
			Statement property = model.getResource(uri).getProperty(model.getProperty(pName));
			if(property == null)
				return null;
			linkedResourceuri = property.getObject().asResource().getURI();
		}

		if(config.getMode(axis) == DeserializationMode.PROXY) 
			return proxyFactory.createResource(linkedResourceuri, cls);
		else if(config.getMode(axis) == DeserializationMode.URI_IN_PLAIN)
			return pojoFactory.createResource(linkedResourceuri, cls);
		else if(config.getMode(axis) == DeserializationMode.CASCADE)
			return readResource(linkedResourceuri, cls);

		logger.warn("No action was performed for deserializing the resource linked by the property " + pName + " to the resource " + uri + ".");
		return null;
	}


	private IBase readBase(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (Base) alreadyReadResources.get(uri);

		Base base = new Base();
		base.setUri(uri);
		fillGenericResource(base);

		fillChildren(true, uri, KtbsConstants.P_CONTAINS, base.getStoredTraces(), false, IStoredTrace.class);
		fillChildren(true, uri, KtbsConstants.P_CONTAINS, base.getComputedTraces(), false, IComputedTrace.class);
		fillChildren(true, uri, KtbsConstants.P_CONTAINS, base.getTraceModels(), false, ITraceModel.class);
		fillChildren(true, uri, KtbsConstants.P_CONTAINS, base.getMethods(), false, IMethod.class);

		((KtbsResource)base).setParentResource(readParent(base, KtbsConstants.P_HAS_BASE, true, IRoot.class));

		return base;
	}

	private IComputedTrace readComputedTrace(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (ComputedTrace) alreadyReadResources.get(uri);

		ComputedTrace trace = new ComputedTrace();
		trace.setUri(uri);
		fillTrace(trace);
		trace.setWithMethodParameterDelegate(readResourceWithParameters(trace.getUri()));
		trace.setSourceTraces(readLinkedResourceSetSameType(uri, KtbsConstants.P_HAS_SOURCE, false, ITrace.class));
		trace.setMethod(readLinkedResource(uri, KtbsConstants.P_HAS_METHOD, false, IMethod.class));

		((KtbsResource)trace).setParentResource(readParent(trace, KtbsConstants.P_CONTAINS, true, IBase.class));

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


	private void fillGenericResource(IKtbsResource resource) {

		alreadyReadResources.put(resource.getUri(), resource);

		StmtIterator it = model.listStatements(
				model.getResource(resource.getUri()), 
				null, 
				(RDFNode)null);

		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			if(statement.getPredicate().equals(RDFS.label))
				resource.getLabels().add(statement.getObject().asLiteral().getString());
			else if(KtbsUtils.hasReservedNamespace(statement.getPredicate().getURI())
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

	private void fillTrace(ITrace trace) {
		fillGenericResource(trace);

		// the traceformed traces
		trace.setTransformedTraces(
				readLinkedResourceSetSameType(trace.getUri(), 
						KtbsConstants.P_HAS_SOURCE, 
						true, 
						IComputedTrace.class));

		// the obsels
		fillChildren(false, trace.getUri(), KtbsConstants.P_HAS_TRACE, trace.getObsels(), true, IObsel.class);


		// origin
		Object origin = getLiteral(trace.getUri(), KtbsConstants.P_HAS_ORIGIN);
		if(origin != null)
			trace.setOrigin(origin.toString());

		// complies with model
		trace.setCompliesWithModel(getLiteralOrNull(trace.getUri(), KtbsConstants.P_COMPLIES_WITH_MODEL, String.class));

		// the trace model
		ITraceModel model = readLinkedResource(trace.getUri(), KtbsConstants.P_HAS_MODEL, false, ITraceModel.class);
		trace.setTraceModel(model);

	}

	private Obsel readObsel(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (Obsel) alreadyReadResources.get(uri);

		Obsel obsel = new Obsel();
		obsel.setUri(uri);
		fillGenericResource(obsel);

		// the obsel type
		obsel.setObselType(readLinkedResource(uri, RDF.type.getURI(), false, IObselType.class));

		// begin
		Object begin = getLiteral(obsel.getUri(), KtbsConstants.P_HAS_BEGIN);
		if(begin != null)
			obsel.setBegin(new BigInteger(begin.toString()));

		// end
		Object end = getLiteral(obsel.getUri(), KtbsConstants.P_HAS_END);
		if(end != null)
			obsel.setEnd(new BigInteger(end.toString()));

		// beginDT
		Object beginDT = getLiteral(obsel.getUri(), KtbsConstants.P_HAS_BEGIN_DT);
		if(beginDT != null)
			obsel.setBeginDT(beginDT.toString());


		// endDT
		Object endDT = getLiteral(obsel.getUri(), KtbsConstants.P_HAS_END_DT);
		if(beginDT != null)
			obsel.setEndDT(endDT.toString());

		// subject
		obsel.setSubject(getLiteralOrNull(obsel.getUri(), KtbsConstants.P_HAS_SUBJECT, String.class));

		// parent trace
		((KtbsResource)obsel).setParentResource(readParent(obsel, KtbsConstants.P_HAS_TRACE, false, ITrace.class));


		// source obsels (transformation source obsels)
		obsel.setSourceObsels(readLinkedResourceSetSameType(uri, KtbsConstants.P_HAS_SOURCE_OBSEL, false, IObsel.class));


		// incoming relations
		Set<Statement> incomingSourceObselStatement = findIncomingSourceObselStatements(obsel.getUri());
		for(Statement stmt:incomingSourceObselStatement) {
			IObsel sourceObsel = createObselFromRelation(stmt.getSubject().getURI());
			if(sourceObsel != null)
				obsel.getIncomingRelations().add(readRelationStatement(
						sourceObsel, 
						stmt.getPredicate().getURI(), 
						obsel));
		}

		// outgoing relations
		Set<Statement> outgoingTargetObselStatements = findOutgoingTargetObselStatements(obsel.getUri());
		for(Statement stmt:outgoingTargetObselStatements) {
			IObsel targetObsel = createObselFromRelation(stmt.getObject().asResource().getURI());
			if(targetObsel != null)
				obsel.getOutgoingRelations().add(readRelationStatement(
						obsel, 
						stmt.getPredicate().getURI(), 
						targetObsel));
		}

		// attributes
		Set<Statement> attributeStatements = findAttributeStatements(obsel.getUri());
		for(Statement stmt:attributeStatements) {
			obsel.getAttributePairs().add(new AttributePair(
					proxyFactory.createResource(stmt.getPredicate().getURI(), IAttributeType.class),
					stmt.getObject().asLiteral().getValue()
			));
		}

		return obsel;
	}

	/*
	 * inverse is true when 'parent pName child'
	 * inverse is false when 'child pName parent'
	 */
	private <T extends IKtbsResource> T readParent(IKtbsResource child, String pParentChildRel, boolean inverse, Class<T> cls) {
		return readLinkedResourceOnAxis(child.getUri(), pParentChildRel, inverse, cls, LinkAxis.PARENT);
	}

	private IObsel createObselFromRelation(String obselUri) {
		IObsel obsel = null;
		if(config.getMode(LinkAxis.LINKED_SAME_TYPE) == DeserializationMode.NULL)
			return null;
		else if(config.getMode(LinkAxis.LINKED_SAME_TYPE) == DeserializationMode.URI_IN_PLAIN) 
			obsel = pojoFactory.createResource(obselUri, IObsel.class);
		else if(config.getMode(LinkAxis.LINKED_SAME_TYPE) == DeserializationMode.PROXY) 
			obsel = proxyFactory.createResource(obselUri, IObsel.class);
		else if(config.getMode(LinkAxis.LINKED_SAME_TYPE) == DeserializationMode.CASCADE) 
			obsel = readObsel(obselUri);
		else
			logger.warn("Unknown deserialisation option: " + config.getMode(LinkAxis.LINKED_SAME_TYPE));
		return obsel;
	}

	private ThreeKeyedMap<IRelationStatement> obselRelations = new ThreeKeyedMap<IRelationStatement>();

	private IRelationStatement readRelationStatement(IObsel sourceObsel,
			String relUri, IObsel targetObsel) {
		IRelationStatement rel = obselRelations.get(sourceObsel.getUri(), relUri, targetObsel.getUri());
		if(rel != null)
			return rel;
		else {
			rel = new RelationStatement(
					sourceObsel, 
					proxyFactory.createResource(relUri, IRelationType.class), 
					targetObsel);

			obselRelations.put(sourceObsel.getUri(), relUri, targetObsel.getUri(), rel);
			return rel;
		}
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


	/*
	 *  READ TRACE MODEL RESOURCES
	 */

	private AttributeType readAttributeType(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (AttributeType) alreadyReadResources.get(uri);

		AttributeType attType = new AttributeType();
		attType.setUri(uri);
		fillGenericResource(attType);


		attType.setDomains(readLinkedResourceSet(
				uri, 
				KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN, 
				false, 
				IObselType.class));


		// ranges
		StmtIterator it = model.listStatements(
				model.getResource(attType.getUri()), 
				model.getProperty(KtbsConstants.P_HAS_ATTRIBUTE_RANGE), 
				(RDFNode)null);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			attType.getRanges().add(statement.getObject().asLiteral().getString());
		}

		// always cascade the deserialization to the parent trace model for a trace model child
		// TODO change this properly with the generic putParent method when the rdf format changes
		((KtbsResource)attType).setParentResource(readTraceModel(KtbsUtils.resolveParentURI(attType.getUri())));

		return attType;
	}

	private RelationType readRelationType(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (RelationType) alreadyReadResources.get(uri);

		RelationType relType = new RelationType();
		relType.setUri(uri);
		fillGenericResource(relType);

		// super relation types
		relType.setSuperRelationTypes(readLinkedResourceSetSameType(
				uri, 
				KtbsConstants.P_HAS_SUPER_RELATION_TYPE, 
				false, 
				IRelationType.class));

		// domains
		relType.setDomains(readLinkedResourceSet(
				uri, 
				KtbsConstants.P_HAS_RELATION_DOMAIN, 
				false, 
				IObselType.class));

		// ranges
		relType.setRanges(readLinkedResourceSet(
				uri, 
				KtbsConstants.P_HAS_RELATION_RANGE, 
				false, 
				IObselType.class));

		// always cascade the deserialization to the parent trace model for a trace model child
		// TODO change this properly with the generic putParent method when the rdf format changes
		((KtbsResource)relType).setParentResource(readTraceModel(KtbsUtils.resolveParentURI(relType.getUri())));


		return relType;
	}


	private ObselType readObselType(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (ObselType) alreadyReadResources.get(uri);

		ObselType obselType = new ObselType();
		obselType.setUri(uri);
		fillGenericResource(obselType);

		obselType.setSuperObselTypes(readLinkedResourceSetSameType(
				uri, 
				KtbsConstants.P_HAS_SUPER_OBSEL_TYPE, 
				false, 
				IObselType.class));

		// always cascade the deserialization to the parent trace model for a trace model child
		// TODO change this properly with the generic putParent method when the rdf format changes
		((KtbsResource)obselType).setParentResource(readTraceModel(KtbsUtils.resolveParentURI(obselType.getUri())));


		return obselType;
	}

	/*
	 * Read a trace from the model without knowing in advance if 
	 * its a stored trace or a computed trace
	 */
	private ITrace readTrace(String uri) {
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

	private IStoredTrace readStoredTrace(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (StoredTrace) alreadyReadResources.get(uri);

		StoredTrace trace = new StoredTrace();
		trace.setUri(uri);
		fillTrace(trace);

		trace.setDefaultSubject(getLiteralOrNull(uri, KtbsConstants.P_HAS_SUBJECT, String.class));

		((KtbsResource)trace).setParentResource(readParent(trace, KtbsConstants.P_CONTAINS, true, IBase.class));

		return trace;
	}

	private ITraceModel readTraceModel(String uri) {
		if(alreadyReadResources.containsKey(uri))
			return (TraceModel) alreadyReadResources.get(uri);


		TraceModel traceModel = new TraceModel();
		traceModel.setUri(uri);
		fillGenericResource(traceModel);

		if(config.getChildMode() == DeserializationMode.NULL)
			return traceModel;

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
			if(objectResource.equals(KtbsConstants.RELATION_TYPE)) {
				IRelationType relType = readTraceModelChild(uri2, IRelationType.class);
				if(relType == null)
					continue;
				traceModel.getRelationTypes().add(relType);
			} else if(objectResource.equals(KtbsConstants.OBSEL_TYPE)) {

				IObselType obsType = readTraceModelChild(uri2, IObselType.class);
				if(obsType == null)
					continue;
				traceModel.getObselTypes().add(obsType);
			} else if(objectResource.equals(KtbsConstants.ATTRIBUTE_TYPE))  {
				IAttributeType attType = readTraceModelChild(uri2, IAttributeType.class);
				if(attType == null)
					continue;
				traceModel.getAttributeTypes().add(attType);
			} else
				/*
				 * In some cases, a statement [(attributeType|obselType|relationType)Uri, RDF.type, ktbs:TraceModel]
				 * seems to be present in the model in addition to the expected rdf:type of the trace model element
				 */
				logger.warn("The resource "+uri2+" has the same prefix than the trace model but is of unknown type: " + objectResource);
		}

		((KtbsResource)traceModel).setParentResource(readParent(traceModel, KtbsConstants.P_CONTAINS, true, IBase.class));

		return traceModel;
	}


	private <T extends IKtbsResource> T readTraceModelChild(String childUri, Class<T> cls) {

		if(config.getChildMode() == DeserializationMode.NULL)
			return null;
		else if(config.getChildMode() == DeserializationMode.PROXY) 
			return proxyFactory.createResource(childUri, cls);
		else if(config.getChildMode() == DeserializationMode.URI_IN_PLAIN)
			return pojoFactory.createResource(childUri, cls);
		else if(config.getChildMode() == DeserializationMode.CASCADE)
			return readResource(childUri, cls);
		else {
			logger.warn("Unknown deserialization mode for the child axis: " + config.getChildMode());
			return null;
		}
	}

	private String getRdfType(String uri) {
		String type = model.getResource(uri).getPropertyResourceValue(RDF.type).getURI();
		if(type == null)
			return null;
		else 
			return type;
	}

	private <T> T getLiteralOrNull(String uri, String pName, Class<T> cls) {
		Object literal = getLiteral(uri, pName);
		if(literal == null)
			return null;
		else
			return cls.cast(literal);
	}

	/*
	 * Return null if there is not such property set for that resource
	 */
	private Object getLiteral(String uri, String pName) {
		Statement stmt = model.getResource(uri).getProperty(model.getProperty(pName));
		if(stmt == null)
			return null;
		RDFNode object = stmt.getObject();
		if(object!=null && object.isLiteral())
			return object.asLiteral().getValue();
		else
			return null;
	}


}
