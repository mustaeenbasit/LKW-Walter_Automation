package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Opportunities_28889 extends SugarTest {

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that in View Change Log page Close button should be display properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28889_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Opportunity List View
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-695,738
		new VoodooControl("a", "css", "[name='audit_button']").click();
		VoodooUtils.waitForReady();
		
		// Verify that On View Change Log page Close button should be shown instead of only link.
		VoodooControl btnCtrl = new VoodooControl("a", "css", ".fld_close_button a");
		btnCtrl.assertAttribute("class", "btn-link", false);
		btnCtrl.assertAttribute("class", "btn btn-primary", true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}