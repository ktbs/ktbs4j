package org.liris.ktbs.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.liris.ktbs.core.JenaConstants;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.SimpleKtbsParameter;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.KtbsParameter;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.KtbsStatement;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.Root;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;

public class KtbsUtils {

	/**
	 * Give the Java class that is used to create new instances 
	 * of a KTBS resource of given rdf type.
	 * 
	 * @param rdfType the value of the rdf:type property of the KTBS resource
	 * @return the Java class that is associated to that rdf type, null if 
	 * the rdf type does not defined a KTBS resource
	 */
	public static Class<? extends KtbsResource> getJavaClass(String rdfType) {
		if(rdfType.equals(KtbsConstants.ROOT)) 
			return Root.class;
		else if(rdfType.equals(KtbsConstants.BASE)) 
			return Base.class;
		else if(rdfType.equals(KtbsConstants.STORED_TRACE)) 
			return StoredTrace.class;
		else if(rdfType.equals(KtbsConstants.COMPUTED_TRACE)) 
			return ComputedTrace.class;
		else if(rdfType.equals(KtbsConstants.ATTRIBUTE_TYPE)) 
			return AttributeType.class;
		else if(rdfType.equals(KtbsConstants.RELATION_TYPE)) 
			return RelationType.class;
		else if(rdfType.equals(KtbsConstants.OBSEL_TYPE)) 
			return ObselType.class;
		else if(rdfType.equals(KtbsConstants.TRACE_MODEL)) 
			return TraceModel.class;
		else if(rdfType.equals(KtbsConstants.METHOD)) 
			return Method.class;
		return null;

	}

	/**
	 * Give the mime type associated to a rdf syntax.
	 * 
	 * @param jenaSyntax the RDF syntax
	 * @return the mime type
	 * @see JenaConstants
	 */
	public static String getMimeType(String jenaSyntax) {
		if(jenaSyntax.equals(JenaConstants.TURTLE))
			return KtbsConstants.MIME_TURTLE;
		else if(jenaSyntax.equals(JenaConstants.N3))
			return KtbsConstants.MIME_N3;
		else if(jenaSyntax.equals(JenaConstants.N_TRIPLES))
			return KtbsConstants.MIME_NTRIPLES;
		else 
			return KtbsConstants.MIME_RDF_XML;
	}
	
	/**
	 * Give the Jena syntax associated to a mime type.
	 * 
	 * @param mimeType the mime type
	 * @return the RDF syntax
	 * @see JenaConstants
	 */
	public static String getJenaSyntax(String mimeType) {
		if(mimeType.equals(KtbsConstants.MIME_TURTLE))
			return JenaConstants.TURTLE;
		else if(mimeType.equals(KtbsConstants.MIME_N3))
			return JenaConstants.N3;
		else if(mimeType.equals(KtbsConstants.MIME_NTRIPLES))
			return JenaConstants.N_TRIPLES;
		else if(mimeType.equals(KtbsConstants.MIME_RDF_XML))
			return JenaConstants.RDF_XML_ABBR;
		else
			return "text/plain";
	}
	
	/**
	 * Resolves the URI of the parent resource of a Ktbs resource.
	 * 
	 * @param resource the child resource whose parent resource URI 
	 * is to be resolved
	 * @return the URI of the parent resource of this resource, null 
	 * if no parent URI could be resolved
	 * 
	 * TODO implements this method the proper way, i.e. by getting the parent 
	 * URI of the resource from the resource itself.
	 * 
	 */
	public static String getParentResource(KtbsResource resource) {
		return resolveParentURI(resource.getURI());
	}
	
	/**
	 * Give the rdf type that is used to represent a KTBS resource of a 
	 * given Java type in RDF, according to KTBS specifications.
	 * 
	 * @param clazz the type of KTBS resource
	 * @return the rdf type associated to that class of KTBS resource
	 */
	public static String getRDFType(Class<?> clazz) {
		if(Root.class.isAssignableFrom(clazz)) 
			return KtbsConstants.ROOT;
		else if(Base.class.isAssignableFrom(clazz)) 
			return KtbsConstants.BASE;
		else if(StoredTrace.class.isAssignableFrom(clazz)) 
			return KtbsConstants.STORED_TRACE;
		else if(ComputedTrace.class.isAssignableFrom(clazz)) 
			return KtbsConstants.COMPUTED_TRACE;
		else if(Obsel.class.isAssignableFrom(clazz)) 
			return null;
		else if(ObselType.class.isAssignableFrom(clazz)) 
			return KtbsConstants.OBSEL_TYPE;
		else if(AttributeType.class.isAssignableFrom(clazz)) 
			return KtbsConstants.ATTRIBUTE_TYPE;
		else if(RelationType.class.isAssignableFrom(clazz)) 
			return KtbsConstants.RELATION_TYPE;
		else if(TraceModel.class.isAssignableFrom(clazz)) 
			return KtbsConstants.TRACE_MODEL;
		else if(Method.class.isAssignableFrom(clazz)) 
			return KtbsConstants.METHOD;
		else if(Trace.class.isAssignableFrom(clazz)) 
			return null;
		else
			return null;
	}


