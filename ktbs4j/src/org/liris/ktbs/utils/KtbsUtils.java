package org.liris.ktbs.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.liris.ktbs.client.KtbsConstants;
import org.liris.ktbs.domain.AttributeType;
import org.liris.ktbs.domain.KtbsResource;
import org.liris.ktbs.domain.MethodParameter;
import org.liris.ktbs.domain.Obsel;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IMethodParameter;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * 
 * @author Damien Cram
 *
 */
public class KtbsUtils {

	private static final Logger logger = LoggerFactory.getLogger(KtbsUtils.class);

	/**
	 * Tell if a given property name is defined is a namespace reserved by the KTBS.
	 * 
	 * <p>
	 *  Reserved namespaces are:
	 *  <ul>
	 *  <li>http://liris.cnrs.fr/silex/2009/ktbs#</li>
	 *  <li>http://blablabla/rdf#</li>
	 *  <li>http://blablabla/rdfs#</li>
	 *  </ul>
	 * </p>
	 * 
	 * <p>
	 * The purpose of forbidding the entire namespaces rdf anb rdfs (while 
	 * only rdf:type and rdfs:label are reserved by the KTBS) is to draw the border
	 * between triples that are interpreted as relations between obsel and triples interpreted as
	 * simple resource properties.
	 * </p>
	 * 
	 * @param pName
	 * @return
	 */
	public static boolean hasReservedNamespace(String pName) {
		return pName.startsWith(KtbsConstants.NAMESPACE_KTBS)
		|| pName.startsWith(RDFS.getURI())
		|| pName.startsWith(RDF.getURI());
	}

	/**
	 * Give the Java class that is used to create new instances 
	 * of a KTBS resource of given rdf type.
	 * 
	 * @param rdfType the value of the rdf:type property of the KTBS resource
	 * @return the Java class that is associated to that rdf type, null if 
	 * the rdf type does not defined a KTBS resource
	 */
	public static Class<? extends IKtbsResource> getJavaClass(String rdfType) {
		if(rdfType.equals(KtbsConstants.ROOT)) 
			return IRoot.class;
		else if(rdfType.equals(KtbsConstants.BASE)) 
			return IBase.class;
		else if(rdfType.equals(KtbsConstants.STORED_TRACE)) 
			return IStoredTrace.class;
		else if(rdfType.equals(KtbsConstants.COMPUTED_TRACE)) 
			return IComputedTrace.class;
		else if(rdfType.equals(KtbsConstants.ATTRIBUTE_TYPE)) 
			return IAttributeType.class;
		else if(rdfType.equals(KtbsConstants.RELATION_TYPE)) 
			return IRelationType.class;
		else if(rdfType.equals(KtbsConstants.OBSEL_TYPE)) 
			return IObselType.class;
		else if(rdfType.equals(KtbsConstants.TRACE_MODEL)) 
			return ITraceModel.class;
		else if(rdfType.equals(KtbsConstants.METHOD)) 
			return IMethod.class;
		return null;

	}

	/**
	 * Convert a long to a big integer.
	 * 
	 * @param begin
	 * @return
	 */
	public static BigInteger longToBigInt(long begin) {
		return new BigInteger(Long.toString(begin));
	}

	public static IMethodParameter parseMethodParameter(String string) {
		int index = string.indexOf("=");
		String key = string.substring(0, index);
		String value = string.substring(index+1, string.length());
		return new MethodParameter(key, value);
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
		return resolveParentURI(resource.getUri());
	}

	/**
	 * Give the rdf type that is used to represent a KTBS resource of a 
	 * given Java type in RDF, according to KTBS specifications.
	 * 
	 * @param cls the type of KTBS resource
	 * @return the rdf type associated to that class of KTBS resource
	 */
	public static String getRDFType(Class<?> cls) {
		if(IRoot.class.isAssignableFrom(cls)) 
			return KtbsConstants.ROOT;
		else if(IBase.class.isAssignableFrom(cls)) 
			return KtbsConstants.BASE;
		else if(IStoredTrace.class.isAssignableFrom(cls)) 
			return KtbsConstants.STORED_TRACE;
		else if(IComputedTrace.class.isAssignableFrom(cls)) 
			return KtbsConstants.COMPUTED_TRACE;
		else if(IObsel.class.isAssignableFrom(cls)) 
			return null;
		else if(IObselType.class.isAssignableFrom(cls)) 
			return KtbsConstants.OBSEL_TYPE;
		else if(IAttributeType.class.isAssignableFrom(cls)) 
			return KtbsConstants.ATTRIBUTE_TYPE;
		else if(IRelationType.class.isAssignableFrom(cls)) 
			return KtbsConstants.RELATION_TYPE;
		else if(ITraceModel.class.isAssignableFrom(cls)) 
			return KtbsConstants.TRACE_MODEL;
		else if(IMethod.class.isAssignableFrom(cls)) 
			return KtbsConstants.METHOD;
		else if(ITrace.class.isAssignableFrom(cls)) 
			return null;
		else
			return null;
	}

