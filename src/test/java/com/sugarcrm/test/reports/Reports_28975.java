package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_28975 extends SugarTest {
	VoodooControl activeDropdown, advancedReportingCtrl, viewCustomQueryCtrl, createDataFormatCtrl;

	public void setup() throws Exception {
		FieldSet customQueryData = testData.get(testName).get(0);
		sugar().login();

		// Define controls
		// TODO: VOOD-1559
		advancedReportingCtrl = sugar().reports.menu.getControl("manageAdvancedReports");
		activeDropdown = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "input[title='Save']");
		VoodooControl activeDropdownValue = new VoodooControl("a", "css", ".dropdown.active .dropdown-menu.scroll ");
		viewCustomQueryCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_CUSTOMQUERIES']");
		VoodooControl createCustomQueryCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_NEW_CUSTOMQUERY']");
		createDataFormatCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_NEW_DATASET']");

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
	 * Verify that Query Type column should not be displayed at search field and listview column in the popup of Custom Query List
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_28975_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on Advanced Reports -> Create Data Format
		activeDropdown.click();
		createDataFormatCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click on Select arrow icon at Query Name field
		new VoodooControl("button", "id", "btn_query_name").click();
		VoodooUtils.focusWindow(1);
		
		// Verify that "Query Type" should not be displayed at search field and listview column in the popup
		// TODO: VOOD-1559
		new VoodooControl("table", "css", "#popup_query_form tr:nth-child(1) > td > table").assertContains("Query Type", false);
		new VoodooControl("table", "css", "body > table.list.view").assertContains("Query Type", false);
		
		// Select custom query
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(1) a").click();
		VoodooUtils.focusWindow(0);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}