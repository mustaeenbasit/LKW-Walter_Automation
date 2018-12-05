package com.sugarcrm.test.RevenueLineItems;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18416 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();

		// Create 2 RLI records with Closed Won and Closed Lost sales stage
		DataSource rliData = testData.get(testName);
		sugar().revLineItems.create(rliData);
	}

	/**
	 * Verify closed sales stage Revenue Line Item record is not deleted in subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18416_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities Record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Expand RLI Subpanel
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get("RevenueLineItems");
		rliSubpanel.expandSubpanel();

		// Select RLI records
		rliSubpanel.getControl("checkbox01").click();
		rliSubpanel.getControl("checkbox02").click();

		// Open action dropdown 
		rliSubpanel.openActionDropdown();

		// Click delete
		rliSubpanel.getControl("deleteButton").click();

		// Verify warning message "One or more of the selected records has a status of Closed Won or Closed Lost and cannot be deleted."
		FieldSet warning = testData.get(testName + "_warning").get(0);
		sugar().alerts.getWarning().assertContains(warning.get("warningMsg"), true);

		// Verify that there is no checkbox in the far left corner inside subpanels
		Assert.assertFalse("The checkbox is checked", rliSubpanel.getControl("checkbox01").isChecked());
		Assert.assertFalse("The checkbox is checked", rliSubpanel.getControl("checkbox02").isChecked());

		// Try to click Action drop down
		rliSubpanel.getControl("actionDropdown").click();
		VoodooUtils.waitForReady();

		// Verify that action dropdown is disabled and there is no delete button in Action drop down 
		rliSubpanel.getControl("actionDropdown").assertAttribute("class", "disabled", true);
		rliSubpanel.getControl("deleteButton").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}