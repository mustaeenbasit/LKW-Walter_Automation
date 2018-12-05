package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_26950 extends SugarTest {
	UserRecord max;
	
	public void setup() throws Exception {
		DataSource userData = testData.get(testName+"_newUser");
		sugar().login();
		
		// TODO: VOOD-1200
		// Create Max user
		max = (UserRecord) sugar().users.create(userData.get(0));

		// TODO: VOOD-822
		// Create Custom(Rows and Columns Report) Report in Accounts module
		VoodooControl rowsColumsCtrl = new VoodooControl("td", "css", "img[name='rowsColsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");

		// Go to Reports -> Create reports -> summation report -> Opportunities
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		rowsColumsCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("table", "id", "Accounts").click();
		VoodooUtils.waitForReady();
		nextBtnCtrl.click();

		new VoodooControl("td", "css", "#module_tree .ygtvchildren#ygtvc1 tr .ygtvcell.ygtvcontent").click();
		new VoodooControl("td", "css", "#module_fields #Users_full_name").click();
		VoodooUtils.waitForReady();
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Name the report and then click 'Save and run'
		reportNameCtrl.set(testName);
		new VoodooControl("input", "css", "#report_details_table input#assigned_user_name").set(max.getRecordIdentifier());
		VoodooUtils.waitForReady();
		saveAndRunCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify the full name is shown and displayed under "Assigned to" column
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_26950_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Reports Module
		sugar.reports.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		// Enter "Test" value in Title text field
		sugar.reports.listView.getControl("basicSearchLink").click();
		new VoodooControl("input", "css", "input[name='name_basic']").set(testName);

		// Click on Search button
		sugar.reports.listView.getControl("searchButton").click();
		VoodooUtils.waitForReady();

		// Verify that the "Create "test" as a new Report" link should not be displayed. 
		sugar.reports.listView.assertContains(testName, true);
		sugar.reports.listView.assertContains(max.get("firstName") +" "+ max.get("lastName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}