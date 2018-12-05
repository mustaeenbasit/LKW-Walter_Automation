package com.sugarcrm.test.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19798 extends SugarTest {
	String todaysDate = "", accountName= "";

	public void setup() throws Exception {
		// Create a Contract and Accounts with default data
		sugar().contracts.api.create();
		sugar().accounts.api.create();
		FieldSet searchData = testData.get(testName).get(0);
		todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		accountName = sugar().accounts.getDefaultData().get("name"); 

		// Login ad admin
		sugar().login();

		// Enabled Contract module
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);

		// Create Another Contract
		FieldSet contractData = new FieldSet();
		contractData.put("name", testName);
		contractData.put("status", searchData.get("status"));
		contractData.put("account_name", accountName);
		contractData.put("date_start", todaysDate);
		contractData.put("date_end", todaysDate);
		sugar().contracts.create(contractData);
	}

	/**
	 * Search Contract_Verify that advanced search for existing contracts works correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19798_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Contracts" tab on top navigation bar
		sugar().contracts.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on "Advanced Search"
		// TODO: VOOD-975
		new VoodooControl("a", "id", "advanced_search_link").click();
		VoodooUtils.waitForReady();

		// Enter data in each of text fields of "Contract Search" sub-panel, such as Contract Name, Account Name, Status, Start Date and Start Date
		// TODO: VOOD-975
		new VoodooControl("input", "id", "name_advanced").set(testName);
		new VoodooControl("input", "id", "account_name_advanced").set(accountName);
		new VoodooControl("option", "css", "#status_advanced option[value='inprogress']").click();
		new VoodooControl("input", "id", "range_start_date_advanced").set(todaysDate);
		new VoodooControl("input", "id", "range_end_date_advanced").set(todaysDate);

		// Click "Search" button in "Contract Search" sub-panel
		new VoodooControl("input", "id", "search_form_submit_advanced").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Verify that the matching records are displayed correctly in "Contract" list view
		sugar().contracts.listView.verifyField(1, "name", testName);
		Assert.assertTrue("Count record is not equals One", sugar().contracts.listView.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}