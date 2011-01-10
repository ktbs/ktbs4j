package org.liris.ktbs.rdf.resource.test;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class KtbsJenaResourceTestCase extends AbstractKtbsJenaTestCase {
	
	private KtbsResource resource;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		resource = loadInHolder(
				"", 
				"ktbs-jena-resource.ttl", 
				KtbsRoot.class);
	}

	@Test
	public void testSetLabel() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetLabel() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetType() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testListAllProperties() {
		assertEquals(7,KtbsUtils.count(resource.listAllProperties()));
		assertEquals(7,KtbsUtils.countSubject(resource.listAllProperties(), "http://localhost:8001/"));
		assertEquals(1,KtbsUtils.countSubjectProperty(
				resource.listAllProperties(), 
				"http://localhost:8001/",
				RDF.type.getURI()
		));
		assertEquals(1,KtbsUtils.countSubjectProperty(
				resource.listAllProperties(), 
				"http://localhost:8001/",
				RDFS.label.getURI()
		));
		assertEquals(3,KtbsUtils.countProperty(
				resource.listAllProperties(), 
				KtbsConstants.P_HAS_BASE
		));
		assertEquals(2,KtbsUtils.countSubjectPropertyNS(
				resource.listAllProperties(), 
				"http://localhost:8001/",
				"http://mondomaine/monnamespace#"
		));

		Map<String, String> properties = KtbsUtils.toMap(resource.listAllProperties());
		
		assertTrue(properties.containsKey("http://mondomaine/monnamespace#prop1"));
		assertTrue(properties.containsKey("http://mondomaine/monnamespace#prop2"));
		assertTrue(properties.containsValue("http://mondomaine/monnamespace#concept1"));
		assertTrue(properties.containsValue("http://mondomaine/monnamespace#concept2"));
	}

	@Test
	public void testListKtbsProperties() {
		assertEquals(5,KtbsUtils.count(resource.listKtbsProperties()));
		assertEquals(3,KtbsUtils.countProperty(
				resource.listKtbsProperties(), 
				KtbsConstants.P_HAS_BASE
		));
		assertEquals(3,KtbsUtils.countSubjectProperty(
				resource.listKtbsProperties(), 
				"http://localhost:8001/",
				KtbsConstants.P_HAS_BASE
		));
		assertEquals(0,KtbsUtils.countSubjectPropertyNS(
				resource.listKtbsProperties(), 
				"http://localhost:8001/",
				"http://mondomaine/monnamespace#"
		));
		assertEquals(1,KtbsUtils.countSubjectProperty(
				resource.listKtbsProperties(), 
				"http://localhost:8001/",
				RDF.type.getURI()
		));
		assertEquals(1,KtbsUtils.countSubjectProperty(
				resource.listKtbsProperties(), 
				"http://localhost:8001/",
				RDFS.label.getURI()
		));
		Map<String, String> properties = KtbsUtils.toMap(resource.listKtbsProperties());
		assertFalse(properties.containsKey("http://mondomaine/monnamespace#prop1"));
		assertFalse(properties.containsKey("http://mondomaine/monnamespace#prop2"));
		assertFalse(properties.containsValue("http://mondomaine/monnamespace#concept1"));
		assertFalse(properties.containsValue("http://mondomaine/monnamespace#concept2"));
	}

	@Test
	public void testListNonKtbsProperties() {
		assertEquals(2,KtbsUtils.count(resource.listNonKtbsProperties()));
		assertEquals(0,KtbsUtils.countProperty(
				resource.listNonKtbsProperties(), 
				KtbsConstants.P_HAS_BASE
		));
		assertEquals(0,KtbsUtils.countSubjectProperty(
				resource.listNonKtbsProperties(), 
				"http://localhost:8001/",
				KtbsConstants.P_HAS_BASE
		));
		assertEquals(2,KtbsUtils.countSubjectPropertyNS(
				resource.listNonKtbsProperties(), 
				"http://localhost:8001/",
				"http://mondomaine/monnamespace#"
		));
		assertEquals(0,KtbsUtils.countSubjectProperty(
				resource.listNonKtbsProperties(), 
				"http://localhost:8001/",
				RDF.type.getURI()
		));
		assertEquals(0,KtbsUtils.countSubjectProperty(
				resource.listNonKtbsProperties(), 
				"http://localhost:8001/",
				RDFS.label.getURI()
		));
		Map<String, String> properties = KtbsUtils.toMap(resource.listNonKtbsProperties());
		assertEquals(2, properties.size());
		assertTrue(properties.containsKey("http://mondomaine/monnamespace#prop1"));
		assertTrue(properties.containsKey("http://mondomaine/monnamespace#prop2"));
		assertTrue(properties.containsValue("http://mondomaine/monnamespace#concept1"));
		assertTrue(properties.containsValue("http://mondomaine/monnamespace#concept2"));
	}

	@Test
	public void testGetPropertyValues() {
		String[] values;
		
		values = resource.getPropertyValues(RDF.type.getURI());
		assertNotNull(values);
		assertEquals(1, values.length);
		assertTrue(Arrays.asList(values).contains("http://liris.cnrs.fr/silex/2009/ktbs#KtbsRoot"));
		
		values = resource.getPropertyValues(RDFS.label.getURI());
		assertNotNull(values);
		assertEquals(1, values.length);
		assertTrue(Arrays.asList(values).contains("My kernel for Trace Based Systems"));
		
		values = resource.getPropertyValues(KtbsConstants.P_HAS_BASE);
		assertNotNull(values);
		assertEquals(3, values.length);
		assertTrue(Arrays.asList(values).contains("http://localhost:8001/base1/"));
		assertTrue(Arrays.asList(values).contains("http://localhost:8001/base2/"));
		assertTrue(Arrays.asList(values).contains("http://localhost:8001/base3/"));
		
		values = resource.getPropertyValues("http://mondomaine/monnamespace#prop1");
		assertNotNull(values);
		assertEquals(1, values.length);
		assertTrue(Arrays.asList(values).contains("http://mondomaine/monnamespace#concept1"));

		values = resource.getPropertyValues("http://mondomaine/monnamespace#prop2");
		assertNotNull(values);
		assertEquals(1, values.length);
		assertTrue(Arrays.asList(values).contains("http://mondomaine/monnamespace#concept2"));
	}

}
