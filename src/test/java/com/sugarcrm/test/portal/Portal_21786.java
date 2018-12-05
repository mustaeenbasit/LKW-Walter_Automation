package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_21786 extends PortalTest {
	ContactRecord myContact;
	FieldSet portalContact = new FieldSet();

	public void setup() throws Exception {
		portalContact = testData.get("env_portal_contact_setup").get(0);
		sugar().accounts.api.create();

		// Setup Portal Access
		sugar().login();
		sugar().admin.portalSetup.enablePortal();

		// Create a new contact with portal login info and not related to any account.
		myContact = (ContactRecord) sugar().contacts.api.create(portalContact);

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);

		sugar().logout();
	}

	/**
	 * Verify portal user without associate with an accounts can still login to portal().
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21786_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login to Sugar Portal as this new portal user.
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Verify Portal user is able to login. Portal home page should display.
		new VoodooControl("div", "css", ".dashboard .thumbnail").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}