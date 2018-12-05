package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
@Features(revenueLineItem = false)
public class Opportunities_17966 extends SugarTest {
	VoodooSelect salesStage;
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		// 1 account & 1 opportunity created by API
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();

		// Opportunity has closed sales stage
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);

		// TODO: VOOD-1359
		salesStage = new VoodooSelect("span", "css", ".fld_sales_stage.edit");
		salesStage.set(customData.get("closed_sales_stage"));
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.listView.saveRecord(1);
	}

	/**
	 * Verify Delete option is disabled in Opportunity list record action when opportunity status is closed
	 * @throws Exception
	 */
	@Test
	public void Opportunities_17966_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl dropdown = sugar().opportunities.listView.getControl("dropdown01");
		VoodooControl delete = sugar().opportunities.listView.getControl("delete01");
		VoodooControl edit = sugar().opportunities.listView.getControl("edit01");
		VoodooControl unfollow = sugar().opportunities.listView.getControl("unfollow01");

		// Verify edit, follow buttons are enabled and delete button is disabled
		sugar().opportunities.listView.openRowActionDropdown(1);
		Assert.assertFalse("Edit button is disabled.", edit.isDisabled());
		Assert.assertFalse("Unfollow button is disabled.", unfollow.isDisabled());
		Assert.assertTrue("Delete button is enabled.", delete.isDisabled());

		// Verify error message on delete hover button
		delete.hover();
		
		VoodooUtils.waitForReady();

		// TODO: VOOD-1292
		new VoodooControl("div", "css", ".tooltip-inner").assertEquals(customData.get("error_msg"), true);
		dropdown.click();

		// After changing sales stage to any non closed state and save record
		sugar().opportunities.listView.editRecord(1);
		salesStage.set(customData.get("non_closed_sales_stage"));
		sugar().opportunities.listView.saveRecord(1);

		// Verify edit, follow, delete buttons are enabled
		sugar().opportunities.listView.openRowActionDropdown(1);
		Assert.assertFalse("Edit button is disabled.", edit.isDisabled());
		Assert.assertFalse("Unfollow button is disabled.", unfollow.isDisabled());
		Assert.assertFalse("Delete button is disabled.", delete.isDisabled());
		dropdown.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}