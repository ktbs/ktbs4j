package org.liris.ktbs.rdf.resource;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsResourceHolder;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.core.TemporalDomainException;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.datatypes.xsd.IllegalDateTimeFieldException;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public abstract class KtbsJenaTrace extends KtbsJenaResource implements Trace {

	KtbsJenaTrace(String uri, Model rdfModel, KtbsResourceHolder holder) {
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
					public boolean accept(String beginDT, String endDT, long begin, long end) {
						return true;
					}
				});
	}
	
	@Override
	public void setTraceModel(TraceModel traceModel) {
		TraceModel tm = holder.putResource(traceModel);
		
		removeAllProperties(KtbsConstants.P_HAS_MODEL);
		rdfModel.getResource(uri).addProperty(rdfModel.getProperty(KtbsConstants.P_HAS_MODEL), rdfModel.getResource(tm.getURI()));
	}

	@Override
	public Iterator<Obsel> listObsels(final long paramBegin, final long paramEnd) {
		ResIterator restIt = rdfModel.listResourcesWithProperty(
				rdfModel.getProperty(KtbsConstants.P_HAS_TRACE),
				rdfModel.getResource(uri)); 
		
		return new ObselIterator(
				restIt, 
				new TemporalCondition() {
					@Override
					public boolean accept(String beginDT, String endDT, long begin, long end) {
						return begin>=paramBegin && end <= paramEnd;
					}
				});
	}



	@Override
	public Iterator<Trace> listTransformedTraces() {
		StmtIterator it = rdfModel.listStatements(
				null,
				rdfModel.getProperty(KtbsConstants.P_HAS_SOURCE), 
				rdfModel.getResource(uri));
		return new KtbsResourceSubjectIterator<Trace>(
				it, 
				Trace.class,
				holder);
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
				
				Obsel obsel = holder.getResourceAlreadyInModel(obselURI, Obsel.class, rdfModel);
				traceObselCache.put(obselURI, obsel);
				return obsel;
			} else
				return null;
		}
	}


	@Override
	public Base getBase() {
		StmtIterator it = rdfModel.listStatements(
				null, 
				rdfModel.getProperty(KtbsConstants.P_OWNS), 
				rdfModel.getResource(uri));
		if(it.hasNext()) {
			String baseURI = it.nextStatement().getSubject().asResource().getURI();
			return holder.getResource(baseURI, Base.class);
		} else
			return null;
	}

	@Override
	public TraceModel getTraceModel() {
		Resource r = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_MODEL);
		if(r==null)
			return null;
		else
			return holder.getResource(r.getURI(), TraceModel.class);
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
		public boolean accept(String beginDT, String endDT, long begin, long end);
	}

	private class ObselIterator implements Iterator<Obsel> {

		private ResIterator resIt;
		private KtbsJenaTrace.TemporalCondition temporalCondition;

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
				next = KtbsJenaTrace.this.holder.getResourceAlreadyInModel(
						obselURI, 
						Obsel.class, 
						KtbsJenaTrace.this.rdfModel);
				if(temporalCondition.accept(
						next.getBeginDT(), 
						next.getEndDT(), 
						next.getBegin(), 
						next.getEnd() 
				)) 
					foundNext = true;

				KtbsJenaTrace.this.traceObselCache.put(obselURI, next);
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
			throw new ReadOnlyObjectException(KtbsJenaTrace.this);
		}
	}
	
	
	@Override
	public void setOrigin(String origin) {
		removeAllAndAddLiteral(KtbsConstants.P_HAS_ORIGIN, origin);
	}
	
	@Override
	public void setOriginAsDate(Date origin) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(origin);
		
		removeAllAndAddLiteral(KtbsConstants.P_HAS_ORIGIN, new XSDDateTime(cal));
	}
	
	@Override
	public void setCompliantWithModel(boolean compliant) {
		removeAllAndAddLiteral(KtbsConstants.P_COMPLIES_WITH_MODEL, compliant);
	}
}
