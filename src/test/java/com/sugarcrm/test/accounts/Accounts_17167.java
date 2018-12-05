package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17167 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Open Search and select Teams drawer after click the "Search for more..." option in teams dropdown on mass update panel
	 * @throws Exception
	 */			
	@Test
	public void Accounts_17167_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// go to accounts module and select one or more records in list view
		FieldSet moduleTitle = testData.get(testName).get(0);
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);

		// Select mass update action
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.massUpdate();

		// Select Teams from fields drop down
		sugar().accounts.massUpdate.getControl("massUpdateField02").set(sugar().teams.moduleNamePlural);
		sugar().accounts.massUpdate.getControl("massUpdateValue02").click();
		VoodooSelect searchForMore = (VoodooSelect) sugar().accounts.massUpdate.getControl("massUpdateValue02");
		searchForMore.clickSearchForMore();

		// Verify that the Search and select Teams drawer is opened
		sugar().teams.searchSelect.getControl("moduleTitle").assertContains(moduleTitle.get("title"), true);
		sugar().teams.searchSelect.cancel();

		// Cancel the mass update form 
		sugar().accounts.massUpdate.cancelUpdate();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}