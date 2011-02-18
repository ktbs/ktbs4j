package org.liris.ktbs.serial;

import java.io.Reader;
import java.io.Writer;

import org.liris.ktbs.core.api.share.KtbsResource;
import org.liris.ktbs.rdf.RdfKtbsMapper;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;

public class RdfResourceSerializer implements KtbsResourceSerializer {

	@Override
	public KtbsResource deserialize(String uri, Reader reader, String mimeFormat) {
		return null;
	}

	@Override
	public void serialize(Writer writer, KtbsResource resource,
			String mimeFormat) {
		
		RdfKtbsMapper mapper = new RdfKtbsMapper(resource);
		Model model = mapper.getModel();
		
		model.write(writer, KtbsUtils.getJenaSyntax(mimeFormat), "");
	}
}
