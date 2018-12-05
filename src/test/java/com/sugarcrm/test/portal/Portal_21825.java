package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_21825 extends PortalTest {
	ContactRecord myContact;
	FieldSet portalContact = new FieldSet();
	FieldSet portalSetupData = new FieldSet();

	public void setup() throws Exception {
		portalContact = testData.get(testName).get(0);
		portalSetupData = testData.get("env_portal_contact_setup").get(0);

		// Setup Portal Access
		// Create a new contact with portal login info.
		sugar().accounts.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);
		sugar().login();
		sugar().admin.portalSetup.enablePortal();

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);
		sugar().logout();
	}

	/**
	 * Verify that change password link opens a popup in the profile page
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21825_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login to Sugar Portal as this new portal user.
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Navigating portal profile
		portal().navbar.navToProfile();

		// TODO: VOOD-1053
		// Click on edit button in profile
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary[name='edit_button']").click();

		// Verify password related fields should appear.
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='current_password']").assertVisible(true);
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='new_password']").assertVisible(true);
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='confirm_password']").assertVisible(true);

		// Change password.
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='current_password']").set(portalSetupData.get("password"));
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='new_password']").set(portalContact.get("newPassword"));
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='confirm_password']").set(portalContact.get("newPassword"));

		// save record
		new VoodooControl("a", "css", ".fld_save_button.detail a").click();
		VoodooUtils.waitForReady();

		// Logout from portal
		portal().logout();

		// Log into Portal with new password to verify password is working properly.
		portalUser.put("password", portalContact.get("newPassword"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}