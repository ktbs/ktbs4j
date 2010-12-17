package org.liris.ktbs.core.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Relation;

public class RelationImpl implements Relation {

	private static final Log log = LogFactory.getLog(RelationImpl.class);
	
	private String fromObselURI;
	private Obsel fromObsel;
	private Obsel toObsel;
	private String relationName;
	private String toObselURI;
	
	RelationImpl(Obsel fromObsel, Obsel toObsel, String relationName) {
		super();
		this.fromObsel = fromObsel;
		this.toObsel = toObsel;
		this.relationName = relationName;
	}
	
	RelationImpl(String fromObselURI, String toObselURI, String relationName) {
		super();
		this.fromObselURI = fromObselURI;
		this.toObselURI = toObselURI;
		this.relationName = relationName;
	}

	@Override
	public Obsel getFromObsel() {
		return fromObsel;
	}

	@Override
	public String getRelationName() {
		return relationName;
	}

	@Override
	public Obsel getToObsel() {
		return toObsel;
	}
	
	@Override
	public String toString() {
		return "<" + fromObsel + "> " + relationName + " <" + toObsel + ">";
	}

	@Override
	public String getFromObselURI() {
		if(fromObselURI==null) {
			if(this.fromObsel == null) 
				log.warn("A relation should never have null values for both fromObsel and fromObselURI fields at a time.");
			 else 
				fromObselURI = this.fromObsel.getURI();
		}
		return fromObselURI;
	}

	@Override
	public String getToObselURI() {
		if(toObselURI==null) {
			if(this.toObsel == null) 
				log.warn("A relation should never have null values for both toObsel and toObselURI fields at a time.");
			else 
				toObselURI = this.toObsel.getURI();
		}
		return toObselURI;
	}

}
