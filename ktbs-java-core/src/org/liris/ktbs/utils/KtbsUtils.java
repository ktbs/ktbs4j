package org.liris.ktbs.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.liris.ktbs.core.KtbsStatement;

public class KtbsUtils {

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
			Object o = it.next();
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

}
