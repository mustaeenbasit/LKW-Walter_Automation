package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooDate;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_27658 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Expected Closed Date comes from non-closed RLIs when switching from Opps to Opps+RLIs
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27658_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create an Opportunities record
		DataSource rliData = testData.get(testName);
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(rliData.get(0).get("name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(rliData.get(0).get("expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(rliData.get(0).get("likely"));
		VoodooControl rliStageFirstCtrl = sugar().opportunities.createDrawer.getEditField("rli_stage");
		rliStageFirstCtrl.scrollIntoViewIfNeeded(false);
		rliStageFirstCtrl.set(rliData.get(0).get("stage"));

		// TODO: VOOD-1357 - Need Lib support to create multiple RLI records in opportunity creatDrawer view
		// TODO: VOOD-1359 - Need to modify Lib support for controls on Opportunity createDrawer view and recordView
		// Add new RLI row and fill with data
		for (int i = 1; i < rliData.size(); i++) {
			new VoodooControl("a", "css", "[data-voodoo-name='subpanel-for-opportunities-create'] tr.single:nth-of-type(" + i + ") .addBtn").click();
			VoodooUtils.waitForReady();
			String rliRecord = "[data-voodoo-name='subpanel-for-opportunities-create'] tr.single:nth-of-type(" + (i + 1) + ")";
			new VoodooControl("input", "css", rliRecord + " .fld_name.edit input").set(rliData.get(i).get("name"));
			new VoodooDate("input", "css", rliRecord + " .fld_date_closed.edit input").set(rliData.get(i).get("expected_closed_date"));
			new VoodooControl("input", "css", rliRecord + " .fld_likely_case.edit input").set(rliData.get(i).get("likely"));
			VoodooControl rliStageNextCtrl = new VoodooSelect("a", "css", rliRecord + " .fld_sales_stage.edit a");
			rliStageNextCtrl.scrollIntoViewIfNeeded(false);
			rliStageNextCtrl.set(rliData.get(i).get("stage"));
		}
		
		// An Opportunities record is created with multiple RLI records
		sugar().opportunities.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Switch to Opportunities only mode.
		sugar().admin.api.switchToOpportunitiesView();
		
		// Navigate to Opportunities list view
		sugar().opportunities.navToListView();
		
		// Verify expected closed date should be latest from the CSV i.e 04/26/2014 (latest/earliest date should be lies between 04/21/2014 - 04/29/2014)
		VoodooControl expectedClosedDate = sugar().opportunities.listView.getDetailField(1, "date_closed");
		expectedClosedDate.waitForVisible();
		expectedClosedDate.scrollIntoViewIfNeeded(false);
		expectedClosedDate.assertEquals(rliData.get(3).get("expected_closed_date"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}