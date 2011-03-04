package org.liris.ktbs.serial;

import java.io.Writer;
import java.util.Collection;

import org.liris.ktbs.core.domain.interfaces.IKtbsResource;

public interface Serializer {
	
	public void serializeResource(Writer writer, IKtbsResource resource, String mimeFormat, SerializationConfig config);
	public void serializeResource(Writer writer, IKtbsResource resource, String mimeFormat);
	public void serializeResourceSet(Writer writer, Collection<? extends IKtbsResource> resourceSet, String mimeFormat, SerializationConfig config);
	public void serializeResourceSet(Writer writer, Collection<? extends IKtbsResource> resourceSet, String mimeFormat);
	
	public void setSerializationConfig(SerializationConfig config);
}
