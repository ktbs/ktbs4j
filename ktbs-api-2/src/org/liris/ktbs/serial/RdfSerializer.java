package org.liris.ktbs.serial;

import java.io.Writer;
import java.util.Set;

import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.rdf.Java2Rdf;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;


public class RdfSerializer implements Serializer {

	private SerializationConfig serializationConfig = new SerializationConfig();

	@Override
	public void setSerializationConfig(SerializationConfig serializationConfig) {
		this.serializationConfig = serializationConfig;
	}

	@Override
	public void serializeResource(Writer writer, IKtbsResource resource,
			String mimeFormat) {
		
		Java2Rdf mapper = createMapper();
		Model model = mapper.getModel(resource);
		
		model.write(writer, KtbsUtils.getJenaSyntax(mimeFormat), "");
	}

	private Java2Rdf createMapper() {
		Java2Rdf mapper = new Java2Rdf(serializationConfig);
		mapper.setConfig(serializationConfig);
		return mapper;
	}

	@Override
	public void serializeResourceSet(Writer writer,
			Set<? extends IKtbsResource> resourceSet, String mimeFormat) {
		
		Java2Rdf mapper = createMapper();
		Model model = mapper.getModel(resourceSet);
		model.write(writer, KtbsUtils.getJenaSyntax(mimeFormat), "");
	}
}
