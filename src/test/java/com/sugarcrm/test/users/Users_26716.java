package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_26716 extends SugarTest {
		
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * 26716 Verify that advanced tab in the profile section now includes "Show Preferred Currency:" checkbox
	 * @throws Exception
	 */
	@Test
	public void Users_26716_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.navToProfile();
		sugar.users.detailView.edit();		
		
		VoodooUtils.focusFrame("bwc-frame");
		// Click Advanced tab
		sugar.users.userPref.getControl("tab4").click();
		
		// "Show Preferred Currency:" checkbox should be present
		sugar.users.userPref.getControl("advanced_showpreferedCurrency").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}