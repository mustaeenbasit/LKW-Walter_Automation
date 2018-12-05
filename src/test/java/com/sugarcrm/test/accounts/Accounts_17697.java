package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17697 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
		
		// New Filter created
		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.setFilterFields(customData.get("sugarfield"), customData.get("display_name"), customData.get("operator"), customData.get("value"), 1);
		sugar().accounts.listView.filterCreate.getControl("filterName").set(customData.get("filter_name"));
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.save();
	}

	/**
	 * Verify user can delete a defined filter
	 * @throws Exception
	 */
	@Test
	public void Accounts_17697_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1462
		// Verify delete button is visible and deleted
		new VoodooControl("div", "css",".choice-filter.choice-filter-clickable").click();
		sugar().accounts.listView.filterCreate.getControl("deleteButton").click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify deleted filter is not available in dropdown
		sugar().accounts.listView.openFilterDropdown();
		new VoodooControl("li", "css", "div#select2-drop li").assertContains(customData.get("filter_name"), false);
		sugar().accounts.listView.selectFilterAll(); // to close dropdown

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}