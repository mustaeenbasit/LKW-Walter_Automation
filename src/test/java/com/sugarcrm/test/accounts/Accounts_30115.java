package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_30115 extends SugarTest {
	public void setup() throws Exception {
		// Create an Account record
		sugar().accounts.api.create();

		// Login to Sugar
		sugar().login();
	}

	/**
	 * Verify that Mass update panel should not be displayed in RLI sub panel
	 *  
	 * @throws Exception
	 */
	@Test
	public void Accounts_30115_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet rliSubpanelData = testData.get(testName).get(0);

		// Navigate to Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel rliSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);

		// Go to RLI sub-panel and click on the RLI panel 3 times
		rliSubpanelCtrl.scrollIntoView();
		rliSubpanelCtrl.click();
		VoodooUtils.waitForReady();
		rliSubpanelCtrl.click();
		VoodooUtils.waitForReady();
		rliSubpanelCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that 'No data available' message should be displayed
		rliSubpanelCtrl.assertContains(rliSubpanelData.get("noDataFound"), true);

		// Verify that the 'Mass update' panel should not be displayed in the RLI sub-panel
		// Here verify that 'Mass Update' title and 'Update' button is not displayed
		rliSubpanelCtrl.assertContains(rliSubpanelData.get("massUpdate"), false);
		rliSubpanelCtrl.assertContains(rliSubpanelData.get("updateBtn"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}