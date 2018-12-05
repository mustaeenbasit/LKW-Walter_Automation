package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_27739 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Opportunity is successfully created with RLI record linked to it 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27739_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource rliDS = testData.get(testName);
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		FieldSet fs = sugar().opportunities.defaultData;
		sugar().opportunities.createDrawer.getEditField("name").set(fs.get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(fs.get("relAccountName"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(rliDS.get(0).get("name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(rliDS.get(0).get("date_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(rliDS.get(0).get("likely"));

		// TODO: VOOD-1357 and VOOD-1359
		// Add new RLI row 
		new VoodooControl("a", "css", ".fieldset.edit .addBtn").click();
		VoodooUtils.waitForReady();
		String secondRLIRecord = "[data-voodoo-name='subpanel-for-opportunities-create'] tr:nth-of-type(2)";
		new VoodooControl("input", "css", secondRLIRecord+" .fld_name.edit input").set(rliDS.get(1).get("name"));
		new VoodooControl("input", "css", secondRLIRecord+" .fld_date_closed.edit input").set(rliDS.get(1).get("date_expected_closed_date"));
		new VoodooControl("input", "css", secondRLIRecord+" .fld_likely_case.edit input").set(rliDS.get(1).get("likely"));

		// Save the Opportunity record
		sugar().opportunities.createDrawer.save();

		// Click on the created opportunity record from list view
		sugar().opportunities.listView.clickRecord(1);

		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify new opp with proper rollup likely/date values as expected and should NOT see the "opp needs an RLI, click to Create an RLI" alert (used to be there before 7.6)
		int likelyVal = Integer.parseInt(rliDS.get(0).get("likely")) + Integer.parseInt(rliDS.get(1).get("likely"));
		StringBuilder price = new StringBuilder(Integer.toString(likelyVal));
		price.insert(1, ",");
		String totalRLIPrice  = String.format("$%s.00", price);
		sugar().opportunities.recordView.getDetailField("status").waitForVisible();
		sugar().opportunities.recordView.getDetailField("likelyCase").assertEquals(totalRLIPrice, true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertEquals(totalRLIPrice, true);
		sugar().opportunities.recordView.getDetailField("worstCase").assertEquals(totalRLIPrice, true);
		sugar().opportunities.recordView.getDetailField("date_closed").assertEquals(rliDS.get(1).get("date_expected_closed_date"), true);

		// Verify "opp needs an RLI, click to Create an RLI" alert does not appear
		sugar().alerts.getWarning().assertExists(false);

		// Verify RLI records
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.expandSubpanel();

		// Sorting subpanel records in order to verify them correctly
		rliSubpanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();

		// Verifying RLI1 name
		// TODO: VOOD-1587
		new VoodooControl("div", "css", ".layout_RevenueLineItems table tbody tr:nth-child(1) .fld_name div").assertEquals(rliDS.get(1).get("name"), true);

		// Verifying Likely for RLI1
		price = new StringBuilder(rliDS.get(1).get("likely"));
		price.insert(1, ",");
		String likelyPrice  = String.format("$%s.00", price);
		new VoodooControl("div", "css", ".layout_RevenueLineItems table tbody tr:nth-child(1) .fld_likely_case div").assertEquals(likelyPrice, true);

		// Verifying RLI2 name
		new VoodooControl("div", "css", ".layout_RevenueLineItems table tbody tr:nth-child(2) .fld_name div").assertEquals(rliDS.get(0).get("name"), true);

		// Verifying Likely for RLI2
		StringBuilder price1 = new StringBuilder(rliDS.get(0).get("likely"));
		String likelyPrice1  = String.format("$%s.00", price1);
		new VoodooControl("div", "css", ".layout_RevenueLineItems table tbody tr:nth-child(2) .fld_likely_case div").assertEquals(likelyPrice1, true);
		// Collapsing subpanel because the default subpanel state is "Collapsed"
		rliSubpanel.collapseSubpanel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}