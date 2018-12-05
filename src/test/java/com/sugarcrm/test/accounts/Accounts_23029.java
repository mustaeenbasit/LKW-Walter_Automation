package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Accounts_23029 extends SugarTest {
	DataSource opportunityData = new DataSource();

	public void setup() throws Exception {
		// Create multiple Opportunity records & an account record.
		opportunityData = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();
		sugar().opportunities.create(opportunityData);
	}

	/**
	 * Account Detail - Opportunities sub-panel - Sort_Verify that opportunity records related to the 
	 * accounts can be sorted by column titles on "OPPORTUNITIES" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23029_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to accounts list view and expand the opportunity subpanel in account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel oppSubpanel = sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		oppSubpanel.expandSubpanel();

		// TODO: VOOD-1424
		// Click on "Name" header and verify the name column is sorted in descending order
		oppSubpanel.sortBy("headerName", false);
		int count = opportunityData.size();
		int i;
		for (i = 1; i <= count; i++) {
			oppSubpanel.getDetailField(i, "name").assertEquals(opportunityData.get(count - i).get("name"), true);
		}

		// Click on "Name" header and verify the name is sorted in ascending order
		oppSubpanel.sortBy("headerName", true);
		for (i = 1; i <= count; i++) {
			oppSubpanel.getDetailField(i, "name").assertEquals(opportunityData.get(i-1).get("name"), true);
		}

		// Click on "date_closed" header and verify the expected close date is sorted in descending order 
		oppSubpanel.sortBy("headerDateclosed", false);
		for (i = 1; i <= count; i++) {
			oppSubpanel.getDetailField(i, "date_closed").assertEquals(opportunityData.get(count - i).get("date_closed"), true);
		}

		// Click on "date_closed" header and verify the expected close date is sorted in ascending order 
		oppSubpanel.sortBy("headerDateclosed", true);
		for (i = 1; i <= count; i++) {
			oppSubpanel.getDetailField(i, "date_closed").assertEquals(opportunityData.get(i-1).get("date_closed"), true);
		}

		// Click on "relAssignedTo" header and verify the assigned user is sorted in descending order 
		oppSubpanel.sortBy("headerAssignedusername", false);
		for (i = 1; i <= count; i++) {
			oppSubpanel.getDetailField(i, "relAssignedTo").assertEquals(opportunityData.get(count - i).get("relAssignedTo"), true);
		}

		// Click on "relAssignedTo" header and verify the assigned user is sorted in ascending order 
		oppSubpanel.sortBy("headerAssignedusername", true);
		for (i = 1; i <= count; i++) {
			oppSubpanel.getDetailField(i, "relAssignedTo").assertEquals(opportunityData.get(i-1).get("relAssignedTo"), true);
		}

		// Click on "likelyCase" header and verify the likely is sorted in descending order
		oppSubpanel.sortBy("headerAmount", false);
		for (i = 1; i <= count; i++) {
			oppSubpanel.getDetailField(i, "likelyCase").assertContains(opportunityData.get(count - i).get("likelyCase"), true);
		}

		// Click on "likelyCase" header and verify the likely is sorted in ascending order
		oppSubpanel.sortBy("headerAmount", true);
		for (i = 1; i <= count; i++) {
			oppSubpanel.getDetailField(i, "likelyCase").assertContains(opportunityData.get(i - 1).get("likelyCase"), true);
		}

		// TODO: VOOD-1380, VOOD-609
		// Click on "salesStage" header and verify the sales stage is sorted in descending order
		VoodooControl salesStage = new VoodooControl("tr", "css", "[data-fieldname='sales_stage']");
		salesStage.click();
		VoodooUtils.waitForReady();
		for (i = 1; i <= count; i++) {
			oppSubpanel.getDetailField(i, "salesStage").assertEquals(opportunityData.get(count - i).get("salesStage"), true);
		}

		// Click on "salesStage" header and verify the sales stage is sorted in ascending order
		salesStage.click();
		VoodooUtils.waitForReady();
		for (i = 1; i <= count; i++) {
			oppSubpanel.getDetailField(i, "salesStage").assertEquals(opportunityData.get(i - 1).get("salesStage"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
