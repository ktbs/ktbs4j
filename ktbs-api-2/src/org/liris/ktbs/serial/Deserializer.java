package org.liris.ktbs.serial;

import java.io.Reader;

import org.liris.ktbs.core.ResultSet;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;

public interface Deserializer {
	public IKtbsResource deserializeResource(String uri, Reader reader, String mimeFormat);
	public <T extends IKtbsResource> T deserializeResource(String uri, Reader reader, String mimeFormat, Class<T> cls);
	public <T extends IKtbsResource> ResultSet<T> deserializeResourceSet(Class<T> cls, Reader reader, String mimeFormat);

	public void setDeserializationConfig(DeserializationConfig config);
}
