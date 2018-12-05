package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_27794 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that RLIs with closed lost sales stage are not included in the Opportunity rollup total 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27794_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();

		DataSource rliDS = testData.get(testName);
		// Create a new Opportunity with RLI
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.defaultData.get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().opportunities.defaultData.get("relAccountName"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(rliDS.get(0).get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(rliDS.get(0).get("date_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(rliDS.get(0).get("likely"));

		// TODO: VOOD-1357 and VOOD-1359
		// Add new RLI row 
		new VoodooControl("a", "css", ".fieldset.edit .addBtn").click();
		VoodooUtils.waitForReady();
		String secondRLIRecord = "[data-voodoo-name='subpanel-for-opportunities-create'] tr:nth-of-type(2)";
		new VoodooControl("input", "css", secondRLIRecord+" .fld_name.edit input").set(rliDS.get(1).get("rli_name"));
		new VoodooControl("input", "css", secondRLIRecord+" .fld_date_closed.edit input").set(rliDS.get(1).get("date_expected_closed_date"));
		new VoodooControl("input", "css", secondRLIRecord+" .fld_likely_case.edit input").set(rliDS.get(1).get("likely"));

		// Save the Opportunity record
		sugar().opportunities.createDrawer.save();

		// Click on the created opp record from the list view
		sugar().opportunities.listView.clickRecord(1);

		// Verify that When the Sales Stage of RLI is not closed it is included in the roll up total
		VoodooControl likelyCaseDetailCtrl = sugar().opportunities.recordView.getDetailField("likelyCase");
		likelyCaseDetailCtrl.assertContains(rliDS.get(0).get("total"), true);
		VoodooControl bestCaseDetailCtrl = sugar().opportunities.recordView.getDetailField("bestCase");
		bestCaseDetailCtrl.assertContains(rliDS.get(0).get("total"), true);
		VoodooControl worstCaseDetailCtrl = sugar().opportunities.recordView.getDetailField("worstCase");
		worstCaseDetailCtrl.assertContains(rliDS.get(0).get("total"), true);

		// navigate to RLI record
		sugar().revLineItems.navToListView();

		// search and select rli record and update salesStage to "Close Lost"
		sugar().revLineItems.listView.setSearchString(rliDS.get(1).get("rli_name"));
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("salesStage").set(rliDS.get(0).get("sales_stage"));
		sugar().revLineItems.recordView.save();

		// navigate to opportunities record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Verify that When the Sales Stage of RLI is Closed Lost it is not included in the roll up total
		likelyCaseDetailCtrl.assertContains(rliDS.get(1).get("total"), true);
		bestCaseDetailCtrl.assertContains(rliDS.get(1).get("total"), true);
		worstCaseDetailCtrl.assertContains(rliDS.get(1).get("total"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}