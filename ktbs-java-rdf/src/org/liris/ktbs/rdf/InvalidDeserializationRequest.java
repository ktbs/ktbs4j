package org.liris.ktbs.rdf;

public class InvalidDeserializationRequest extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Class<?> requiredResourceClass;
	private String traceAspect;
	private String[] associatedCompliantRDFTypes;
	private String actualRDFType;

	InvalidDeserializationRequest(Class<?> requiredResourceClass,
			String traceAspect, String actualRDFType, String... associatedCompliantRDFTypes) {
		super();
		this.requiredResourceClass = requiredResourceClass;
		this.associatedCompliantRDFTypes = associatedCompliantRDFTypes;
		this.actualRDFType = actualRDFType;
		this.traceAspect = traceAspect;
	}

	@Override
	public String getMessage() {
		String associatedCompliantRDFTypes = "";
		for(int i = 0; i<this.associatedCompliantRDFTypes.length; i++) 
			associatedCompliantRDFTypes+=(i>0?", ":"")+ this.associatedCompliantRDFTypes[i];
		return "Resource class: " + requiredResourceClass + ", traceAspect: " + traceAspect + ", accepted rdf types: [" + associatedCompliantRDFTypes + "], actual rdf:type: " + actualRDFType;
	}
}