	public static boolean isLeafType(Class<? extends IKtbsResource> cls) {
		return IObsel.class.isAssignableFrom(cls) 
		|| IRelationType.class.isAssignableFrom(cls) 
		|| IMethod.class.isAssignableFrom(cls) 
		|| IRelationType.class.isAssignableFrom(cls) 
		|| IAttributeType.class.isAssignableFrom(cls) 
		|| IObselType.class.isAssignableFrom(cls) ;
	}

	public static Date parseXsdDate(String dateString) throws ParseException {
		SimpleDateFormat xsdDatetimeFormat = KtbsConstants.XSD_DATETIME_FORMAT;
		xsdDatetimeFormat.setTimeZone(KtbsConstants.UTC_ZONE);

		return xsdDatetimeFormat.parse(dateString);
	}
	public static String now() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat xsdDatetimeFormat = KtbsConstants.XSD_DATETIME_FORMAT;
		xsdDatetimeFormat.setTimeZone(KtbsConstants.UTC_ZONE);

		String nowAsXsdString = xsdDatetimeFormat.format(calendar.getTime());
		return nowAsXsdString;
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
	public static Collection<String> toUriCollection(Collection<KtbsResource> c) {
		if( c == null)
			return null;
		Collection<String> uriCollection = new ArrayList<String>(c.size());
		for(KtbsResource r:c)
			uriCollection.add(r.getUri());
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
			m.put(att.getUri(), attributes.get(att));
		return m;
	}

	/**
	 * Creates an absolute URI for a child whose uri is either relative to 
	 * its parent's or absolute.
	 * 
	 * @param parentURI the uri of the parent
	 * @param descendantURI the uri, either relative or absolute, of the child.
	 * @return the absolute child URI
	 */
	public static String makeAbsoluteURI(String parentURI, String descendantURI, boolean leaf) {
		String uri = makeAbsoluteURI(parentURI, descendantURI);
		if(leaf && uri.endsWith("/"))
			return replaceLast(uri, "/", "");
		if(!leaf && !uri.endsWith("/"))
			return uri+"/";
		return uri;
	}


	public static String getRDFType(IKtbsResource r) {
		if(Obsel.class.isAssignableFrom(r.getClass()))
			return ((IObsel)r).getObselType().getUri();
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

	public static boolean isTraceModelElement(IKtbsResource resource) {
		Class<? extends IKtbsResource> cls = resource.getClass();
		return IAttributeType.class.isAssignableFrom(cls)
		|| IRelationType.class.isAssignableFrom(cls)
		|| IObselType.class.isAssignableFrom(cls);
	}
	public static boolean isObsel(IKtbsResource resource) {
		Class<? extends IKtbsResource> cls = resource.getClass();
		return IObsel.class.isAssignableFrom(cls);
	}
	public static boolean isTrace(IKtbsResource resource) {
		Class<? extends IKtbsResource> cls = resource.getClass();
		return ITrace.class.isAssignableFrom(cls);
	}

	public static boolean isTraceModel(IKtbsResource resource) {
		Class<? extends IKtbsResource> cls = resource.getClass();
		return ITraceModel.class.isAssignableFrom(cls);
	}

	public static String xsdDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return xsdDate(
				c.get(Calendar.YEAR), 
				c.get(Calendar.MONTH) +1, 
				c.get(Calendar.DAY_OF_MONTH), 
				c.get(Calendar.HOUR_OF_DAY), 
				c.get(Calendar.MINUTE), 
				c.get(Calendar.SECOND),
				c.get(Calendar.MILLISECOND)
		);
	}

	public static String xsdDate(int year, int month, int day, int hours, int min, int sec, int milisec) {
		return xsdDate(year, month, day, hours, min, sec, milisec, TimeZone.getTimeZone("Europe/Paris"));
	}

	public static String xsdDateUTC(int year, int month, int day, int hours, int min, int sec, int milisec) {
		return xsdDate(year, month, day, hours, min, sec, milisec, TimeZone.getTimeZone("UTC"));
	}

