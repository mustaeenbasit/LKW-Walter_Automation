package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_21827 extends PortalTest {
	ContactRecord myContact;
	FieldSet portalContact = new FieldSet();
	FieldSet portalSetupData = new FieldSet();

	public void setup() throws Exception {
		portalContact = testData.get(testName).get(0);
		portalSetupData = testData.get("env_portal_contact_setup").get(0);

		// Create a new contact with portal login info.
		sugar().accounts.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);

		// Setup Portal Access
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().admin.portalSetup.enablePortal();

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);
		sugar().logout();
	}

	/**
	 * Verify that the error message appears when New Password and Confirm New Password does not
	 * match.
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21827_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login to Sugar Portal as this new portal user.
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Navigate to profile.
		portal().navbar.navToProfile();

		// TODO: VOOD-1053
		// Click on the edit button.
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary[name='edit_button']").click();

		// Change Password.
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='current_password']").set(portalSetupData.get("password"));
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='new_password']").set(portalContact.get("newPassword"));

		// Type different password in the conform new password field.
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='confirm_password']").set(portalSetupData.get("password"));

		// Click on Save button.
		new VoodooControl("a", "css", ".fld_save_button.detail a").click();
		VoodooUtils.waitForReady();

		// Verify error message "The passwords must match." appears 
		new VoodooControl("span", "css", "[data-original-title='The passwords must match.']").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}