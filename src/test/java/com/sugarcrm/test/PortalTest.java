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
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.util.Map;

public abstract class PortalTest {
	protected static AppModel sugar;
	protected static PortalAppModel portal;
	protected String testName;
	public Map<String, DataSource> testData = null;
	DS dsWrapper = null;
	String propNameCsvBaseDir = "datasource.csv.baseDir";
	String propValueCsvBaseDir = "src/test/resources/data";
	public String baseUrl;
	public static String portalUrl;
	Configuration grimoireConfig, voodooConfig;

	boolean isRevenueLineItemOn = true;

	@BeforeClass
	public static void setupOnce() throws Exception {}

	@Before
	public void baseSetup() throws Exception {
		VoodooUtils.init();
		VoodooUtils.voodoo.log.info("Setting up PortalTest...");
		
		VoodooUtils.launchApp();
		grimoireConfig = VoodooUtils.getGrimoireConfig();
		voodooConfig = VoodooUtils.getVoodooConfig();
		
		testName = PortalTest.this.getClass().getSimpleName();
		baseUrl = new SugarUrl().getBaseUrl();
		portalUrl = new SugarUrl().getPortalUrl();

		String finalUrl = new SugarUrl(baseUrl).getFullUrl();
		if(voodooConfig.getValue("code_coverage", "false").equals("1")) {
			String canonicalTestName = PortalTest.this.getClass().getCanonicalName();
			finalUrl = new SugarUrl(baseUrl).enableSetupCodeCoverage(canonicalTestName).getFullUrl();
		}
		WsRestV10 rest = new WsRestV10();
		rest.log("WARN", "Starting " + PortalTest.this.getClass().getCanonicalName() + ".", "VOODOO");

		VoodooUtils.go(finalUrl);

		sugar = AppModel.getInstance();
		sugar.init();
		portal = PortalAppModel.getInstance();
		portal.init();

		VoodooUtils.voodoo.log.info("Setting up " + testName + "...");
		dsWrapper = new DS(testName);
		dsWrapper.init(DS.DataType.CSV, propNameCsvBaseDir, propValueCsvBaseDir);

		testData = dsWrapper.getDataSources(testName + ".*");
		if(testData == null || testData.size() == 0)
			VoodooUtils.voodoo.log.warning("The file " + testName + " does not exist, or is misspelled!");
		testData.putAll(dsWrapper.getDataSources("env" + ".*"));

		// Check Features Annotation and switch to RLI if necessary
		setFeatureFlags();
		if(isRevenueLineItemOn) {
			sugar.admin.api.switchToRevenueLineItemsView();
		}

		PortalTest.this.setup();
		VoodooUtils.voodoo.log.info(testName + " setup complete.");
		VoodooUtils.voodoo.log.info("PortalTest setup complete.");
	}

	/**
	 * Check whether there is Features Annotation and set the appropriate booleans
	 */
	private void setFeatureFlags() {
		Features features =  PortalTest.this.getClass().getAnnotation(Features.class);
		if(features != null) {
			isRevenueLineItemOn = features.revenueLineItem();
		}
	}

	@After
	public void baseCleanup() throws Exception {
		VoodooUtils.voodoo.log.info("Cleaning up PortalTest...");

		VoodooUtils.voodoo.log.info("Cleaning up " + testName + "...");

		// If an exception occurs, finish cleanup before throwing.
		// ALL cleanup requiring UI interaction should be inside this try/catch.
		Exception toThrow = null;
		try {
			standardCleanup();
		} catch (Exception e) {
			VoodooUtils.voodoo.log.warning("Caught exception: " + e.getMessage() + "; will throw after cleanup.");
			toThrow = e;
		}

		// Check if universal_cleanup is enabled
		if ("true".equals(grimoireConfig.getValue("universal_cleanup"))) {
			// Make the api call to reset sugar
			new WsRestV10().restoreSugar();
		}

		VoodooUtils.voodoo.log.info(testName + " cleanup complete.");
		if(voodooConfig.getValue("code_coverage", "false").equals("1")) {
			VoodooUtils.go(new SugarUrl().addParameter("coverageEndTest", "1").getFullUrl());
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
		
		VoodooUtils.voodoo.log.info("PortalTest cleanup complete.");
		if(toThrow != null) {
			VoodooUtils.voodoo.log.warning("Throwing exception caught in test-level cleanup.");
			throw toThrow;
		}
	}

	protected void standardCleanup() throws Exception {
		// Dismiss one or more modal dialogs if present.
		while (VoodooUtils.isDialogVisible()) {
			VoodooUtils.acceptDialog();
			VoodooUtils.pause(1000);
		}

		// Nav to the home screen in case we failed in a bad spot and can't
		// navigate.  If this triggers a modal dialog (e.g. confirm
		// navigation), accept it.
		try {
			VoodooUtils.go(baseUrl);
		} catch (SessionNotFoundException e) {
			VoodooUtils.launchApp();
			VoodooUtils.go(baseUrl);
			sugar().login();
		}
		// Dismiss Confirm Navigation if it was triggered.
		if (VoodooUtils.isDialogVisible()) {
			VoodooUtils.acceptDialog();
			VoodooUtils.pause(1000);
		}
		sugar.alerts.waitForLoadingExpiration(30000);

		// If we're not logged in as admin, relog as admin.
		// First check for the caret to open the User Actions menu.
		// It can't be open yet because we just arrived at this page.
		if (sugar.navbar.userAction.getControl("userActions").queryVisible()) {
			// If it's there, open it.
			sugar.navbar.toggleUserActionsMenu();
			// If Administration is not in the menu
			if (!sugar.navbar.userAction.getControl("admin").queryVisible()) {
				// ...close the menu and log out so we can relog as admin.
				sugar.navbar.toggleUserActionsMenu();
				sugar.logout();
				// if Administration *is* in the menu...
			} else {
				// Just close it.
				sugar.navbar.toggleUserActionsMenu();
			}
		}

		// If we're on the login screen (either because we just logged out or
		// because we already were)...
		if (sugar.loginScreen.getControl("login").queryVisible()) {
			// ...log in as admin.
			sugar.login();
		}

		//VOOD-1180: Revert back to default view for Opportunities vs RLI
		// Revert the enabled/disabled features
		//			if(isRevenueLineItemOn) { sugar.admin.api.switchToOpportunitiesView(); }

		PortalTest.this.cleanup();

		// Each subsequent test should have a fresh, clean environment to begin with. So clear
		// all data in localStorage for current test.
		VoodooUtils.clearLocalStorage();
	}

	@AfterClass
	public static void cleanupOnce() {}
	
	public abstract void setup() throws Exception;
	public abstract void cleanup() throws Exception;
	
	public static String getPortalURL() throws Exception {
		return portalUrl;
	}

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