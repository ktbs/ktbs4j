package org.liris.ktbs.serial;

import java.io.Reader;
import java.io.Writer;

import org.liris.ktbs.core.ResultSet;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;

public interface KtbsResourceSerializer {
	
	/**
	 * The uri of the resource to deserialize
	 * 
	 * @param uri
	 * @param reader
	 * @param mimeFormat
	 * @return
	 */
	public void serialize(Writer writer, IKtbsResource resource, String mimeFormat);
	public IKtbsResource deserializeResource(String uri, Reader reader, String mimeFormat);
	public <T extends IKtbsResource> T deserializeResource(String uri, Reader reader, String mimeFormat, Class<T> cls);
	public <T extends IKtbsResource> ResultSet<T> deserializeResourceSet(Class<T> cls, Reader reader, String mimeFormat);

	public SerializationConfig getSerializationConfig();
	public void setSerializationConfig(SerializationConfig config);

	public void setDeserializationConfig(DeserializationConfig config);
	public DeserializationConfig getDeserializationConfig();
}
