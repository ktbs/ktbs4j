package org.liris.ktbs.serial;

import java.io.Reader;
import java.io.Writer;

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
	public IKtbsResource deserialize(String uri, Reader reader, String mimeFormat);
	public void serialize(Writer writer, IKtbsResource resource, String mimeFormat);
	public void setOptions(SerializationOptions options);
}
