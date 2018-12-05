package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20191 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Display/hide “Downloads” tab
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20191_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// navigate to admin profile
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that "Downloads" tab displayed correctly
		sugar().users.userPref.getControl("tab4").assertContains("Downloads", true);
		VoodooUtils.focusDefault();

		// Navigate to system settings
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");

		// UnChecked the "Display Downloads Tab:" checkbox
		sugar().admin.systemSettings.getControl("showDownloadTab").set("false");

		// Save change settings
		sugar().admin.systemSettings.getControl("save").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		
		// Navigate to profile
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that "Downloads" tab not visible 
		sugar().users.userPref.getControl("tab4").assertVisible(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}