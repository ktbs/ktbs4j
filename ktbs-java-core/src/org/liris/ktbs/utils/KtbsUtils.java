package org.liris.ktbs.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsParameter;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.KtbsStatement;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.SimpleKtbsParameter;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;

public class KtbsUtils {

	public static Class<? extends KtbsResource> getJavaClass(String rdfType) {
		if(rdfType.equals(KtbsConstants.KTBS_ROOT)) 
			return KtbsRoot.class;
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

	public static String getRDFType(Class<?> clazz) {
		if(clazz.equals(KtbsRoot.class)) 
			return KtbsConstants.KTBS_ROOT;
		else if(clazz.equals(Base.class)) 
			return KtbsConstants.BASE;
		else if(clazz.equals(StoredTrace.class)) 
			return KtbsConstants.STORED_TRACE;
		else if(clazz.equals(ComputedTrace.class)) 
			return KtbsConstants.COMPUTED_TRACE;
		else if(clazz.equals(Obsel.class)) 
			return null;
		else if(clazz.equals(ObselType.class)) 
			return KtbsConstants.OBSEL_TYPE;
		else if(clazz.equals(AttributeType.class)) 
			return KtbsConstants.ATTRIBUTE_TYPE;
		else if(clazz.equals(RelationType.class)) 
			return KtbsConstants.RELATION_TYPE;
		else if(clazz.equals(TraceModel.class)) 
			return KtbsConstants.TRACE_MODEL;
		else if(clazz.equals(Method.class)) 
			return KtbsConstants.METHOD;
		else if(clazz.equals(Trace.class)) 
			return null;
		else
			return null;
	}

	public static <T> LinkedList<T> toLinkedList(Iterator<T> it) {
		LinkedList<T> list = new LinkedList<T>();
		while (it.hasNext())
			list.add(it.next());
		return list;
	}

	public static <T> Iterable<T> toIterable(final Iterator<T> it) {
		Iterable<T> iterable = new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return it;
			}
		};
		return iterable;
	}

	public static Map<String,String> toMap(Iterator<KtbsStatement> it) {
		Map<String,String> map = new HashMap<String, String>();
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			map.put(ktbsStatement.getProperty(), ktbsStatement.getObject());
		}
		return map;
	}

	public static int  count(Iterator<?> it) {
		int cnt = 0;
		while (it.hasNext()) {
			it.next();
			cnt++;
		}
		return cnt;
	}

	public static int  countSubject(Iterator<KtbsStatement> it, String subject) {
		int cnt = 0;
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			if(ktbsStatement.getSubject().equals(subject))
				cnt++;
		}
		return cnt;
	}

	public static int  countProperty(Iterator<KtbsStatement> it, String property) {
		int cnt = 0;
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			if(ktbsStatement.getProperty().equals(property))
				cnt++;
		}
		return cnt;
	}

	public static int  countObject(Iterator<KtbsStatement> it, String object) {
		int cnt = 0;
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			if(ktbsStatement.getObject().equals(object))
				cnt++;
		}
		return cnt;
	}

	public static int  countSubjectProperty(Iterator<KtbsStatement> it, String subject, String property) {
		int cnt = 0;
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			if(ktbsStatement.getSubject().equals(subject) && ktbsStatement.getProperty().equals(property) )
				cnt++;
		}
		return cnt;
	}

	public static int  countSubjectPropertyNS(Iterator<KtbsStatement> it, String subject, String propertyNS) {
		int cnt = 0;
		while (it.hasNext()) {
			KtbsStatement ktbsStatement = it.next();
			if(ktbsStatement.getSubject().equals(subject) && ktbsStatement.getProperty().startsWith(propertyNS))
				cnt++;
		}
		return cnt;
	}

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

	public static KtbsParameter parseParameter(String string) {
		int index = string.indexOf("=");
		String key = string.substring(0, index);
		String value = string.substring(index+1, string.length());
		return new SimpleKtbsParameter(key, value);
	}

}
