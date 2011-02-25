package org.liris.ktbs.serial;

import java.io.Reader;
import java.io.Writer;

import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.rdf.Java2Rdf;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;

public class RdfResourceSerializer implements KtbsResourceSerializer {

	@Override
	public IKtbsResource deserialize(String uri, Reader reader, String mimeFormat) {
		return null;
	}

	@Override
	public void serialize(Writer writer, IKtbsResource resource,
			String mimeFormat) {
		
		Java2Rdf mapper = new Java2Rdf(resource);
		Model model = mapper.getModel();
		
		model.write(writer, KtbsUtils.getJenaSyntax(mimeFormat), "");
	}

	@Override
	public void setOptions(SerializationOptions options) {
		// TODO Auto-generated method stub
		
	}
}
