package org.liris.ktbs.domain;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IResourceContainer;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.utils.KtbsUtils;

@SuppressWarnings("serial")
public abstract class ResourceContainer<T extends IKtbsResource> extends KtbsResource implements IResourceContainer<T> {

	private class ConcatenatingIterator implements Iterator<T> {

		private Iterator<? extends T> currentIterator;
		private Iterator<? extends Collection<? extends T>> collectionIterator;

		public ConcatenatingIterator(Collection<? extends Collection<? extends T>> c) {
			super();
			collectionIterator = c.iterator();
			currentIterator = collectionIterator.next().iterator();
			doNext();
		}


		private T next = null;

		private void doNext() {
			next = null;
			if(currentIterator.hasNext()) 
				next = currentIterator.next();
			else {
				if(collectionIterator.hasNext()) {
					currentIterator = collectionIterator.next().iterator();
					doNext();
				}
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public T next() {
			if(next == null)
				throw new NoSuchElementException();
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
		String absoluteUri = KtbsUtils.makeAbsoluteURI(getUri(), resourceURI, isChildALeaf());
		
		Iterator<T> it = newConcatenatingIterator();
		while (it.hasNext()) {
			T t = it.next();
			if(t.getUri().equals(absoluteUri)) 
				return t;
		}
		return null;
	}

	@Override
	public <U extends T> U get(String resourceURI, Class<U> cls) {
		T resource = get(resourceURI);
		if(resource == null)
			return null;
		else 
			return cls.cast(resource);
	}
	
	private boolean isChildALeaf() {
		boolean isTrace = ITrace.class.isAssignableFrom(getClass());
		boolean isTraceModel = ITraceModel.class.isAssignableFrom(getClass());
		return isTrace || isTraceModel;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IResourceContainer#delete(java.lang.String)
	 */
	@Override
	public boolean delete(String resourceURI) {
		boolean delete = false;
		Iterator<T> it = newConcatenatingIterator();
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
		return newConcatenatingIterator();
	}

	private ConcatenatingIterator newConcatenatingIterator() {
		return new ConcatenatingIterator(getContainedResourceCollections());
	}

	protected abstract Collection<? extends Collection<? extends T>> getContainedResourceCollections();

	@Override
	public Iterator<T> iterator() {
		return newConcatenatingIterator();
	}
}
