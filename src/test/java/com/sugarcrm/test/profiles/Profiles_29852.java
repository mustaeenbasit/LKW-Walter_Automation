package com.sugarcrm.test.profiles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Profiles_29852 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that 'Fatal error: Call to a member function get_plugin_list()' Error is not seen on Downloads page of Profile.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Profiles_29852_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Navigate to Profile Icon >> Profile
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-563
		// Click on Downloads tab
		new VoodooControl("a", "id", "tab4").click();
		VoodooUtils.waitForReady();
		FieldSet customFS = testData.get(testName).get(0);

		// Verify that the User should not get any of the fatal error on profile >> Download
		sugar().users.detailView.assertContains(customFS.get("errorMsg"), false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}