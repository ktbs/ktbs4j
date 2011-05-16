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
		Serializer rdfSerializer = newRdfSerializer();
		rdfSerializer.setSerializationConfig(config);
		return rdfSerializer;
	}

	public Serializer newRdfSerializer() {
		RdfSerializer rdfSerializer = new RdfSerializer();
		return rdfSerializer;
	}

	public RdfDeserializer newRdfDeserializer(ProxyFactory proxyFactory, DeserializationConfig config) {
		RdfDeserializer deserializer = newRdfDeserializer(proxyFactory);
		deserializer.setDeserializationConfig(config);
		return deserializer;
	}

	public RdfDeserializer newRdfDeserializer(ProxyFactory proxyFactory) {
		RdfDeserializer rdfDeserializer = new RdfDeserializer();
		rdfDeserializer.setPojoFactory(pojoFactory);
		rdfDeserializer.setProxyFactory(proxyFactory);
		return rdfDeserializer;
	}
}
