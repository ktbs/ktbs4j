package org.liris.ktbs.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class RdfModelResource {
	
	private Model model;
	private String uri;
	
	public void addLiteral(String pName, Object value) {
		model.getResource(uri).addLiteral(model.getProperty(pName), value);
	}

	public Resource addResource(String pName, String resourceURI) {
		return model.getResource(uri).addProperty(
				model.getProperty(pName),
				model.getResource(resourceURI));
	}
	
	public Model getModel() {
		return model;
	}
	
	public String getUri() {
		return uri;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RdfModelResource) {
			RdfModelResource rmr = (RdfModelResource) obj;
			return rmr.uri.equals(uri);
		}
		return false;
	}
}
