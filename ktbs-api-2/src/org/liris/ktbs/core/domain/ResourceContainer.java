package org.liris.ktbs.core.domain;

import java.util.Collection;
import java.util.Iterator;

import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IResourceContainer;

public abstract class ResourceContainer<T extends IKtbsResource> extends KtbsResource implements IResourceContainer<T> {

	private class ConcatenatingIterator implements Iterator<T> {

		private Iterator<? extends T> currentIterator;
		private Iterator<? extends Collection<? extends T>> collectionIterator;

		public ConcatenatingIterator(Collection<? extends Collection<? extends T>> c) {
			super();
			collectionIterator = c.iterator();
			currentIterator = collectionIterator.next().iterator();
		}


		private T next;

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
		public T next() {
			T t = next;
			doNext();
			return t;
		}

		@Override
		public void remove() {
			currentIterator.remove();
		}
	};

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IResourceContainer#get(java.lang.String)
	 */
	@Override
	public T get(String resourceURI) {
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			T t = it.next();
			if(t.getUri().equals(resourceURI)) 
				return t;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IResourceContainer#delete(java.lang.String)
	 */
	@Override
	public boolean delete(String resourceURI) {
		boolean delete = false;
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			T t = it.next();
			if(t.getUri().equals(resourceURI)) {
				it.remove();
				delete = true;
			}
		}
		return delete;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IResourceContainer#listResources()
	 */
	@Override
	public Iterator<T> listResources() {
		return iterator();
	}

	private ConcatenatingIterator iterator() {
		return new ConcatenatingIterator(getContainedResourceCollections());
	}

	protected abstract Collection<? extends Collection<? extends T>> getContainedResourceCollections();

}
