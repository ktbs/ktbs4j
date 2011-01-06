package org.liris.ktbs.core.empty;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.liris.ktbs.core.AttributeStatement;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.KtbsStatement;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;

public class EmptyObsel extends EmptyResource implements Obsel {

	EmptyObsel(String uri) {
		super(uri);
	}

	@Override
	public long getBegin() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public long getEnd() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getBeginDT() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getEndDT() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public ObselType getObselType() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getSubject() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void addOutgoingRelation(Relation relation) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void addIncomingRelation(Relation relation) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Trace getTrace() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<AttributeStatement> listAttributes() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Object getAttributeValue(AttributeType attribute) {
		throw new UnsupportedOperationException(MESSAGE);
	}
		
	@Override
	public Iterator<Relation> listIncomingRelations() {
		throw new UnsupportedOperationException(MESSAGE);
	}
		
	@Override
	public Iterator<Relation> listOutgoingRelations() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Obsel getTargetObsel(String relationName) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Obsel getSourceObsel(String relationName) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Date getBeginDTAsDate() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Date getEndDTAsDate() {
		throw new UnsupportedOperationException(MESSAGE);
	}
}
