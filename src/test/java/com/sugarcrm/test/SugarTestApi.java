package com.sugarcrm.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;

import static com.jayway.restassured.config.HttpClientConfig.*;

import com.sugarcrm.sugar.*;

public abstract class SugarTestApi {
	protected String testName;

	@BeforeClass
	public static void setupOnce() throws Exception {
	}

	@Before
	public void baseSetup() throws Exception {
		VoodooUtils.init();
		VoodooUtils.voodoo.log.info("Setting up SugarTestApi...");
		testName = SugarTestApi.this.getClass().getSimpleName();

		// Set the Rest Assured API global environment variables
		RestAssured.baseURI = new SugarUrl().getBaseUrl();
		RestAssured.port = 80;
		RestAssured.basePath = new SugarUrl().getRestRelativeUrl();
		
		//Set Time out to be 4 minutes, to avoid any server issues, check actual timings in log
		RestAssured.config = RestAssured.config().httpClient(httpClientConfig().setParam("CONNECTION_MANAGER_TIMEOUT", 600000));
		
		SugarTestApi.this.setup();
		VoodooUtils.voodoo.log.info(testName + " setup complete.");
		VoodooUtils.voodoo.log.info("SugarTestApi setup complete.");
	}

	@After
	public void baseCleanup() throws Exception {
		VoodooUtils.voodoo.log.info("Cleaning up SugarTestApi...");
		VoodooUtils.voodoo.log.info("Cleaning up " + testName + "...");
		// If an exception occurs, finish cleanup before throwing.
		Exception toThrow = null;
		try {
			SugarTestApi.this.cleanup();
		} catch (Exception e) {
			VoodooUtils.voodoo.log.warning("Caught exception: "
					+ e.getMessage() + "; will throw after cleanup.");
			toThrow = e;
		}

		VoodooUtils.voodoo.log.info(testName + " cleanup complete.");
		VoodooUtils.voodoo.log.info("SugarTestApi cleanup complete.");
		if (toThrow != null) {
			VoodooUtils.voodoo.log
					.warning("Throwing exception caught in test-level cleanup.");
			throw toThrow;
		}
	}

	@AfterClass
	public static void cleanupOnce() {
	}

	public abstract void setup() throws Exception;

	public abstract void cleanup() throws Exception;
}