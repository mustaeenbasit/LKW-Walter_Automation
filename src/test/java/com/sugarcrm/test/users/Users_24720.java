package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ruchi Bhatnagar <rbhatnagar@sugarcrm.com>
 */
public class Users_24720  extends SugarTest {
	FieldSet portalContactData = new FieldSet();

	public void setup() throws Exception {
		// Create Portal only user
		portalContactData = testData.get("env_portal_contact_setup").get(0);
		sugar().contacts.api.create(portalContactData);

		sugar().login();

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();	
	}

	/**
	 *  User-Portal_Verify that portal only user cannot login SugarCRM.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24720_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().logout();
		// Portal user
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", portalContactData.get("portalName"));
		portalUser.put("password", portalContactData.get("password"));

		// Navigate to portal URL
		portal.loginScreen.navigateToPortal();
		VoodooUtils.waitForReady();

		// Verify Portal user can login to Portal(to ensure correct Portal credentials)
		portal.login(portalUser);

		// Verifying no error alert 
		portal.alerts.getWarning().assertVisible(false);
		// Logging out from portal is implicit check of successful login to Portal.
		portal.logout();
		VoodooUtils.waitForReady();

		// Verify Portal only user cannot login SugarCRM, Error Alert & Error text 
		// Using login code instead of method because last line of the method 
		// checks for dashlet which will not appear in this scenario 
		sugar().loginScreen.navigateToSugar();
		sugar().loginScreen.getControl("loginUserName").set(portalContactData.get("portalName"));
		sugar().loginScreen.getControl("loginPassword").set(portalContactData.get("password"));
		sugar().loginScreen.getControl("login").click();	

		// Error Alert
		sugar().alerts.getError().assertVisible(true);
		FieldSet customData = testData.get(testName).get(0);

		// Error Text
		sugar().alerts.getError().assertContains(customData.get("errorText"), true);
		// Login, else standard cleanup will throw unfound element exception
		sugar().login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}