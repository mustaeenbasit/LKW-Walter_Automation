package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_28977 extends SugarTest {
	VoodooControl activeDropdown, advancedReportingCtrl, viewCustomQueryCtrl, createDataFormatCtrl, saveBtnCtrl, viewDataFormatCtrl;

	public void setup() throws Exception {
		FieldSet customQueryData = testData.get(testName).get(0);
		sugar().login();

		// Define controls
		// TODO: VOOD-1559
		advancedReportingCtrl = sugar().reports.menu.getControl("manageAdvancedReports");
		activeDropdown = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		saveBtnCtrl = new VoodooControl("input", "css", "input[title='Save']");
		VoodooControl activeDropdownValue = new VoodooControl("a", "css", ".dropdown.active .dropdown-menu.scroll ");
		viewCustomQueryCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_CUSTOMQUERIES']");
		VoodooControl createCustomQueryCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_NEW_CUSTOMQUERY']");
		createDataFormatCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_NEW_DATASET']");
		viewDataFormatCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_LIST_DATASET']");

		// Go to Reports Module
		sugar().reports.navToListView();

		// Click on Reports -> Manage Advanced Reports
		activeDropdown.click();
		advancedReportingCtrl.click();

		// Click on Advanced Reports -> Create Custom Queries
		activeDropdown.click();
		createCustomQueryCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		// Create a Custom Query record
		// TODO: VOOD-1559
		new VoodooControl("input", "css", "input[name='name']").set(customQueryData.get("name"));
		new VoodooControl("textarea", "css", "textarea[name='custom_query']").set(customQueryData.get("customQuery"));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Type column should not be displayed at Data Formats list view page
	 *
	 * @throws Exception
	 */
	@Test
	public void Reports_28977_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Advanced Reports -> Create Data Format
		// TODO: VOOD-1057
		activeDropdown.click();
		createDataFormatCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "input[name='name']").set(testName);

		// Select custom query
		new VoodooControl("button", "id", "btn_query_name").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(1) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Click on Advanced Reports->View Data Formats
		activeDropdown.click();
		viewDataFormatCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that 'Type' column should not be displayed at Data Format List view page
		new VoodooControl("tr", "css", "#MassUpdate > table > tbody > tr:nth-child(2)").assertContains("Type", false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}