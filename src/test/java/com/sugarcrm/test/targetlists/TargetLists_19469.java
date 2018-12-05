package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19469 extends SugarTest {

	public void setup() throws Exception {
		// Creating TargetList and few Targets record
		sugar.targetlists.api.create();
		FieldSet fs = new FieldSet();
		for (int i = 0; i < 2; i++) {
			fs.put("lastName", testName+"_"+i);
			sugar.targets.api.create(fs);
			fs.clear();
		}
		sugar.login();

		// Navigating to reports module 
		sugar.navbar.navToModule(sugar.reports.moduleNamePlural);
		VoodooUtils.waitForReady();

		// Create Rows and Columns report, select Targets module
		sugar.navbar.clickModuleDropdown(sugar.reports);
		sugar.reports.menu.getControl("createReport").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Select Row and Column Report Type > Targets Module
		// TODO: VOOD-822
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Targets").click();

		// Set filter for Accounts name
		// TODO: VOOD-822
		new VoodooControl("tr", "css", "#Prospects_account_name").click();
		new VoodooControl("input", "css", ".bd input[type='text']").set(sugar.targets.getDefaultData().get("account_name"));
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
	 * TC 19469: Target List - Targets management_Verify that "Cancel" function in the popup select reports works correctly
	 *  @throws Exception
	 */
	@Test
	public void TargetLists_19469_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Selecting 'Select from Report' in Targets Subpanel and clicking cancel
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1); 
		StandardSubpanel targetsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural);
		targetsSubpanel.clickOnSelectFromReport();
		sugar.reports.searchSelect.getControl("cancel").click();

		// Verifying the target list detail view is displayed correctly
		String listName = sugar.targetlists.getDefaultData().get("targetlistName");
		sugar.targetlists.recordView.assertVisible(true);
		sugar.targetlists.recordView.getDetailField("targetlistName").assertEquals(listName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}