package org.liris.ktbs.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A map key by three {@link String}.
 * 
 * @author Damien Cram
 *
 */
public class ThreeKeyedMap<V> {

	private class Key {
		private String s1;
		private String s2;
		private String s3;

		
		public Key(String s1, String s2, String s3) {
			super();
			this.s1 = s1;
			this.s2 = s2;
			this.s3 = s3;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ThreeKeyedMap.Key) {
				Key k = (Key) obj;
				return s1.equals(k.s1)
				&& s2.equals(s2)
				&& s3.equals(s3);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return s1.hashCode()*s2.hashCode()*s3.hashCode();
		}
	};
	
	private Map<Key, V> map = new HashMap<Key, V>();
	
	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public V get(String s1, String s2, String s3) {
		return map.get(new Key(s1,s2,s3));
	}

	public V put(String s1, String s2, String s3, V value) {
		return map.put(new Key(s1,s2,s3), value);
	}

	public void clear() {
		map.clear();
	}
}
