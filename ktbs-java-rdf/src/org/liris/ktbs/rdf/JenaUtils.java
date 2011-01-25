package org.liris.ktbs.rdf;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.liris.ktbs.core.JenaConstants;

import com.hp.hpl.jena.rdf.model.Alt;
import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Container;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.Seq;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class JenaUtils {

	public static String toTurtleString(Model rdfModel) {
		StringWriter writer = new StringWriter();
		rdfModel.write(writer, JenaConstants.TURTLE, "");
		return writer.toString();
	}

	/**
	 * Creates a new model with only the statement selected by a selector.
	 * 
	 * @param model the input Jena model
	 * @param selector the Jena selector that selects the statements to be kept
	 * @return the filtered model
	 */
	public static Model filterModel(Model model, Selector selector) {
		Model filteredModel = ModelFactory.createDefaultModel();

		filteredModel.add(model);

		StmtIterator it = filteredModel.listStatements();

		while(it.hasNext()) {
			Statement s = it.next();
			if(!selector.test(s))
				it.remove();
		}

		return filteredModel;
	}


	public static boolean isCollection(Resource r) {
		return !getCollectionType(r).equals(CollectionType.UNKOWN);
	}

	private static final class RDFLinkedListIterator implements
	Iterator<Object> {
		private Resource currentResource;

		private RDFLinkedListIterator(Resource r) {
			this.currentResource = r;
		}

		@Override
		public boolean hasNext() {
			String uri = this.currentResource.getURI();
			return uri == null || !uri.equals(RDF.nil.getURI());
		}

		@Override
		public Object next() {
			Statement stmt = currentResource.getProperty(RDF.first);
			Object obj = asJavaObject(stmt.getObject());
			stmt = currentResource.getProperty(RDF.rest);
			this.currentResource = stmt.getObject().asResource();
			return obj;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}


	private enum CollectionType {LINKED_LIST, BAG, ALT, SEQ, UNKOWN}

	private static CollectionType getCollectionType(Resource r) {
		Statement stmt = r.getProperty(RDF.type);
		if(stmt == null)  {
			stmt = r.getProperty(RDF.first);
			if(stmt == null)
				return CollectionType.UNKOWN;
			else
				return CollectionType.LINKED_LIST;
		} else {
			if(!stmt.getObject().isResource())
				return CollectionType.UNKOWN;
			else {
				String uri = stmt.getObject().asResource().getURI();
				if(uri.equals(RDF.Bag.getURI()))
					return CollectionType.BAG;
				else if(uri.equals(RDF.Alt.getURI()))
					return CollectionType.ALT;
				else if(uri.equals(RDF.Seq.getURI()))
					return CollectionType.SEQ;
				else
					return CollectionType.UNKOWN;
			}
		}
	}

	public static Collection<RDFNode> asJenaLiteral(Model model, Object value) {
		Collection<RDFNode> nodes = new HashSet<RDFNode>();
		
		if (value instanceof Iterable<?>) {
			Iterable<?> iterable = (Iterable<?>) value;
			Iterator<?> it = iterable.iterator();
			while (it.hasNext()) {
				Object object = (Object) it.next();
				nodes.addAll(asJenaLiteral(model, object));
			}
		} else {
			nodes.add(model.createTypedLiteral(value));
		}
		
		return nodes;
	}
	
	/**
	 * Translate the RDF node to a java object.
	 * 
	 * @param node the RDF node to be transformed
	 * @return the Java object representation of that node
	 */
	public static Object asJavaObject(RDFNode node) {
		if(node == null)
			return null;
		if(node.isLiteral()) {
			Literal lit = node.asLiteral();
			if(lit.getDatatypeURI() != null)
				// if the value already have a data type defined, let Jena parse it properly
				return lit.getValue();
			Object value = lit.getValue();
			try {
				return new BigInteger(value.toString());
			} catch(NumberFormatException e) {
				// could not be parsed as a BigInteger
				return value;
			}
		}
		if(node.isResource()) {
			Resource asResource = node.asResource();
			if(getCollectionType(asResource).equals(CollectionType.UNKOWN))
				return asResource.getURI();
			else
				return toIterable(asResource);
		}
		return null;
	}

	public static Iterable<?> toIterable(final Resource r) {
		if(!isCollection(r))
			throw new IllegalStateException(r.getURI() + " is not an RDF collection.");

		final Iterator<Object> it;
		CollectionType cType = getCollectionType(r);
		if(cType.equals(CollectionType.SEQ) 
				|| cType.equals(CollectionType.ALT) 
				|| cType.equals(CollectionType.BAG) 
		) {
			Container c = null;
			if(cType == CollectionType.SEQ)
				c = r.as(Seq.class);
			if(cType == CollectionType.ALT)
				c = r.as(Alt.class);
			if(cType == CollectionType.BAG)
				c = r.as(Bag.class);

			final NodeIterator nodeIterator = c.iterator();

			it = new Iterator<Object>() {
				@Override
				public boolean hasNext() {
					return nodeIterator.hasNext();
				}
				@Override
				public Object next() {
					return asJavaObject(nodeIterator.next());
				}
				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		} else {
			// LINKED_LIST

			it = new RDFLinkedListIterator(r);
		}


		return new Iterable<Object>() {
			@Override
			public Iterator<Object> iterator() {
				return it;
			}
		};
	}

}
