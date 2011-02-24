package org.liris.ktbs.core.pojo;

import java.util.Collection;
import java.util.Iterator;

public abstract class ResourceContainerPojo<T extends ResourcePojo> extends ResourcePojo {

	private class ConcatenatingIterator<X> implements Iterator<X> {

		private Iterator<X> currentIterator;
		private Iterator<Collection<X>> collectionIterator;

		public ConcatenatingIterator(Collection<Collection<X>> c) {
			super();
			collectionIterator = c.iterator();
			currentIterator = collectionIterator.next().iterator();
		}

		private X next;

		private void doNext() {
			if(!currentIterator.hasNext()) {
				if(!collectionIterator.hasNext()){
					next = null;
					return;
				} else
					currentIterator = collectionIterator.next().iterator();
			}
			
			next = currentIterator.next();
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public X next() {
			X x = next;
			doNext();
			return x;
		}

		@Override
		public void remove() {
			currentIterator.remove();
		}
	};

	public T get(String resourceURI) {
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			T t = (T) it.next();
			if(t.getUri().equals(resourceURI)) 
				return t;
		}
		return null;
	}

	public boolean delete(String resourceURI) {
		boolean delete = false;
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			T t = (T) it.next();
			if(t.getUri().equals(resourceURI)) {
				it.remove();
				delete = true;
			}
		}
		return delete;
	}

	public Iterator<T> listResources() {
		return iterator();
	}

	private ConcatenatingIterator<T> iterator() {
		return new ConcatenatingIterator<T>(getContainedResourceCollections());
	}

	protected abstract Collection<Collection<T>> getContainedResourceCollections();

}
