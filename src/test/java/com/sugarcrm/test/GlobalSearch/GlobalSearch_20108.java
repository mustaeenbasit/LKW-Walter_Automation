package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20108 extends SugarTest {
	DataSource accountsData = new DataSource();

	public void setup() throws Exception {
		accountsData= testData.get(testName);
		sugar().login();

		// Create 2 accounts with different name and email
		sugar().accounts.create(accountsData);

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to Accounts > Layouts > Search
		// TODO: VOOD-542 and VOOD-1509
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();

		// Add Email Address in search
		// Drag and drop Email into the Default column
		VoodooControl dropHere = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", ".draggable[data-name='email']").dragNDrop(dropHere);

		// Save & Deploy
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that search with % in field: Any Email is working in Advanced Search page.
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20108_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts list view
		sugar().accounts.navToListView();

		// Create a new filter
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();

		// Set filter Fields (Condition: exactly matches)
		DataSource filterData = testData.get(testName + "_filterData");
		sugar().accounts.listView.filterCreate.setFilterFields("emailAddress", filterData.get(0).get("columnDisplayName"), filterData.get(0).get("operator"),
				accountsData.get(0).get("emailAddress"), 1);
		VoodooUtils.waitForReady();

		// Verify that the record should be returned accordingly
		sugar().accounts.listView.verifyField(1, "name", accountsData.get(0).get("name"));
		sugar().accounts.listView.verifyField(1, "emailAddress", accountsData.get(0).get("emailAddress"));

		// Verify that the second record should not populate.
		sugar().accounts.listView.getControl("checkbox03").assertVisible(false);

		// Reset filter
		sugar().accounts.listView.filterCreate.reset();

		// Verify filter result (Condition: starts with)
		sugar().accounts.listView.filterCreate.setFilterFields("emailAddress", filterData.get(1).get("columnDisplayName"), filterData.get(1).get("operator"),
				accountsData.get(1).get("emailAddress"), 1);
		VoodooUtils.waitForReady();

		// Verify that the record should be returned accordingly
		sugar().accounts.listView.verifyField(1, "name", accountsData.get(1).get("name"));
		sugar().accounts.listView.verifyField(1, "emailAddress", accountsData.get(1).get("emailAddress"));

		// Verify that the second record should not populate.
		sugar().accounts.listView.getControl("checkbox03").assertVisible(false);

		// Cancel the filter
		sugar().accounts.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}