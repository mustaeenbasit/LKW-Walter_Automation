package com.sugarcrm.test.reports;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_29147 extends SugarTest {
	VoodooControl activeDropdown, queryLocked, saveBtnCtrl, advancedReportingCtrl, viewCustomQueryCtrl, customQueryRecord;

	public void setup() throws Exception {
		FieldSet customQueryData = testData.get(testName).get(0);
		sugar().login();

		// Define controls 
		advancedReportingCtrl = sugar().reports.menu.getControl("manageAdvancedReports");
		// TODO: VOOD-1559
		activeDropdown = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		queryLocked = new VoodooControl("input", "id", "query_locked");
		saveBtnCtrl = new VoodooControl("input", "css", "input[title='Save']");
		VoodooControl activeDropdownValue = new VoodooControl("a", "css", ".dropdown.active .dropdown-menu.scroll ");
		viewCustomQueryCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_CUSTOMQUERIES']");
		customQueryRecord = new VoodooControl("a", "css", ".list.view .oddListRowS1 a");
		VoodooControl createCustomQueryCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_NEW_CUSTOMQUERY']");

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

		// A record is exist in Custom Query module(Make sure Check box of Lock Query field is checked)
		customQueryRecord.click();
		VoodooUtils.focusFrame("bwc-frame");
		queryLocked.set(Boolean.toString(true));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Unchecked value is showing at Lock Query field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_29147_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on Advanced Reports -> View Custom Queries
		activeDropdown.click();
		viewCustomQueryCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on that record which is created in Prerequisite
		customQueryRecord.click();
		VoodooUtils.focusFrame("bwc-frame");

		// Uncheck the check box of Lock Query field and click on Save button
		queryLocked.set(Boolean.toString(false));
		saveBtnCtrl.click();

		// Click again on that record which is saved
		customQueryRecord.click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the Unchecked value should be displayed at Lock Query field
		Assert.assertFalse("Lock Query field is checked", queryLocked.isChecked());
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}