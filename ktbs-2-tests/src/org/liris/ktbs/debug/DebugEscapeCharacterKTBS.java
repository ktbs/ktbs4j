package org.liris.ktbs.debug;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.domain.AttributePair;
import org.liris.ktbs.domain.interfaces.IAttributePair;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.tests.Examples;
import org.liris.ktbs.utils.KtbsUtils;

public class DebugEscapeCharacterKTBS {

	public static void main(String[] args) {
		ResourceService manager = Ktbs.getRestClient().getResourceService();

//		createBaseAndTrace(manager);
				createObsel(manager);
	
		IObsel obsel = manager.getResource("http://localhost:8001/base6/t02/e4b6878f42bb93706266fb9907753b70", IObsel.class);
//		
		System.out.println(obsel.getSubject());
		
	}

	private static void createObsel(ResourceService manager) {
		Set<IAttributePair> attributes = new HashSet<IAttributePair>();
		attributes.add(new AttributePair(Examples.getMessage(), "Bonjour tout le monde"));
		String value = "Ti\rti";
		String replaceValue = value.replaceAll("\t", Matcher.quoteReplacement("\\t"));
		
		String o = manager.newObsel(
				"http://localhost:8001/base6/t02/", 
				null, 
				Examples.getCloseChat().getUri(),
				null,
				null,
				new BigInteger("2000"),// 1s
				new BigInteger("2000"),// 1s
				value,
				attributes
		);
		System.err.println(value);
		System.err.println(replaceValue);
	}


	private static void createBaseAndTrace(
			ResourceService manager) {
		IRoot root = manager.getResource("http://localhost:8001/", IRoot.class);
		manager.newBase("base6");
		manager.newTraceModel("http://localhost:8001/base6/", "model6");
		manager.newStoredTrace(
				"http://localhost:8001/base6/", 
				"t02", 
				"http://localhost:8001/base6/model6/", 
				KtbsUtils.now(),
				"Nestor"
				);
	}
}
