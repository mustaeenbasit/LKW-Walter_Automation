package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_26030 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		// Create Opportunity linked to an Account & RLI
		sugar().opportunities.create();
	}

	/**
	 * Verify that Revenue Line Item can be deleted from RLI sub-panel of opportunity record view
	 * @throws Exception
	 */
	@Test
	public void Opportunities_26030_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunity record view.
		sugar().opportunities.listView.clickRecord(1);

		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.expandSubpanel();

		// Verify that RLI sub-panel have one record 
		Assert.assertTrue("RLI subpanel doesn't have 1 row", rliSubpanel.countRows() == 1);

		// Check the available record
		rliSubpanel.checkRecord(1);

		// Verify Delete option is available in actions dropdown in RLI subpanel
		rliSubpanel.openActionDropdown();
		VoodooControl deleteBtnCtrl = rliSubpanel.getControl("deleteButton");
		deleteBtnCtrl.assertVisible(true);

		// Select "Delete"
		deleteBtnCtrl.click();

		// Confirm warning
		sugar().alerts.getWarning().confirmAlert();

		// Verify that selected RLI record is successfully deleted and confirmation message is presented.  
		sugar().alerts.getSuccess().assertVisible(true);
		VoodooUtils.waitForReady();
		Assert.assertTrue("RLI subpanel doesn't have 0 row", rliSubpanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}