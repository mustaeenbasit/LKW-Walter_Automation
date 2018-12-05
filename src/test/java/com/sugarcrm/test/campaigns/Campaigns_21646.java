package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;

public class Campaigns_21646 extends SugarTest {
	FieldSet message;
	
	public void setup() throws Exception {
		sugar.login();
		message = testData.get(testName).get(0);
	}

	/**
	 * Show a help message when the user has no record to access in a module
	 * */
	@Test
	public void Campaigns_21646_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.campaigns.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl messagePaneCtrl = new VoodooControl("p", "css", ".msg");
		
		// Verifying that the help message is shown on page:
		// "You currently have no records saved. Create or Import one now."
		messagePaneCtrl.assertEquals(message.get("message"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}