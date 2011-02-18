package org.liris.ktbs.serial;

import java.io.Reader;
import java.io.Writer;

import org.liris.ktbs.core.api.share.KtbsResource;

public interface KtbsResourceDeserializer {
	
	public KtbsResource deserialize(Reader reader, String mimeFormat);
	public void serialize(Writer writer, KtbsResource resource, String mimeFormat);
}
