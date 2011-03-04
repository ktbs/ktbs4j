package org.liris.ktbs.serial.rdf;

import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.dao.ResultSet;
import org.liris.ktbs.domain.ResourceFactory;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.serial.DeserializationConfig;
import org.liris.ktbs.serial.Deserializer;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RdfDeserializer implements Deserializer {

	private static final Log log = LogFactory.getLog(RdfDeserializer.class);
	
	public void setPojoFactory(ResourceFactory pojoFactory) {
		this.pojoFactory = pojoFactory;
	}
	
	public void setProxyFactory(ResourceFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}
	
	private DeserializationConfig deserializationConfig = new DeserializationConfig();
	
	private ResourceFactory pojoFactory;
	private ResourceFactory proxyFactory;
	
	@Override
	public void setDeserializationConfig(DeserializationConfig deserializationConfig) {
		this.deserializationConfig = deserializationConfig;
	}
	
	@Override
	public IKtbsResource deserializeResource(String uri, Reader reader, String mimeFormat) {
		return deserializeResource(uri, reader, "", mimeFormat);
	}

	private Rdf2Java createMapper(Reader reader, String mimeFormat, String baseUri) {
		Model model = ModelFactory.createDefaultModel();
		model.read(reader, baseUri, KtbsUtils.getJenaSyntax(mimeFormat));
		Rdf2Java rdf2Java = new Rdf2Java(model, deserializationConfig, pojoFactory, proxyFactory);
		return rdf2Java;
	}

	@Override
	public <T extends IKtbsResource> ResultSet<T> deserializeResourceSet(Class<T> cls, Reader reader, String baseUri, String mimeFormat) {
		Rdf2Java rdf2Java = createMapper(reader, mimeFormat, baseUri);
		return rdf2Java.getResourceSet(cls);
	}

	@Override
	public <T extends IKtbsResource> T deserializeResource(String uri,
			Reader reader, String mimeFormat, Class<T> cls) {
		return cls.cast(deserializeResource(uri, reader, mimeFormat));
	}

	@Override
	public IKtbsResource deserializeResource(String uri, Reader reader,
			String baseUri, String mimeType) {
		log.info("Deserializing the resource " + uri);
		long start = System.currentTimeMillis();
		
		Rdf2Java rdf2Java = createMapper(reader, mimeType, baseUri);
		IKtbsResource resource = rdf2Java.getResource(uri);
		if(log.isDebugEnabled()) {
			long end = System.currentTimeMillis();
			log.debug("Resource deserialized in " + (end-start) + "ms.");
		}
		
		return resource;
	}

	@Override
	public <T extends IKtbsResource> T deserializeResource(String uri,
			Reader reader, String baseUri, String mimeFormat, Class<T> cls) {
		log.info("Deserializing the resource " + uri);
		long start = System.currentTimeMillis();
		
		Rdf2Java rdf2Java = createMapper(reader, mimeFormat, baseUri);
		T resource = rdf2Java.getResource(uri, cls);
		if(log.isDebugEnabled()) {
			long end = System.currentTimeMillis();
			log.debug("Resource deserialized in " + (end-start) + "ms.");
		}
		
		return resource;
	}
}