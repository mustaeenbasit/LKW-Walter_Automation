package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
@Features(revenueLineItem = false)
public class Opportunities_27655 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that click on the cancel button inside Opportunities Settings config screen returns user to admin panel
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27655_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl oppMgtCtrl = sugar().admin.adminTools.getControl("opportunityManagement");
		oppMgtCtrl.click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();

		// TODO: VOOD-2082
		//  Make some changes in the config panel
		VoodooControl oppHierarchyCtrl = new VoodooControl("input", "css", ".fld_opps_view_by.edit input[value=" + sugar().opportunities.moduleNamePlural + "]");
		oppHierarchyCtrl.assertAttribute("checked", Boolean.toString(true), true);
		new VoodooControl("input", "css", ".fld_opps_view_by.edit input[value=" + sugar().revLineItems.moduleNamePlural + "]").set(Boolean.toString(true));
		sugar().opportunities.createDrawer.cancel();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify User is returned to admin panel and no changes are applied
		sugar().admin.adminTools.assertExists(true);
		oppMgtCtrl.click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
		oppHierarchyCtrl.assertAttribute("checked", Boolean.toString(true), true);		
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}