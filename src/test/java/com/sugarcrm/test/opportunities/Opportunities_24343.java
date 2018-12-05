package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24343 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		customDS = testData.get(testName);
		sugar().opportunities.api.create(customDS);
		sugar().login();

		// Opportunity record assign to QAuser
		// Add Account to OpportunityRecord
		// TODO: VOOD-444 - Support creating relationships via API
		// TODO: VOOD-1828 - Records created via api call are displayed in random order
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.sortBy("headerName", false);
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.recordView.showMore();
		sugar().opportunities.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().opportunities.recordView.save();
	}

	/**
	 * Verify that the opportunities assigned to current user can be listed when searching by "My Opportunities".
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24343_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify filter "My Opportunities" with Admin user and QAuser
		for (int i = 0; i < 2; i++) {
			// Go to Opportunity listView
			sugar().opportunities.navToListView();

			// From filter dropdown, select "My Opportunities" filter. 
			// TODO: VOOD-1828 - Records created via api call are displayed in random order
			sugar().opportunities.listView.selectFilter("My Opportunities");
			sugar().opportunities.listView.sortBy("headerName", true);

			// Verify that the opportunities assigned to current user are displayed.
			if (i == 0) { // For Admin
				sugar().opportunities.listView.verifyField(1, "name", customDS.get(0).get("name"));
				sugar().opportunities.listView.verifyField(2, "name", customDS.get(1).get("name"));
			} else { // For QAuser
				sugar().opportunities.listView.verifyField(1, "name", customDS.get(2).get("name"));
			}

			// Clear filter search
			sugar().opportunities.listView.openFilterDropdown();
			sugar().opportunities.listView.selectFilterAll();

			// Reset listView filter display and Verify all users records are displayed
			for (int j = 1; j <= customDS.size(); j++) {
				sugar().opportunities.listView.verifyField(j, "name", customDS.get(j - 1).get("name"));
			}

			// Logout as Admin and login as QAuser
			if (i == 0) {
				sugar().logout();
				sugar().login(sugar().users.getQAUser());
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}