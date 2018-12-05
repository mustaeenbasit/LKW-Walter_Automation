package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.sugar.views.LoginScreen;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_18323 extends SugarTest {
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Swtiching user won't keep the Activites perference.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_18323_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Home -> Activity Stream
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		ActivityStream stream = new ActivityStream();

		// Seeing Activity Stream
		stream.getControl("streamInput").assertVisible(true);

		// Go to Opportunities module. Create a record.
		sugar.opportunities.create();

		// Click on Home cube.
		sugar.navbar.navToModule("Home");

		// Immediately seeing Activity Stream, don't have to select from home cube
		stream.getControl("streamInput").assertVisible(true);
		sugar.logout();

		// Login as QAUser
		sugar.login(sugar.users.getQAUser());
		sugar.dashboard.getControl("firstDashlet").waitForVisible();

		// Still seeing My Dashboard.
		sugar.dashboard.getControl("firstDashlet").assertVisible(true);
		sugar.logout();

		// TODO: VOOD-966
		// sugar.login();	
		LoginScreen login = new LoginScreen();		
		login.getControl("loginUserName").set(VoodooUtils.getGrimoireConfig().getValue("sugar_user"));
		login.getControl("loginPassword").set(VoodooUtils.getGrimoireConfig().getValue("sugar_pass"));
		login.getControl("login").click();		
		sugar.alerts.waitForLoadingExpiration();

		// Immediately seeing Activity Stream, don't have to select from home cube
		stream.getControl("streamInput").assertVisible(true);

		// TODO: VOOD-954 the clean up will fail if not change back to dashboard page
		sugar.navbar.clickModuleDropdown(sugar.home);

		// TODO: VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}