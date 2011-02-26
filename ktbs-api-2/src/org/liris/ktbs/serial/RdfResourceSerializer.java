package org.liris.ktbs.serial;

import java.io.Reader;
import java.io.Writer;
import java.util.Set;

import org.liris.ktbs.core.ResultSet;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.rdf.Java2Rdf;
import org.liris.ktbs.rdf.Rdf2Java;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RdfResourceSerializer implements KtbsResourceSerializer {

	private DeserializationConfig deserializationConfig;
	private SerializationConfig serializationConfig;
	
	public RdfResourceSerializer(DeserializationConfig deserializationConfig,
			SerializationConfig serializationConfig) {
		super();
		this.deserializationConfig = deserializationConfig;
		this.serializationConfig = serializationConfig;
	}

	public RdfResourceSerializer() {
		super();
	}

	@Override
	public DeserializationConfig getDeserializationConfig() {
		return deserializationConfig;
	}

	@Override
	public void setDeserializationConfig(DeserializationConfig deserializationConfig) {
		this.deserializationConfig = deserializationConfig;
	}

	@Override
	public SerializationConfig getSerializationConfig() {
		return serializationConfig;
	}

	@Override
	public void setSerializationConfig(SerializationConfig serializationConfig) {
		this.serializationConfig = serializationConfig;
	}

	@Override
	public IKtbsResource deserializeResource(String uri, Reader reader, String mimeFormat) {
		Model model = ModelFactory.createDefaultModel();
		model.read(reader, "", KtbsUtils.getJenaSyntax(mimeFormat));
			
		Rdf2Java rdf2Java = new Rdf2Java(model, deserializationConfig);
		return rdf2Java.getResource(uri);
	}

	@Override
	public <T extends IKtbsResource> ResultSet<T> deserializeResourceSet(Class<T> cls, Reader reader, String mimeFormat) {
		Model model = ModelFactory.createDefaultModel();
		model.read(reader, "", KtbsUtils.getJenaSyntax(mimeFormat));
		
		Rdf2Java rdf2Java = new Rdf2Java(model, deserializationConfig);
		return rdf2Java.getResourceSet(cls);
	}

	@Override
	public void serializeResource(Writer writer, IKtbsResource resource,
			String mimeFormat) {
		
		Java2Rdf mapper = new Java2Rdf();
		Model model = mapper.getModel(resource);
		
		model.write(writer, KtbsUtils.getJenaSyntax(mimeFormat), "");
	}

	@Override
	public void serializeResourceSet(Writer writer,
			Set<? extends IKtbsResource> resourceSet, String mimeFormat) {
		
		Java2Rdf mapper = new Java2Rdf();
		Model model = mapper.getModel(resourceSet);
		model.write(writer, KtbsUtils.getJenaSyntax(mimeFormat), "");
	}
	
	@Override
	public <T extends IKtbsResource> T deserializeResource(String uri,
			Reader reader, String mimeFormat, Class<T> cls) {
		return cls.cast(deserializeResource(uri, reader, mimeFormat));
	}
}
