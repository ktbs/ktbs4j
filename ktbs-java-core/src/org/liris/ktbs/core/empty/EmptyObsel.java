package org.liris.ktbs.core.empty;

import java.util.Date;
import java.util.Iterator;

import org.liris.ktbs.core.AttributeStatement;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationStatement;
import org.liris.ktbs.core.RelationType;
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
	public Iterator<RelationStatement> listIncomingRelations() {
		throw new UnsupportedOperationException(MESSAGE);
	}
		
	@Override
	public Iterator<RelationStatement> listOutgoingRelations() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Obsel getTargetObsel(RelationType relationName) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Obsel getSourceObsel(RelationType relationName) {
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

	@Override
	public Obsel getSourceObsel() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setBegin(long begin) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setEnd(long end) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setBeginDT(String beginDT) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setEndDT(String endDT) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setObselType(ObselType type) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setSubject(String subject) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void addAttribute(AttributeType attribute, Object value) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void addOutgoingRelation(RelationType relationType, Obsel target) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void addIncomingRelation(Obsel source, RelationType relationType) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setBeginDTAsDate(Date date) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setEndDTAsDate(Date date) {
		throw new UnsupportedOperationException(MESSAGE);
	}
}

