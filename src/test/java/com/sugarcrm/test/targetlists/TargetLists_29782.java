package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_29782 extends SugarTest {

	public void setup() throws Exception {
		// Creating TargetList 
		sugar().targetlists.api.create();

		sugar().login();

		// Navigating to reports module 
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// Select Row and Column Report Type > Targets Module
		// TODO: VOOD-822
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Targets").click();

		// Set filter for Accounts name
		// TODO: VOOD-822
		new VoodooControl("tr", "css", "#Prospects_account_name").click();
		new VoodooControl("input", "css", ".bd input[type='text']").set(sugar().targets.getDefaultData().get("account_name"));
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();

		// Configure display fields for report
		new VoodooControl("tr", "css", "[id='Prospects_account_name']").click();
		new VoodooControl("tr", "css", "[id='Prospects_first_name']").click();
		new VoodooControl("tr", "css", "[id='Prospects_last_name']").click();
		nextBtnCtrl.click();

		// Save and Run report
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "css", "#saveAndRunButton").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Filter should work in 'Search and Select Reports' drawer.
	 *  @throws Exception
	 */
	@Test
	public void TargetLists_29782_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to TargetList
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1); 
		StandardSubpanel targetsSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural);

		// Selecting 'Select from Report' in Targets Subpanel 
		targetsSubpanel.clickOnSelectFromReport();

		// Verifying name of the report displayed on SSV
		// TODO: VOOD-1162, VOOD-1487
		new VoodooControl("span", "css", ".list.fld_name").assertEquals(testName, true);

		// Verifying non target type report does not appear on SSV 
		// by verifying that only one record is appearing and i.e report belong to Target (verified above)
		Assert.assertTrue("Search and Select Reports have more than 1 report" , sugar().reports.searchSelect.countRows() == 1);

		// Change any filter criteria
		String contacts = sugar().contacts.moduleNamePlural;
		// Add contacts in filter
		new VoodooSelect("li", "css", ".select2-search-field").set(contacts);
		VoodooUtils.waitForReady();

		// Verify report related to selected module appear
		Assert.assertTrue("Search and Select Reports doesn't have 2 reports" , sugar().reports.searchSelect.countRows() == 2);

		// Verify contact module's related report(Report Name & Module Name) start appearing
		FieldSet customData = testData.get(testName).get(0);
		String report =  ".dataTable.search-and-select tr:nth-child(2) td:nth-child(%d)";

		new VoodooControl("table", "css", String.format(report, 2)).assertEquals(customData.get("reportName"), true);
		new VoodooControl("table", "css", String.format(report, 3)).assertEquals(contacts, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}