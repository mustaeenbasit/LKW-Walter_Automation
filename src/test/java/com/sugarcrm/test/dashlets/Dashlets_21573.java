package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21573 extends SugarTest {
	DataSource oppData = new DataSource();
	String accountName = "";

	public void setup() throws Exception {
		// Creating 1 Account record
		sugar().accounts.api.create();

		// Creating two opp records
		oppData = testData.get(testName+"_oppData");
		sugar().opportunities.api.create(oppData);

		sugar().login();

		// Associating Account record with one Opportunity
		// TODO: VOOD-444
		sugar().opportunities.navToListView();
		// Sorting to update specific record.
		sugar().opportunities.listView.sortBy("headerName", false);
		sugar().opportunities.listView.editRecord(1);
		accountName = sugar().accounts.getDefaultData().get("name");
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(accountName);
		sugar().opportunities.listView.saveRecord(1);
	}

	/**
	 * Verify that adding relationships (Accounts, Contacts, custom modules) to dashlet filters works correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21573_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to "Home"
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);

		// Edit to add "List View" dashlet
		sugar().home.dashboard.edit();
		// Clicking 'Add row' 
		// TODO: VOOD-591 Dashlets support
		new VoodooControl("a", "css", ".dashlets li:nth-child(2) [data-value='1']").click();
		sugar().home.dashboard.addDashlet(3,1);

		// Add a dashlet -> select "List View" dashlet view.
		// TODO: VOOD-960 - Dashlet Selection
		FieldSet customData = testData.get(testName).get(0);
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customData.get("listView"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();

		// Under list view dashlet configuration page, Select Opportunities module
		new VoodooSelect("div", "css", ".fld_module .select2-container").set(sugar().opportunities.moduleNamePlural);

		// Create a filer base on "Account Name" "is is any of" a value
		new VoodooControl("i", "css", ".filter-view .select2-choice-type").click();
		new VoodooControl("li", "css", "#select2-drop ul li.select2-result-border-bottom").click();
		new VoodooSelect("a", "css", ".fld_filter_row_name .select2-default").set(customData.get("filter"));
		new VoodooSelect("a", "css", ".fld_filter_row_operator .select2-default").set(customData.get("operator"));
		new VoodooControl("input", "css", ".fld_account_name .select2-default").set(accountName);
		VoodooUtils.waitForReady();
		new VoodooControl("ul", "css", "#select2-drop ul").click();
		VoodooUtils.waitForReady();

		// Save
		new VoodooControl("a", "css", ".active .fld_save_button a").click();

		// Verify data is appearing as per the filter selected
		new VoodooControl("span", "css", ".list.fld_name").assertEquals(oppData.get(1).get("name"), true);
		new VoodooControl("div", "css", ".dashlet-content .list-view").assertContains(oppData.get(0).get("name"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}