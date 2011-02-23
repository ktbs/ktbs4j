package org.liris.ktbs.serial;

import java.io.Reader;
import java.io.Writer;

import org.liris.ktbs.core.api.KtbsResource;

public interface KtbsResourceSerializer {
	
	/**
	 * 
	 * @param uri the uri of the resource to be deserialized
	 * @param reader
	 * @param mimeFormat
	 * @return
	 */
	public KtbsResource deserialize(String uri, Reader reader, String mimeFormat);
	public void serialize(Writer writer, KtbsResource resource, String mimeFormat);
}
