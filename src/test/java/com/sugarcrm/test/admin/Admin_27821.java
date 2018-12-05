package com.sugarcrm.test.admin;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

@Features(revenueLineItem = false)
public class Admin_27821 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that RLI module does not appear in the list of modules when project by Opportunities only
	 * @throws Exception
	 */
	@Test
	public void Admin_27821_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// TODO: VOOD-1860
		VoodooControl enableModuleCtrl = new VoodooControl("tr", "xpath", "//*[@id='enabled_div']/div[3]/table/tbody[2]/tr[contains(.,'"+customData.get("rli_module_name")+"')]");
		
		// Go to Admin -> Search 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		
		// Verify that RLI module is not available
		enableModuleCtrl.assertExists(false);
		VoodooUtils.focusDefault();
		
		// switch opps to Opps+RLIs
		sugar().admin.api.switchToRevenueLineItemsView();
		
		// Wait Needed
		VoodooUtils.waitForReady();
		
		// Go to Admin -> Search 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();
		
		// Verify that RLI module is present in the list of "Enabled" modules
		enableModuleCtrl.assertExists(true);
		VoodooUtils.focusDefault();
		
		// switch Opps+RLIs to Opps only 
		sugar().admin.api.switchToOpportunitiesView();
		
		// Wait Needed
		VoodooUtils.waitForReady();
		
		// Go to Admin -> Search 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();
		
		// Verify that RLI module is not available
		enableModuleCtrl.assertExists(false);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}