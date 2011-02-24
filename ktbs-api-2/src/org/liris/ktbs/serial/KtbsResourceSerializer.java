package org.liris.ktbs.serial;

import java.io.Reader;
import java.io.Writer;

import org.liris.ktbs.core.pojo.ResourcePojo;

public interface KtbsResourceSerializer {
	
	/**
	 * The uri of the resource to deserialize
	 * 
	 * @param uri
	 * @param reader
	 * @param mimeFormat
	 * @return
	 */
	public ResourcePojo deserialize(String uri, Reader reader, String mimeFormat);
	public void serialize(Writer writer, ResourcePojo resource, String mimeFormat);
	public void setOptions(SerializationOptions options);
}