	public static String xsdDate(int year, int month, int day, int hours, int min, int sec,int milisec, TimeZone tz) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(tz);
		calendar.setTimeInMillis(0);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, sec);
		calendar.set(Calendar.MILLISECOND, milisec);

		SimpleDateFormat xsdDatetimeFormat = KtbsConstants.XSD_DATETIME_FORMAT;
		if(tz == null){
		    xsdDatetimeFormat.setTimeZone(KtbsConstants.UTC_ZONE);
		}else{
		    xsdDatetimeFormat.setTimeZone(tz);
		}
		String asXsdString = xsdDatetimeFormat.format(calendar.getTime());
		return asXsdString;
	}

	public static String guessRdfType(String bodyAsString, String uri, String syntax) {
		Model model = ModelFactory.createDefaultModel();
		return guessRdfType(model, uri);
	}

	public static String guessRdfType(Model model, String uri) {
		Class<? extends IKtbsResource> cls = guessResourceType(model, uri);
		return getRDFType(cls);
	}

	public static Class<? extends IKtbsResource> guessResourceType(Model model, String uri) {
		Class<? extends IKtbsResource> cls = null;

		if(logger.isDebugEnabled()) {
			logger.debug("There are " + model.size() + " statements in the model.");
		}

		StmtIterator it = model.listStatements(model.getResource(uri), RDF.type, (RDFNode)null);
		if(!it.hasNext()) {
			throw new RuntimeException("No rdf:type defined for the uri " + uri);
		} else {
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
					throw new IllegalStateException("The rdf:type of the resource " + uri + " is neither a ktbs type nor an obsel type (no ktbs:hasTrace property for that resource)");
				it2.next();

				// this is an obsel
				cls = IObsel.class;
			} 
		}

		checkOnlyOneRdfType(model, uri, it);
		return cls;
	}

	private static void checkOnlyOneRdfType(Model model, String uri,
			StmtIterator it) {
		if(it.hasNext()) {
			StringWriter modelStringWriter = new StringWriter();
			model.write(modelStringWriter, KtbsConstants.JENA_TURTLE, "");

			StringWriter stmtStringWriter = new StringWriter();
			StmtIterator it3 = model.listStatements(model.getResource(uri), RDF.type, (RDFNode)null);
			while (it3.hasNext()) {
				Statement statement = (Statement) it3.next();
				stmtStringWriter.write(statement.toString());
				stmtStringWriter.write("\n");
			}
			logger.warn("There are more than one rdf:type defined for the resource {}. Statements are : \n{}\nComplete model is : \n{}", 
					new Object[]{uri, 
					stmtStringWriter.toString(),
					modelStringWriter.toString()
			});
		}
	}

	public static String removeUriAspects(String requestUri) {
		if(requestUri == null)
			return null;
		return requestUri.replaceAll(KtbsConstants.ABOUT_ASPECT, "").replaceAll(KtbsConstants.OBSELS_ASPECT, "");
	}

	/**.
	 * @param rootUri
	 * @param uri
	 * @return
	 */
	public static String makeAbsoluteURI(String parentURI, String descendantURI) {
		URI child = URI.create(descendantURI);
		String uri;
		if(child.isAbsolute())
			uri = child.normalize().toString();
		else 
			uri = URI.create(parentURI).resolve(child).normalize().toString();
		return uri;
	}

	/**.
	 * @param bodyAsString
	 * @param mimeType
	 * @param absoluteResourceUri
	 * @return
	 */
	public static Class<? extends IKtbsResource> guessResourceType(String bodyAsString,
			String mimeType, String baseUri, String resourceUri) {
		Model model = ModelFactory.createDefaultModel();
		String jenaSyntax = KtbsUtils.getJenaSyntax(mimeType);
		model.read(new StringReader(bodyAsString), baseUri, jenaSyntax);
		return guessResourceType(model, resourceUri);
	}

	public static Class<? extends IKtbsResource> getJavaInterface(
			Class<? extends IKtbsResource> cls) {
		if(IRoot.class.isAssignableFrom(cls)) 
		    return IRoot.class;
		else if(IBase.class.isAssignableFrom(cls)) 
			return IBase.class;
		else if(IStoredTrace.class.isAssignableFrom(cls)) 
			return IStoredTrace.class;
		else if(IComputedTrace.class.isAssignableFrom(cls)) 
			return IComputedTrace.class;
		else if(ITrace.class.isAssignableFrom(cls)) 
			return ITrace.class;
		else if(IObsel.class.isAssignableFrom(cls)) 
		    return IObsel.class;
		else if(IObselType.class.isAssignableFrom(cls)) 
		    return IObselType.class;
		else if(IAttributeType.class.isAssignableFrom(cls)) 
			return IAttributeType.class;
		else if(IRelationType.class.isAssignableFrom(cls)) 
			return IRelationType.class;
		else if(ITraceModel.class.isAssignableFrom(cls)) 
			return ITraceModel.class;
		else if(IMethod.class.isAssignableFrom(cls)) 
			return IMethod.class;
		else
		    return null;
	}
}
