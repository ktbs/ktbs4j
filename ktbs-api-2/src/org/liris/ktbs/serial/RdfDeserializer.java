package org.liris.ktbs.serial;

import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.core.ResultSet;
import org.liris.ktbs.core.domain.ResourceFactory;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.rdf.Rdf2Java;
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
		log.info("Deserializing the resource " + uri);
		long start = System.currentTimeMillis();
		
		Rdf2Java rdf2Java = createMapper(reader, mimeFormat);
		IKtbsResource resource = rdf2Java.getResource(uri);
		if(log.isDebugEnabled()) {
			long end = System.currentTimeMillis();
			log.debug("Resource deserialized in " + (end-start) + "ms.");
		}
		
		return resource;
	}

	private Rdf2Java createMapper(Reader reader, String mimeFormat) {
		Model model = ModelFactory.createDefaultModel();
		model.read(reader, "", KtbsUtils.getJenaSyntax(mimeFormat));
		Rdf2Java rdf2Java = new Rdf2Java(model, deserializationConfig, pojoFactory, proxyFactory);
		return rdf2Java;
	}

	@Override
	public <T extends IKtbsResource> ResultSet<T> deserializeResourceSet(Class<T> cls, Reader reader, String mimeFormat) {
		Rdf2Java rdf2Java = createMapper(reader, mimeFormat);
		return rdf2Java.getResourceSet(cls);
	}

	@Override
	public <T extends IKtbsResource> T deserializeResource(String uri,
			Reader reader, String mimeFormat, Class<T> cls) {
		return cls.cast(deserializeResource(uri, reader, mimeFormat));
	}
	
}
