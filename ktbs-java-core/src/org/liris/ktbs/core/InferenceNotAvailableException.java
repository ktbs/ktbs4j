package org.liris.ktbs.core;

@SuppressWarnings("serial")
public class InferenceNotAvailableException extends RuntimeException {
	
	private String reason;
	private KtbsResource resource;



	public InferenceNotAvailableException(String reason, KtbsResource resource) {
		super();
		this.reason = reason;
		this.resource = resource;
	}

	public String getReason() {
		return reason;
	}
	public KtbsResource getResource() {
		return resource;
	}

	@Override
	public String getMessage() {
		return "Not possible to perform inference on the resource \""+resource.getURI()+"\". " + reason;
	}

}
