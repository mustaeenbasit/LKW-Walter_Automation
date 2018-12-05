package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Roles_29311 extends SugarTest {
	public void setup() throws Exception {
		// Login as a non-admin User i.e qauser
		sugar.login(sugar().users.getQAUser());
	}

	/**
	 * Verify non-dmin user cannot access the inboundEmail via the URL
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_29311_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		
		// Enter the following in the URL and hit return
		VoodooUtils.go(new SugarUrl().getBaseUrl()+customFS.get("inboundPath"));
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that Unauthorized access to administration - is displayed
		sugar().inboundEmail.detailView.assertContains(customFS.get("verifyText"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}