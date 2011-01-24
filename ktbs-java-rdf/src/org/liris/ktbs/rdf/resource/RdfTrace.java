package org.liris.ktbs.rdf.resource;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.TemporalDomainException;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;

import com.hp.hpl.jena.datatypes.xsd.IllegalDateTimeFieldException;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RdfTrace extends RdfKtbsResource implements Trace {

	public RdfTrace(String uri, Model rdfModel, ResourceRepository holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public KtbsResource get(String resourceURI) {
		return getObsel(resourceURI);
	}

	@Override
	public Iterator<Obsel> listObsels() {
		ResIterator restIt = rdfModel.listResourcesWithProperty(
				rdfModel.getProperty(KtbsConstants.P_HAS_TRACE),
				rdfModel.getResource(uri)); 

		return new ObselIterator(
				restIt, 
				new TemporalCondition() {
					@Override
					public boolean accept(String beginDT, String endDT, BigInteger begin, BigInteger end) {
						return true;
					}
				});
	}

	@Override
	public void setTraceModel(TraceModel traceModel) {
		repository.checkExistency(traceModel);

		removeAllProperties(KtbsConstants.P_HAS_MODEL);
		rdfModel.getResource(uri).addProperty(rdfModel.getProperty(KtbsConstants.P_HAS_MODEL), rdfModel.getResource(traceModel.getURI()));
	}

	@Override
	public Iterator<Obsel> listObsels(final BigInteger paramBegin, final BigInteger paramEnd) {
		ResIterator restIt = rdfModel.listResourcesWithProperty(
				rdfModel.getProperty(KtbsConstants.P_HAS_TRACE),
				rdfModel.getResource(uri)); 

		return new ObselIterator(
				restIt, 
				new TemporalCondition() {
					@Override
					public boolean accept(String beginDT, String endDT,  BigInteger begin, BigInteger end) {
						return begin.compareTo(paramBegin) >= 0 && end.compareTo(paramEnd) <= 0;
					}
				});
	}

	@Override
	public Iterator<Trace> listTransformedTraces() {
		StmtIterator it = rdfModel.listStatements(
				null,
				rdfModel.getProperty(KtbsConstants.P_HAS_SOURCE), 
				rdfModel.getResource(uri));
		return new RdfSubjectIterator<Trace>(
				it, 
				Trace.class,
				repository, 
				false);
	}

	@Override
	public String getOrigin() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_ORIGIN);
		if(l==null)
			return null;
		else
			return l.getString();
	}


	@Override
	public Date getOriginAsDate() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_ORIGIN);
		if(l == null)
			throw new TemporalDomainException("No origin defined");
		try {
			Date asDate = ((XSDDateTime)l.getValue()).asCalendar().getTime();
			return asDate;
		} catch(IllegalDateTimeFieldException e) {
			throw new TemporalDomainException("No origin defined",e);
		}

	}

	private Map<String, Obsel> traceObselCache = new HashMap<String, Obsel>();

	@Override
	public Obsel getObsel(String obselURI) {
		if(traceObselCache.containsKey(obselURI))  
			return traceObselCache.get(obselURI);
		else {
			StmtIterator it = rdfModel.listStatements(
					rdfModel.getResource(obselURI), 
					rdfModel.getProperty(KtbsConstants.P_HAS_TRACE), 
					rdfModel.getResource(uri));
			if(it.hasNext()) {

				Obsel obsel = repository.getResource(obselURI, Obsel.class);
				traceObselCache.put(obselURI, obsel);
				return obsel;
			} else
				return null;
		}
	}

	@Override
	public TraceModel getTraceModel() {
		Resource r = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_MODEL);
		if(r==null)
			return null;
		else
			return repository.getResource(r.getURI(), TraceModel.class);
	}


	@Override
	public boolean isCompliantWithModel() {
		Statement stmt = rdfModel.getProperty(
				rdfModel.getResource(uri),
				rdfModel.getProperty(KtbsConstants.P_COMPLIES_WITH_MODEL));

		if(stmt == null || stmt.getObject()==null)
			return false;
		String string = stmt.getObject().asLiteral().getString();
		if(string.equals("yes"))
			return true;
		else
			return false;
	}


	private interface TemporalCondition {
		public boolean accept(String beginDT, String endDT, BigInteger begin, BigInteger end);
	}

	private class ObselIterator implements Iterator<Obsel> {

		private ResIterator resIt;
		private RdfTrace.TemporalCondition temporalCondition;

		private Obsel next;
		ObselIterator(ResIterator stmtIt, TemporalCondition temporalCondition) {
			super();
			this.resIt = stmtIt;
			this.temporalCondition = temporalCondition;
			doNext();
		}

		private void doNext() {
			boolean foundNext = false;
			while(resIt.hasNext() && !foundNext) {
				Resource res = resIt.next();
				String obselURI = res.getURI();
				next = RdfTrace.this.repository.getResource(
						obselURI, 
						Obsel.class);
				if(temporalCondition.accept(
						next.getBeginDT(), 
						next.getEndDT(), 
						next.getBegin(), 
						next.getEnd() 
				)) 
					foundNext = true;

				RdfTrace.this.traceObselCache.put(obselURI, next);
			} 
			if(!foundNext)
				next = null;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public Obsel next() {
			Obsel toBeReturned = next;
			doNext();
			return toBeReturned;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot use iterator to remove obsels from a trace.");
		}
	}

	@Override
	public void setOrigin(String origin) {
		removeAllAndAddTypedLiteral(KtbsConstants.P_HAS_ORIGIN, origin, XSDDatatype.XSDdateTime);
	}

	@Override
	public void setOriginAsDate(Date origin) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(origin);
		XSDDateTime x = new XSDDateTime(cal);
		removeAllAndAddLiteral(KtbsConstants.P_HAS_ORIGIN, x);
	}
}
