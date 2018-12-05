package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28469 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().opportunities.create();
	}

	/**
	 * Verify that edited opportunity record can be successfully saved after linked RLI is updated while opp still in the edit mode
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28469_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		// Expand subpanel since the default state is "Collapsed"
		rliSubpanel.expandSubpanel();
		rliSubpanel.editRecord(1);

		// Editing the "salesStage" field of linked RLI to "Closed Lost" 
		FieldSet fs = testData.get(testName).get(0);
		// TODO: VOOD-1359
		new VoodooSelect("a", "css", ".fld_sales_stage.edit a").set(fs.get("salesStage"));

		// Save RLI
		rliSubpanel.saveAction(1);

		// Asserting the Sales Stage is Closed Lost
		// TODO: VOOD-1587
		new VoodooControl("div", "css", "[data-voodoo-name='sales_stage'] .ellipsis_inline").assertEquals(fs.get("salesStage"), true);

		// Asserting the status of Opportunity is Closed Lost in Record View
		sugar().opportunities.recordView.getDetailField("status").assertContains(fs.get("salesStage"), true);

		// Verify "Save" button is enabled.
		Assert.assertTrue("Save Button is Disabled", !(sugar().opportunities.recordView.getControl("saveButton").isDisabled()));

		// Save the Opp record
		sugar().opportunities.recordView.save();

		// Collapsing  the subpanel since the default state is collapsed.
		rliSubpanel.collapseSubpanel();

		// Asserting the status of Opportunity inListview, Closed Lost after Saving
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.getDetailField(1, "status").assertEquals(fs.get("salesStage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}