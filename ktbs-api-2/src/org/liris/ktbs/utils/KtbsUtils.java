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

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.Root;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.core.api.share.KtbsResource;

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

	/**
	 * Creates an absolute URI for a child whose uri is either relative to 
	 * its parent's or absolute.
	 * 
	 * @param parentURI the uri of the parent
	 * @param childURI the uri, either relative or absolute, of the child.
	 * @return the absolute child URI
	 */
	public static String resolveAbsoluteChildURI(String parentURI, String childURI) {
		URI child = URI.create(childURI);
		if(child.isAbsolute())
			return child.toString();
		else 
			return URI.create(parentURI).resolve(child).toString();
	}


	public static String getRDFType(KtbsResource r) {
		if(Obsel.class.isAssignableFrom(r.getClass()))
			return ((Obsel)r).getObselType().getURI();
		else
			return getRDFType(r.getClass());
	}


	public static String getJenaSyntax(String mimeFormat) {
		if(mimeFormat.equals(KtbsConstants.MIME_RDF_XML))
			return KtbsConstants.JENA_RDF_XML;
		else if(mimeFormat.equals(KtbsConstants.MIME_TURTLE))
			return KtbsConstants.JENA_TURTLE;
		else if(mimeFormat.equals(KtbsConstants.MIME_N3))
			return KtbsConstants.JENA_N3;
		else if(mimeFormat.equals(KtbsConstants.MIME_NTRIPLES))
			return KtbsConstants.JENA_N_TRIPLES;
		else
			throw new IllegalArgumentException("This mime type cannot be mapped to an rdf syntax: " + mimeFormat);
	}
}