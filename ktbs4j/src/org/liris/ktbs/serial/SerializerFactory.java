package org.liris.ktbs.serial;

import org.liris.ktbs.dao.ProxyFactory;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.serial.rdf.RdfDeserializer;
import org.liris.ktbs.serial.rdf.RdfSerializer;

public class SerializerFactory {

	private PojoFactory pojoFactory;

	public void setPojoFactory(PojoFactory pojoFactory) {
		this.pojoFactory = pojoFactory;
	}

	public Serializer newRdfSerializer(SerializationConfig config) {
		RdfSerializer rdfSerializer = new RdfSerializer();
		rdfSerializer.setSerializationConfig(config);
		return rdfSerializer;
	}


	public RdfDeserializer newRdfDeserializer(ProxyFactory proxyFactory, DeserializationConfig config) {
		RdfDeserializer rdfDeserializer = new RdfDeserializer();
		rdfDeserializer.setPojoFactory(pojoFactory);
		rdfDeserializer.setProxyFactory(proxyFactory);
		rdfDeserializer.setDeserializationConfig(config);
		return rdfDeserializer;
	}
}