	/**
	 * Create a new linked list built from an iterator.
	 * 
	 * @param <T> the class of object contained in the new linked list
	 * @param it the source iterator
	 * @return the created linked list
	 */
	public static <T> LinkedList<T> toLinkedList(Iterator<T> it) {
		LinkedList<T> list = new LinkedList<T>();
		while (it.hasNext())
			list.add(it.next());
		return list;
	}

	/**
	 * Create a new iterable from an iterator.
	 * 
	 * @param <T> the class of object contained in the new iterable
	 * @param it the source iterator
	 * @return the created iterable object
	 */
	public static <T> Iterable<T> toIterable(final Iterator<T> it) {
		Iterable<T> iterable = new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return it;
			}
		};
		return iterable;
	}

	/**
	 * Transform a collection of KTBS statement to a String-Object map in which 
	 * keys are the predicate uris of the statement and values are associated values.
	 * 
	 * @param it the iterator on input KTBS statements
	 * @return the created map
	 */
	public static Map<String,Object> toMap(Iterator<KtbsStatement> it) {
		Map<String,Object> map = new HashMap<String, Object>();
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			map.put(ktbsStatement.getProperty(), ktbsStatement.getObject());
		}
		return map;
	}

	/**
	 * Give the number of element in an iterator.
	 * 
	 * @param it the input iterator
	 * @return the number of elements
	 */
	public static int  count(Iterator<?> it) {
		int cnt = 0;
		while (it.hasNext()) {
			it.next();
			cnt++;
		}
		return cnt;
	}

	/**
	 * Give the number of KTBS statements that have the specified value as 
	 * subject.
	 * 
	 * @param it the iterator on input KTBS statement
	 * @param subject the requested subject
	 * @return the number of KTBS statements that have the specified value as 
	 * subject
	 */
	public static int  countSubject(Iterator<KtbsStatement> it, String subject) {
		int cnt = 0;
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			if(ktbsStatement.getSubject().equals(subject))
				cnt++;
		}
		return cnt;
	}

	/**
	 * Give the number of KTBS statements that have a predicate
	 * of a given name.
	 * 
	 * @param it the iterator on input KTBS statement
	 * @param pName the requested property name 
	 * @return the number of KTBS statements that have a predicate
	 * of the specified name
	 */
	public static int  countProperty(Iterator<KtbsStatement> it, String pName) {
		int cnt = 0;
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			if(ktbsStatement.getProperty().equals(pName))
				cnt++;
		}
		return cnt;
	}

	/**
	 * Give the number of KTBS statements that have an object
	 * of a specified value.
	 * 
	 * @param it the iterator on input KTBS statement
	 * @param object the requested object value
	 * @return the number of KTBS statements that have an object 
	 * of the specified value
	 */
	public static int  countObject(Iterator<KtbsStatement> it, String object) {
		int cnt = 0;
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			if(ktbsStatement.getObject().equals(object))
				cnt++;
		}
		return cnt;
	}

	/**
	 * Give the number of KTBS statements that have a given subject and a given predicate name.
	 * 
	 * @param it the iterator on input KTBS statement
	 * @param subject the requested subject name
	 * @param pName the requested predicate name
	 * @return the number of KTBS statements that have a given subject and a given predicate name
	 */
	public static int  countSubjectProperty(Iterator<KtbsStatement> it, String subject, String pName) {
		int cnt = 0;
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			if(ktbsStatement.getSubject().equals(subject) && ktbsStatement.getProperty().equals(pName) )
				cnt++;
		}
		return cnt;
	}

	/**
	 * Give the number of KTBS statements that have a given subject 
	 * and a predicate starting with a given prefix.
	 * 
	 * @param it the iterator on input KTBS statement
	 * @param subject the requested subject name
	 * @param propertyNS the requested predicate prefix
	 * @return the number of KTBS statements that have a given subject 
	 * and a predicate starting with a given prefix
	 */
	public static int  countSubjectPropertyNS(Iterator<KtbsStatement> it, String subject, String propertyNS) {
		int cnt = 0;
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			if(ktbsStatement.getSubject().equals(subject) && ktbsStatement.getProperty().startsWith(propertyNS))
				cnt++;
		}
		return cnt;
	}

	/**
	 * Resolve the local name (last part after all "/" chars) of a given uri. 
	 * 
	 * @param uri the absolute uri
	 * @return the local name
	 */
	public static String resolveLocalName(String uri) {
		String path;
		try {
			path = new URI(uri).normalize().getPath();
			StringTokenizer st = new StringTokenizer(path, "/");
			String lastToken = null;
			while(st.hasMoreTokens())
				lastToken = st.nextToken();
			return lastToken;
		} catch (URISyntaxException e) {
			throw new RuntimeException("Invalid URI syntax: \""+uri+"\".",e);
		}
	}

	/**
	 * Resolve the parent (the prefix until the last "/" included) of a given uri. 
	 * 
	 * @param uri the absolute uri
	 * @return the parent uri
	 */
	public static String resolveParentURI(String uri) {
		try {
			String localName = resolveLocalName(uri);
			String normalized = new URI(uri).normalize().getPath();
			String suffix = normalized.endsWith("/")?"/":"";
			return replaceLast(uri, localName+suffix, "");
		} catch (URISyntaxException e) {
			throw new RuntimeException("Invalid URI syntax: \""+uri+"\".",e);
		}

	}

	/**
	 * Replace the last match of a regex with a given value.
	 * 
	 * @param input the input string
	 * @param regex the regex
	 * @param replacement the replacement string
	 * @return the new string
	 */
	public static String replaceLast(String input, String regex, String replacement) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (!matcher.find()) {
			return input;
		}
		int lastMatchStart=0;
		do {
			lastMatchStart=matcher.start();
		} while (matcher.find());
		matcher.find(lastMatchStart);
		StringBuffer sb = new StringBuffer(input.length());
		matcher.appendReplacement(sb, replacement);
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Parse a string that contains a KTBS parameter definition, i.e. a string 
	 * of the form "key=value", into a {@link KtbsParameter}.
	 * 
	 * @param string the string containing the parameter definition
	 * @return the {@link KtbsParameter} object created from that string
	 */
	public static KtbsParameter parseParameter(String string) {
		int index = string.indexOf("=");
		String key = string.substring(0, index);
		String value = string.substring(index+1, string.length());
		return new SimpleKtbsParameter(key, value);
	}

	/**
	 * Builds a URI with a specified Ktbs resource aspect (e.g. &#64;obsels 
	 * and &#64;about for a ktbs:StoredTrace or a ktbs:ComputedTrace) at the 
	 * end of the URI, whatever the aspect suffix was before calling this method.
	 * 
	 * @param resourceURI the input resource URI
	 * @param aspect the aspect suffix, null if no aspect suffix wanted
	 * @param alternatives the other aspect suffixes that the resource could have had
	 * @return the resource URI, with the desired aspect suffix
	 */
	public static String addAspect(String resourceURI, String aspect, String... alternatives) {
		if(aspect == null) {
			String uri = resourceURI;
			for(String alt:alternatives) {
				if(uri.endsWith(alt)) {
					uri = uri.replaceAll(alt, "");
					return uri;
				}
			}
			return uri;
		}
		
		if(resourceURI.endsWith(aspect))
			return resourceURI;

		for(String alt:alternatives) {
			if(resourceURI.endsWith(alt)) {
				String newURI = resourceURI.replaceAll(alt, aspect);
				return newURI;
			}
		}

		return resourceURI+aspect;
	}

	/**
	 * Create a collection of resource uris from a collection of KTBS resources.
	 * 
	 * @param c the input collection of KTBS resources
	 * @return the collection of resource uris
	 */
	public static Collection<String> toUriCollection(Collection<? extends KtbsResource> c) {
		if( c == null)
			return null;
		Collection<String> uriCollection = new ArrayList<String>(c.size());
		for(KtbsResource r:c)
			uriCollection.add(r.getURI());
		return uriCollection;
	}

	/**
	 * Create an attribute map with attribute uris as keys
	 * from an attribute map that has {@link AttributeType} object as keys
	 * 
	 * @param attributes the input attribute map
	 * @return the created attribute map with attribute uris as keys
	 */
	public static Map<String, Object> toUriMap(
			Map<AttributeType, Object> attributes) {
		if(attributes == null)
			return null;
		Map<String, Object> m = new HashMap<String, Object>();
		for(AttributeType att:attributes.keySet())
			m.put(att.getURI(), attributes.get(att));
		return m;
	}

}
