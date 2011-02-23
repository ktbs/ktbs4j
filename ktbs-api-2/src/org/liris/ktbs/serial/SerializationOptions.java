package org.liris.ktbs.serial;

public class SerializationOptions {
	
	private boolean withContainedResourceURI = true;
	private boolean withContainedResourceType = false;
	private boolean withContainedResource = false;
	private boolean withLinkedResourceURI = true;
	private boolean withLinkedResourceType = false;
	private boolean withLinkedResource = false;
	
	public boolean isWithContainedResourceURI() {
		return withContainedResourceURI;
	}

	public void setWithContainedResourceURI(boolean withContainedResourceURI) {
		this.withContainedResourceURI = withContainedResourceURI;
	}

	public boolean isWithContainedResourceType() {
		return withContainedResourceType;
	}

	public void setWithContainedResourceType(boolean withContainedResourceType) {
		if(withContainedResourceType)
			setWithContainedResourceURI(true);
		this.withContainedResourceType = withContainedResourceType;
	}

	public boolean isWithContainedResource() {
		return withContainedResource;
	}

	public void setWithContainedResource(boolean withContainedResource) {
		if(withContainedResource)
			setWithContainedResourceURI(true);
		this.withContainedResource = withContainedResource;
	}

	public boolean isWithLinkedResourceURI() {
		return withLinkedResourceURI;
	}

	public void setWithLinkedResourceURI(boolean withLinkedResourceURI) {
		this.withLinkedResourceURI = withLinkedResourceURI;
	}

	public boolean isWithLinkedResourceType() {
		return withLinkedResourceType;
	}

	public void setWithLinkedResourceType(boolean withLinkedResourceType) {
		if(withLinkedResourceType)
			setWithContainedResourceURI(true);
		this.withLinkedResourceType = withLinkedResourceType;
	}

	public boolean isWithLinkedResource() {
		return withLinkedResource;
	}

	public void setWithLinkedResource(boolean withLinkedResource) {
		if(withLinkedResource)
			setWithContainedResourceURI(true);
		this.withLinkedResource = withLinkedResource;
	}
}
