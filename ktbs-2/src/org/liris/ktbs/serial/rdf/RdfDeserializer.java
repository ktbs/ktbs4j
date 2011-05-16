package org.liris.ktbs.serial.rdf;

import java.io.Reader;

import org.liris.ktbs.dao.ProxyFactory;
import org.liris.ktbs.dao.ResultSet;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.serial.DeserializationConfig;
import org.liris.ktbs.serial.Deserializer;
import org.liris.ktbs.utils.KtbsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RdfDeserializer implements Deserializer {

	private static final Logger logger = LoggerFactory.getLogger(RdfDeserializer.class);
	
	public void setPojoFactory(PojoFactory pojoFactory) {
		this.pojoFactory = pojoFactory;
	}
	
	public void setProxyFactory(ProxyFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}
	
	private DeserializationConfig deserializationConfig = new DeserializationConfig();
	
	private PojoFactory pojoFactory;
	private ProxyFactory proxyFactory;
	
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
		this.lastDeserializedModel = model;
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
		logger.info("Deserializing the resource " + uri);
		long start = System.currentTimeMillis();
		
		Rdf2Java rdf2Java = createMapper(reader, mimeType, baseUri);
		IKtbsResource resource = rdf2Java.getResource(uri);
		if(logger.isDebugEnabled()) {
			long end = System.currentTimeMillis();
			logger.debug("Resource deserialized in " + (end-start) + "ms.");
		}
		
		return resource;
	}

	@Override
	public <T extends IKtbsResource> T deserializeResource(String uri,
			Reader reader, String baseUri, String mimeFormat, Class<T> cls) {
		logger.info("Deserializing the resource " + uri);
		long start = System.currentTimeMillis();
		
		Rdf2Java rdf2Java = createMapper(reader, mimeFormat, baseUri);
		T resource = rdf2Java.getResource(uri, cls);
		if(logger.isDebugEnabled()) {
			long end = System.currentTimeMillis();
			logger.debug("Resource deserialized in " + (end-start) + "ms.");
		}
		return resource;
	}

	private Model lastDeserializedModel;
	
	@Override
	public Model getLastDeserializedModel() {
		return lastDeserializedModel;
	}
}
