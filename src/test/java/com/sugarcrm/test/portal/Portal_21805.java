package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_21805 extends PortalTest {
	FieldSet portalContact = new FieldSet();
	ContactRecord myCon;

	public void setup() throws Exception {
		portalContact = testData.get("env_portal_contact_setup").get(0);
		sugar().accounts.api.create();

		// Setup Portal Access
		sugar().login();

		sugar().admin.portalSetup.enablePortal();

		// Set up one contact as a portal user.
		myCon = (ContactRecord) sugar().contacts.api.create(portalContact);

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myCon);
	}

	/**
	 * Verify that the Language selector dropdown appears in the Signup page.
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21805_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().logout();

		// Go to the login page of the Portal
		portal().loginScreen.navigateToPortal();

		// Verify the language selector dropdown option available in the footer of login page.
		new VoodooControl("span", "id", "languageList").assertVisible(true);

		// Click on the Signup page.
		portal().loginScreen.startSignup();

		// Verify the language selector dropdown option available in the footer of sign up page.
		new VoodooControl("span", "id", "languageList").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}