package org.liris.ktbs.visu2;

public class Obsel {
	
	private long id;
	private String rdfText;
	private String type;
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRdfText() {
		return rdfText;
	}

	public void setRdfText(String rdfText) {
		this.rdfText = rdfText;
	}
}
