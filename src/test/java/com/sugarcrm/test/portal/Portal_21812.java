package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_21812 extends PortalTest {
	ContactRecord myContact;
	AccountRecord myAcc;
	DataSource customData = new DataSource();
	FieldSet portalSetupData = new FieldSet();

	public void setup() throws Exception {
		portalSetupData = testData.get("env_portal_contact_setup").get(0);
		customData = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();

		// Create Account Record
		myAcc = (AccountRecord) sugar().accounts.api.create();

		// Create portal set up
		myContact = (ContactRecord) sugar().contacts.create(portalSetupData);

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);
	}

	/**
	 * Verify that style is updated in portal after it is customized
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21812_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sugar Portal -> Theme -> default color changes to custom
		sugar().admin.navToPortalSettings();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.portalSetup.getControl("themePortal").click();
		VoodooUtils.focusDefault();
		sugar().admin.portalSetup.themePortal.getControl("cancel").waitForVisible(60000);

		// Verify styles of Border, Navigation, Button in theme Preview
		sugar().admin.portalSetup.themePortal.getControl("borderColor").set(customData.get(0).get("style_color"));
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("body", "css", "body").click(); // Click outside to get rid of color panel
		sugar().admin.portalSetup.themePortal.getControl("navigationBarColor").set(customData.get(1).get("style_color"));
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("body", "css", "body").click(); // Click outside to get rid of color panel
		sugar().admin.portalSetup.themePortal.getControl("primaryButtonColor").set(customData.get(2).get("style_color"));
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("body", "css", "body").click(); // Click outside to get rid of color panel

		sugar().admin.portalSetup.themePortal.getControl("saveAndDeploy").click();
		sugar().alerts.waitForLoadingExpiration();

		// TODO: Need to verify theme on RHS pane
		VoodooUtils.focusFrame("previewTheme");
		VoodooUtils.voodoo.log.info("Border color: " + new VoodooControl("div", "css", ".navbar-inner").getCssAttribute("box-shadow"));
		VoodooUtils.voodoo.log.info("Navigation Bar: " + new VoodooControl("div", "css", ".navbar-inner").getCssAttribute("background-color"));
		VoodooUtils.voodoo.log.info("Primary Button: " + new VoodooControl("div", "css", ".btn.btn-primary.previewprimaryBtn").getCssAttribute("background-color"));

		// Verify Border color
		new VoodooControl("div", "css", ".navbar-inner").assertCssAttribute("box-shadow", customData.get(0).get("rgb_value"));
		// Verify Navigation Bar
		new VoodooControl("div", "css", ".navbar-inner").assertCssAttribute("background-color", customData.get(1).get("rgb_value"));
		// Verify Primary Button
		new VoodooControl("div", "css", ".btn.btn-primary.previewprimaryBtn").assertCssAttribute("background-color", customData.get(2).get("rgb_value"));

		VoodooUtils.focusDefault();

		sugar().logout();

		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));

		// Navigate to portal URL
		portal().loginScreen.navigateToPortal();

		portal().loginScreen.getControl("login").assertExists(true);
		portal().loginScreen.getControl("forgotPassword").assertExists(true);

		// Verify Primary Button on Portal loginscreen page - Use assertAttribute for CSS
		VoodooUtils.voodoo.log.info("Primary Button: " + new VoodooControl("div", "css", ".btn.btn-primary").getCssAttribute("background-color"));
		new VoodooControl("div", "css", ".btn.btn-primary").assertCssAttribute("background-color", customData.get(2).get("rgb_value"));

		portal().loginScreen.startSignup();
		portal().signupScreen.getControl("signup").assertExists(true);
		portal().signupScreen.getControl("back").click();

		// login as portal user
		portal().login(portalUser);

		// verify theme on all pages/modules
		VoodooUtils.voodoo.log.info("Border color: " + new VoodooControl("div", "css", ".navbar-inner").getCssAttribute("box-shadow"));
		VoodooUtils.voodoo.log.info("Navigation Bar: " + new VoodooControl("div", "css", ".navbar-inner").getCssAttribute("background-color"));

		// Verify Border color
		new VoodooControl("div", "css", ".navbar-inner").assertCssAttribute("box-shadow", customData.get(0).get("rgb_value"));
		// Verify Navigation Bar
		new VoodooControl("div", "css", ".navbar-inner").assertCssAttribute("background-color", customData.get(1).get("rgb_value"));

		// logout from portal user
		portal().logout();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}