package com.sugarcrm.test;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.datasource.DS;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.*;
import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.util.Map;

public abstract class SugarTest {
	protected static AppModel sugar;
	protected static PortalAppModel portal;
	protected String testName;
	public Map<String, DataSource> testData = null;
	DS dsWrapper = null;
	String propNameCsvBaseDir = "datasource.csv.baseDir";
	String propValueCsvBaseDir = "src/test/resources/data";
	String baseUrl;
	Configuration grimoireConfig, voodooConfig;
	long testStartTime, testDurationTime = 0;

	boolean isRevenueLineItemOn = true;

	@BeforeClass
	public static void setupOnce() throws Exception {}

	@Before
	public void baseSetup() throws Exception {
		testStartTime = System.currentTimeMillis();
		VoodooUtils.init();
		VoodooUtils.voodoo.log.info("Setting up SugarTest...");
		
		VoodooUtils.launchApp();
		grimoireConfig = VoodooUtils.getGrimoireConfig();
		voodooConfig = VoodooUtils.getVoodooConfig();
		
		testName = SugarTest.this.getClass().getSimpleName();

		baseUrl = new SugarUrl().getBaseUrl();

		String finalUrl = new SugarUrl(baseUrl).getFullUrl();
		if(voodooConfig.getValue("code_coverage", "false").equals("1")) {
			String canonicalTestName = SugarTest.this.getClass().getCanonicalName();
			finalUrl = new SugarUrl(baseUrl).enableSetupCodeCoverage(canonicalTestName).getFullUrl();
		}
		VoodooUtils.go(finalUrl);

		sugar = AppModel.getInstance();
		sugar.init();
		portal = PortalAppModel.getInstance();
		portal.init(); // TODO: May be needed in later versions of Portal Support

		VoodooUtils.voodoo.log.info("Setting up " + testName + "...");
		dsWrapper = new DS(testName);
		dsWrapper.init(DS.DataType.CSV, propNameCsvBaseDir, propValueCsvBaseDir);

		testData = dsWrapper.getDataSources(testName + ".*");
		if(testData == null || testData.size() == 0)
			VoodooUtils.voodoo.log.warning("The file " + testName + " does not exist, or is misspelled!");
		testData.putAll(dsWrapper.getDataSources("env" + ".*"));

		SugarTest.this.setup();
		VoodooUtils.voodoo.log.info(testName + " setup complete.");
		VoodooUtils.voodoo.log.info("SugarTest setup complete.");
	}

	/**
	 * Check whether there is Features Annotation and set the appropriate booleans
	 */
	private void setFeatureFlags() {
		Features features =  SugarTest.this.getClass().getAnnotation(Features.class);
		if(features != null) {
			isRevenueLineItemOn = features.revenueLineItem();
		}
	}
	
	@After
	public void baseCleanup() throws Exception {
		VoodooUtils.voodoo.log.info("Cleaning up SugarTest...");

		VoodooUtils.voodoo.log.info("Cleaning up " + testName + "...");
		
		// If an exception occurs, finish cleanup before throwing.
		// ALL cleanup requiring UI interaction should be inside this try/catch.
		Exception toThrow = null;
		try {
			// Dismiss one or more modal dialogs if present.
			while(VoodooUtils.isDialogVisible()) {
				VoodooUtils.acceptDialog();
			}
	
			// Nav to the home screen in case we failed in a bad spot and can't
			// navigate.  If this triggers a modal dialog (e.g. confirm
			// navigation), accept it.
			VoodooUtils.go(baseUrl);
			while(VoodooUtils.isDialogVisible()) {
				VoodooUtils.acceptDialog();
			}
			sugar.alerts.waitForLoadingExpiration();
//			VoodooUtils.waitForAlertExpiration();
//			// Dismiss Confirm Navigation if it was triggered.
//			if(VoodooUtils.isDialogVisible()) {
//				VoodooUtils.acceptDialog();
//			}
//			// Dismiss Confirm Navigation if it was triggered.
//			if(VoodooUtils.isDialogVisible()) {
//				VoodooUtils.acceptDialog();
//			}
		
			SugarTest.this.cleanup();
		} catch (Exception e) {
			VoodooUtils.voodoo.log.warning("Caught exception: " + e.getMessage() + "; will throw after cleanup.");
			toThrow = e;
		}

		VoodooUtils.voodoo.log.info(testName + " cleanup complete.");
		
		if(grimoireConfig.getValue("code_coverage", "false").equals("true")) {
			VoodooUtils.go(baseUrl + "?coverageEndTest=1");
		}
		
		try {
			VoodooUtils.closeApp();
		} catch (UnreachableBrowserException e) {
			VoodooUtils.voodoo.log.warning("UnreachableBrowserException caught in closeApp.  Proceeding.");
		} catch (WebDriverException e) {
			if(e.getMessage().contains("no such session")) {
				VoodooUtils.voodoo.log.warning("WebDriverException \"no such session\" caught in closeApp.  Proceeding.");
			} else {
				throw e;
			}
		}
		
		// TODO: Remove this try/catch after VOOD-370 is fixed.
		try {
			dsWrapper.cleanup();
		} catch(AssertionFailedError e) {
			VoodooUtils.voodoo.log.warning("An exception occurred while cleaning up the DataSource wrapper object.");
			e.printStackTrace();			
		}
		
		VoodooUtils.voodoo.log.info("SugarTest cleanup complete.");
		testDurationTime = System.currentTimeMillis() - testStartTime;
		VoodooUtils.voodoo.log.info("Test duration time im ms "+testDurationTime);
		if(toThrow != null) {
			VoodooUtils.voodoo.log.warning("Throwing exception caught in test-level cleanup.");
			throw toThrow;
		}
	}

	@AfterClass
	public static void cleanupOnce() {}
	
	public abstract void setup() throws Exception;
	public abstract void cleanup() throws Exception;

	/**
	 * Helper method to return a AppModel instance
	 * @return AppModel instance
	 */
	protected static AppModel sugar() {
		return AppModel.getInstance();
	}

	/**
	 * Helper method to return a PortalAppModel instance
	 * @return PortalAppModel instance
	 */
	protected static PortalAppModel portal() {
		return PortalAppModel.getInstance();
	}
}
