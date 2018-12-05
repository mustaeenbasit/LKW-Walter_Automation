package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19514 extends SugarTest {

	public void setup() throws Exception {
		sugar().targetlists.api.create();
		sugar().login();

		// Creating Rows And Columns Report for Users
		// TODO: VOOD-822
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Users").click();
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Choose Display Columns
		new VoodooControl("tr", "id", "Users_id").click();
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Report name and save
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Target List - Users management_Verify that "Cancel" function in popup search & select user windows works correctly
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19514_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to target list record view and click 'selectfromReport' in users subpanel
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);
		StandardSubpanel usersSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural);
		usersSubpanel.clickOnSelectFromReport();
		FieldSet reportData = testData.get(testName).get(0);

		// Verify that the user is navigated to search and select report window
		sugar().reports.searchSelect.getControl("moduleTitle").assertContains(reportData.get("searchSelectLabel"), true);

		// Verify that the result are populated based on User's Report
		// TODO: VOOD-1487
		new VoodooControl("span", "css", ".layout_Reports .choice-filter-label").assertContains(reportData.get("userReports"), true);

		// Clicking on Cancel
		sugar().reports.searchSelect.cancel();

		// Verify no records gets populated in User's subpanel
		Assert.assertTrue("Records populated in User's subpanel", usersSubpanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}