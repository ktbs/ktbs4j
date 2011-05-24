package org.liris.ktbs.serial;

import java.io.Reader;

import org.liris.ktbs.dao.ResultSet;
import org.liris.ktbs.domain.interfaces.IKtbsResource;

import com.hp.hpl.jena.rdf.model.Model;

public interface Deserializer {
	public IKtbsResource deserializeResource(String uri, Reader reader, String mimeFormat);
	public IKtbsResource deserializeResource(String uri, Reader reader,
			String baseUri, String mimeType);
	
	public <T extends IKtbsResource> T deserializeResource(String uri, Reader reader, String mimeFormat, Class<T> cls);
	public <T extends IKtbsResource> T deserializeResource(String uri, Reader reader, String baseUri, String mimeFormat, Class<T> cls);
	
	public <T extends IKtbsResource> ResultSet<T> deserializeResourceSet(Class<T> cls, Reader reader, String baseUri, String mimeFormat);

	public void setDeserializationConfig(DeserializationConfig config);
	public Model getLastDeserializedModel();
}
