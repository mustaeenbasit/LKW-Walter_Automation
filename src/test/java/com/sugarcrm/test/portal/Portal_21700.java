package com.sugarcrm.test.portal;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21700 extends PortalTest {
	ContactRecord myContact;
	FieldSet portalUser = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		// Create portal set up
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);
		portalUser.put("userName", portalSetupData.get("portalName"));
		portalUser.put("password", portalSetupData.get("password"));
		sugar().login();

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();
	}

	/**
	 * Verify reset portal user password.
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21700_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet passwordFS = testData.get(testName).get(0);
		FieldSet portalUserWithNewPassword = new FieldSet();
		portalUserWithNewPassword.put("userName", myContact.get("portalName"));
		portalUserWithNewPassword.put("password", passwordFS.get("password"));
		portalUserWithNewPassword.put("confirmPassword", passwordFS.get("confirmPassword"));

		// Edit contact with passwords field only
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();

		// TODO: VOOD-1108
		new VoodooControl("a", "css", ".fld_portal_password.edit a").click();
		sugar().contacts.recordView.getEditField("password").set(portalUserWithNewPassword.get("password"));
		sugar().contacts.recordView.getEditField("confirmPassword").set(portalUserWithNewPassword.get("confirmPassword"));
		sugar().contacts.recordView.save();
		sugar().logout();

		// Navigate to portal URL
		portal().loginScreen.navigateToPortal();

		// login as portal user with Old password
		portal().loginScreen.getControl("loginUserName").waitForVisible(300000);
		portal().loginScreen.getControl("loginUserName").set(portalUser.get("userName"));
		portal().loginScreen.getControl("loginPassword").set(portalUser.get("password"));
		portal().loginScreen.getControl("login").click();

		// Verify error alert with invalid credentials
		portal().alerts.getError().assertContains(testData.get(testName).get(0).get("error_msg"), true);
		portal().alerts.getError().closeAlert();

		// login as portal user with new/updated password
		portal().login(portalUserWithNewPassword);

		// Verify portal URL and page is opened after login
		Assert.assertTrue("Current URL is not a Portal URL", VoodooUtils.getUrl().toString().contains(VoodooUtils.getGrimoireConfig().getValue("env.portal_url")));
		portal().navbar.userAction.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}