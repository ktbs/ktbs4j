package org.liris.ktbs.serial;

import java.io.Writer;
import java.util.Set;

import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.rdf.Java2Rdf;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;


public class RdfSerializer implements Serializer {

	private SerializationConfig defaultConfig = new SerializationConfig();

	@Override
	public void setSerializationConfig(SerializationConfig serializationConfig) {
		this.defaultConfig = serializationConfig;
	}

	@Override
	public void serializeResource(Writer writer, IKtbsResource resource,
			String mimeFormat) {
		serializeResource(writer, resource, mimeFormat, defaultConfig);
	}

	private Java2Rdf createMapper(SerializationConfig config) {
		Java2Rdf mapper = new Java2Rdf(config);
		return mapper;
	}

	@Override
	public void serializeResourceSet(Writer writer,
			Set<? extends IKtbsResource> resourceSet, String mimeFormat) {
		serializeResourceSet(writer, resourceSet, mimeFormat, defaultConfig);
	}

	@Override
	public void serializeResource(Writer writer, IKtbsResource resource,
			String mimeFormat, SerializationConfig config) {
		Java2Rdf mapper = createMapper(config);
		Model model = mapper.getModel(resource);
		
		model.write(writer, KtbsUtils.getJenaSyntax(mimeFormat), "");
	}

	@Override
	public void serializeResourceSet(Writer writer,
			Set<? extends IKtbsResource> resourceSet, String mimeFormat,
			SerializationConfig config) {
		
		Java2Rdf mapper = createMapper(config);
		Model model = mapper.getModel(resourceSet);
		model.write(writer, KtbsUtils.getJenaSyntax(mimeFormat), "");
	}
}
