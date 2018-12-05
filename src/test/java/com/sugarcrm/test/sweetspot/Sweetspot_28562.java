package com.sugarcrm.test.sweetspot;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ruchi Bhatnagar <rbhatnagar@sugarcrm.com>
 */
public class Sweetspot_28562 extends SugarTest {

	public void setup() throws Exception {
		UserRecord chris = (UserRecord)sugar().users.api.create();
		chris.login();
	}

	/**
	 * Verify user cannot call Sweet Spot during SetUp Wizard
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28562_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Call Sweetspot
		sugar().sweetspot.show();

		// Verify User should not be able to call Sweet Spot
		VoodooControl sweetspotBarCtrl = sugar().sweetspot.getControl("sweetspotBar");
		sweetspotBarCtrl.assertVisible(false);
		VoodooControl searchBoxCtrl = sugar().sweetspot.getControl("searchBox");
		searchBoxCtrl.assertVisible(false);

		// Enter Email Address (Required field)
		sugar().newUserWizard.getControl("emailAddress").set(sugar().users.getDefaultData().get("emailAddress"));
		VoodooUtils.waitForReady();

		// Click "Next" button
		for (int i = 0 ; i < 3 ; i++) {
			sugar().newUserWizard.clickNextButton();
		}

		// Call Sweetspot at last step of Set Up Wizard
		sugar().sweetspot.show();

		// Verify User should not be able to call Sweet Spot
		sweetspotBarCtrl.assertVisible(false);
		searchBoxCtrl.assertVisible(false);

		// Clicking "Start Sugar" 
		sugar().newUserWizard.clickStartSugar();
		VoodooUtils.waitForReady();

		// Verify sugar is loaded.
		sugar().home.dashboard.getControl("dashboardTitle").assertVisible(true);

		// Call Sweetspot
		sugar().sweetspot.show();

		// Verify User should be able to call Sweet Spot
		sweetspotBarCtrl.assertVisible(true);
		searchBoxCtrl.assertVisible(true);

		// Hide Sweetspot
		sugar().sweetspot.hide();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}