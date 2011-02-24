package org.liris.ktbs.serial;

import java.io.Reader;
import java.io.Writer;

import org.liris.ktbs.core.pojo.ResourcePojo;
import org.liris.ktbs.rdf.Resource2RdfModel;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;

public class RdfResourceSerializer implements KtbsResourceSerializer {

	@Override
	public ResourcePojo deserialize(String uri, Reader reader, String mimeFormat) {
		return null;
	}

	@Override
	public void serialize(Writer writer, ResourcePojo resource,
			String mimeFormat) {
		
		Resource2RdfModel mapper = new Resource2RdfModel(resource);
		Model model = mapper.getModel();
		
		model.write(writer, KtbsUtils.getJenaSyntax(mimeFormat), "");
	}

	@Override
	public void setOptions(SerializationOptions options) {
		// TODO Auto-generated method stub
		
	}
}
