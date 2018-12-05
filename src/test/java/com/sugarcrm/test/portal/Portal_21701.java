package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_21701 extends PortalTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		sugar().accounts.api.create();
		ContactRecord myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);
		customData = testData.get(testName).get(0);
		sugar().login();

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();

		sugar().logout();
	}

	/**
	 * Verify new portal user register.
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21701_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login portal with default data from CSV
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", testData.get("env_portal_contact_setup").get(0).get("portalName"));
		portalUser.put("password", testData.get("env_portal_contact_setup").get(0).get("password"));

		// Navigate to portal URL
		portal().loginScreen.navigateToPortal();
		portal().loginScreen.startSignup();

		// portal sign-up page
		portal().signupScreen.getControl("firstName").set(customData.get("firstName"));
		portal().signupScreen.getControl("lastName").set(customData.get("lastName"));
		portal().signupScreen.getControl("email").set(customData.get("email"));
		portal().signupScreen.getControl("country").set(customData.get("country"));
		portal().signupScreen.getControl("company").set(customData.get("company"));
		portal().signupScreen.getControl("signup").click();
		portal().alerts.waitForLoadingExpiration();

		// Verify sign up welcome message
		String signupMsg = String.format("%s\n%s", customData.get("signUpMessageHead"), customData.get("signupMessageBody"));
		portal().signupScreen.getControl("signupMessage").assertContains(signupMsg, true);
		portal().signupScreen.getControl("back").click();

		// Navigate to actual (i.e Sugar) login URL
		sugar().loginScreen.navigateToSugar();
		sugar().login();

		// Verify lead record created after Portal sign up with same first, last name
		sugar().leads.navToListView();
		sugar().leads.listView.verifyField(1, "fullName", String.format("%s %s", customData.get("firstName"), customData.get("lastName")));

		// TODO: VOOD-1132
		sugar().logout();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}