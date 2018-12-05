package com.sugarcrm.test.grimoire;

import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for testing SugarUrl. There are 2 assumptions.
 *  	First, before any tests are ran, VoodooUtils need to be initialized for access
 *  	to grimoire and voodoo configs.
 *
 *  	Second, this test assumes that all tests have parallel disabled initially as it
 *  	doesn't need to be tested for all scenarios (It is accounted as part of base url
 *  	construction which is used for all url constructions).
 *
 * @author Eric Tam <etam@sugarcrm.com>
 */
public class SugarUrlTest {
	@BeforeClass
	public static void baseSetUp() throws Exception{
		VoodooUtils.init();
	}

	@Before
	public void setUp() {
		System.setProperty("parallel.enabled", "false");
	}

	@Test
	public void testBaseUrl_NoEscapeSlash() throws Exception {
		SugarUrl url = new SugarUrl("http://localhost/sugar");
		Assert.assertEquals("http://localhost/sugar/", url.getBaseUrl());
	}

	@Test
	public void testBaseUrl_NoParallel() throws Exception {
		SugarUrl url = new SugarUrl("http://localhost/sugar/");
		Assert.assertEquals("http://localhost/sugar/", url.getBaseUrl());
	}

	@Test
	public void testBaseUrl_Parallel() throws Exception {
		System.setProperty("parallel.enabled", "true");
		SugarUrl url = new SugarUrl("http://localhost/sugar/");
		Thread.currentThread().setName("1");
		Assert.assertEquals("http://localhost/sugar_1/", url.getBaseUrl());
	}

	@Test
	public void testPortalUrl() throws Exception {
		System.setProperty("env.portal_url", "portal");
		SugarUrl url = new SugarUrl("http://localhost/sugar/");
		Assert.assertEquals("http://localhost/sugar/portal/", url.getPortalUrl());
	}

	@Test
	public void testRestUrl() throws Exception {
		System.setProperty("restV10", "rest/v10");
		SugarUrl url = new SugarUrl("http://localhost/sugar/");
		Assert.assertEquals("http://localhost/sugar/rest/v10/", url.getRestUrl());
	}

	@Test
	public void testRestoreUrl() throws Exception {
		SugarUrl url = new SugarUrl("http://localhost/sugar/");
		Assert.assertEquals("http://localhost:5000/api/v1.0/resetSugar", url.getRestoreUrl());

		url = new SugarUrl("http://localhost:8080/sugar/");
		Assert.assertEquals("http://localhost:5000/api/v1.0/resetSugar", url.getRestoreUrl());
	}

	@Test
	public void testFullUrl_Parameters() throws Exception {
		SugarUrl url = new SugarUrl("http://localhost/sugar/");
		url.addParameter("key1", "value1");
		Assert.assertEquals("http://localhost/sugar?key1=value1/", url.getFullUrl());
		url.addParameter("key2", "value2");
		url.addParameter("key3", "value3");
		Assert.assertEquals("http://localhost/sugar?key1=value1&key2=value2&key3=value3/", url.getFullUrl());
	}

	@Test
	public void testFullUrl_NoParameters() throws Exception {
		SugarUrl url = new SugarUrl("http://localhost/sugar/");
		Assert.assertEquals("http://localhost/sugar/", url.getFullUrl());
	}
}
