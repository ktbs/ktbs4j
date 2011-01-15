package org.liris.ktbs.rdf.resource.test;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.Root;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class KtbsJenaResourceTestCase extends AbstractKtbsJenaTestCase {

	private KtbsResource resource;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		resource = loadInRepo(
				root,
				"ktbs-jena-resource.ttl", 
				Root.class);
	}

	@Test
	public void testSetLabel() {
		assertEquals("My kernel for Trace Based Systems", resource.getLabel());
		resource.setLabel("Tata");
		assertEquals("Tata", resource.getLabel());
	}

	@Test
	public void testGetLabel() {
		assertEquals("My kernel for Trace Based Systems", resource.getLabel());
	}

	@Test
	public void testGetType() {
		assertEquals(KtbsConstants.ROOT, resource.getResourceType());
	}

	@Test
	public void testListAllProperties() {
		assertEquals(7,KtbsUtils.count(resource.listAllStatements()));
		assertEquals(7,KtbsUtils.countSubject(resource.listAllStatements(), "http://localhost:8001/"));
		assertEquals(1,KtbsUtils.countSubjectProperty(
				resource.listAllStatements(), 
				"http://localhost:8001/",
				RDF.type.getURI()
		));
		assertEquals(1,KtbsUtils.countSubjectProperty(
				resource.listAllStatements(), 
				"http://localhost:8001/",
				RDFS.label.getURI()
		));
		assertEquals(3,KtbsUtils.countProperty(
				resource.listAllStatements(), 
				KtbsConstants.P_HAS_BASE
		));
		assertEquals(2,KtbsUtils.countSubjectPropertyNS(
				resource.listAllStatements(), 
				"http://localhost:8001/",
				"http://mondomaine/monnamespace#"
		));

		Map<String, Object> properties = KtbsUtils.toMap(resource.listAllStatements());

		assertTrue(properties.containsKey("http://mondomaine/monnamespace#prop1"));
		assertTrue(properties.containsKey("http://mondomaine/monnamespace#prop2"));
		assertTrue(properties.containsValue("http://mondomaine/monnamespace#concept1"));
		assertTrue(properties.containsValue("http://mondomaine/monnamespace#concept2"));
	}

	@Test
	public void testListKtbsProperties() {
		assertEquals(5,KtbsUtils.count(resource.listKtbsStatements()));
		assertEquals(3,KtbsUtils.countProperty(
				resource.listKtbsStatements(), 
				KtbsConstants.P_HAS_BASE
		));
		assertEquals(3,KtbsUtils.countSubjectProperty(
				resource.listKtbsStatements(), 
				"http://localhost:8001/",
				KtbsConstants.P_HAS_BASE
		));
		assertEquals(0,KtbsUtils.countSubjectPropertyNS(
				resource.listKtbsStatements(), 
				"http://localhost:8001/",
				"http://mondomaine/monnamespace#"
		));
		assertEquals(1,KtbsUtils.countSubjectProperty(
				resource.listKtbsStatements(), 
				"http://localhost:8001/",
				RDF.type.getURI()
		));
		assertEquals(1,KtbsUtils.countSubjectProperty(
				resource.listKtbsStatements(), 
				"http://localhost:8001/",
				RDFS.label.getURI()
		));
		Map<String, Object> properties = KtbsUtils.toMap(resource.listKtbsStatements());
		assertFalse(properties.containsKey("http://mondomaine/monnamespace#prop1"));
		assertFalse(properties.containsKey("http://mondomaine/monnamespace#prop2"));
		assertFalse(properties.containsValue("http://mondomaine/monnamespace#concept1"));
		assertFalse(properties.containsValue("http://mondomaine/monnamespace#concept2"));
	}

	@Test
	public void testListNonKtbsProperties() {
		assertEquals(2,KtbsUtils.count(resource.listNonKtbsStatements()));
		assertEquals(0,KtbsUtils.countProperty(
				resource.listNonKtbsStatements(), 
				KtbsConstants.P_HAS_BASE
		));
		assertEquals(0,KtbsUtils.countSubjectProperty(
				resource.listNonKtbsStatements(), 
				"http://localhost:8001/",
				KtbsConstants.P_HAS_BASE
		));
		assertEquals(2,KtbsUtils.countSubjectPropertyNS(
				resource.listNonKtbsStatements(), 
				"http://localhost:8001/",
				"http://mondomaine/monnamespace#"
		));
		assertEquals(0,KtbsUtils.countSubjectProperty(
				resource.listNonKtbsStatements(), 
				"http://localhost:8001/",
				RDF.type.getURI()
		));
		assertEquals(0,KtbsUtils.countSubjectProperty(
				resource.listNonKtbsStatements(), 
				"http://localhost:8001/",
				RDFS.label.getURI()
		));
		Map<String, Object> properties = KtbsUtils.toMap(resource.listNonKtbsStatements());
		assertEquals(2, properties.size());
		assertTrue(properties.containsKey("http://mondomaine/monnamespace#prop1"));
		assertTrue(properties.containsKey("http://mondomaine/monnamespace#prop2"));
		assertTrue(properties.containsValue("http://mondomaine/monnamespace#concept1"));
		assertTrue(properties.containsValue("http://mondomaine/monnamespace#concept2"));
	}

	@Test
	public void testAddProperty() {
		int all = KtbsUtils.count(resource.listAllStatements());
		int ktbs = KtbsUtils.count(resource.listKtbsStatements());
		int nonKtbs = KtbsUtils.count(resource.listNonKtbsStatements());


		resource.addProperty("bidon", "bidonValue1");
		assertEquals(1, resource.getPropertyValues("bidon").length);
		assertEquals("bidonValue1", resource.getPropertyValues("bidon")[0]);
		assertEquals(all+1,KtbsUtils.count(resource.listAllStatements()));
		assertEquals(ktbs,KtbsUtils.count(resource.listKtbsStatements()));
		assertEquals(nonKtbs +1,KtbsUtils.count(resource.listNonKtbsStatements()));

		resource.addProperty("bidon", "bidonValue2");
		assertEquals(2, resource.getPropertyValues("bidon").length);
		assertEquals(all+2,KtbsUtils.count(resource.listAllStatements()));
		assertEquals(ktbs,KtbsUtils.count(resource.listKtbsStatements()));
		assertEquals(nonKtbs+2,KtbsUtils.count(resource.listNonKtbsStatements()));

		resource.addProperty(KtbsConstants.P_INHERITS, "Nestor");
		assertEquals("Nestor", resource.getPropertyValues(KtbsConstants.P_INHERITS)[0]);
		assertEquals(all+3,KtbsUtils.count(resource.listAllStatements()));
		assertEquals(ktbs+1,KtbsUtils.count(resource.listKtbsStatements()));
		assertEquals(nonKtbs+2,KtbsUtils.count(resource.listNonKtbsStatements()));

	}

	@Test
	public void testremoveProperty() {
		resource.addProperty("bidon", "bidonValue1");
		resource.addProperty("bidon", "bidonValue2");
		assertEquals(2, resource.getPropertyValues("bidon").length);
		resource.removeProperty("bidon");
		assertEquals(0, resource.getPropertyValues("bidon").length);

		assertEquals(1, resource.getPropertyValues("http://mondomaine/monnamespace#prop1").length);
		resource.removeProperty("http://mondomaine/monnamespace#prop1");
		assertEquals(0, resource.getPropertyValues("http://mondomaine/monnamespace#prop1").length);

		assertEquals(3, resource.getPropertyValues(KtbsConstants.P_HAS_BASE).length);
		try {
			
			resource.removeProperty(KtbsConstants.P_HAS_BASE);
			fail("Should fail");
		} catch(IllegalStateException e) {
			
		} catch(Exception e) {
			fail("");
		}
		assertEquals(3, resource.getPropertyValues(KtbsConstants.P_HAS_BASE).length);
		
		
	}

	@Test
	public void testGetPropertyValues() {
		Object[] values;

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
