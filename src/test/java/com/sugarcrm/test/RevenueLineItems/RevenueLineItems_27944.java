package com.sugarcrm.test.RevenueLineItems;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_27944 extends SugarTest {
	public void setup() throws Exception {
		// Login to Sugar
		sugar().login();
	}

	/**
	 *  Verify that Opportunities config is set to Opps + RLIs OOTB on new installs of Sugar 7.7.0.0
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_27944_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> Opportunities 
		sugar().admin.navToOpportunitySettings();

		// Verify that the 'Opportunities and Revenue Line Items' radio button is checked by default
		Assert.assertTrue("'Opportunities and Revenue Line Items' radio button is not checked by default", sugar().admin.oppViewSettings.getControl("rliView").isChecked());
		Assert.assertFalse("'Opportunities ' radio button is checked", sugar().admin.oppViewSettings.getControl("oppView").isChecked());

		// Cancel the 'Opportunities Setting' page
		sugar().admin.oppViewSettings.getControl("cancelButton").click();

		// Also verify that the RLI module is exist in the menu
		sugar().navbar.showAllModules();
		// TODO: VOOD-784
		new VoodooControl("li", "css", "ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='" + sugar().revLineItems.moduleNamePlural + "']").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}