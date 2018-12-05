package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_29150 extends SugarTest {
	VoodooControl titleFieldCtrl, searchBtnCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Create link is not showing when Search a report in Reports Module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_29150_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet reportSearchData = testData.get(testName).get(0);

		// Go to Reports Module
		sugar().reports.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Define controls
		searchBtnCtrl = sugar().reports.listView.getControl("searchButton");

		// TODO: VOOD-822
		titleFieldCtrl = new VoodooControl("input", "css", "input[name='name_basic']");
		VoodooControl noResultMessageCtrl = new VoodooControl("p", "css", ".listViewEmpty p");

		// Enter "Test" value in Title text field
		sugar().reports.listView.getControl("basicSearchLink").click();
		titleFieldCtrl.set(reportSearchData.get("reportName"));

		// Click on Search button
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that No results found for "test" message should be displayed
		sugar().reports.listView.getControl("checkbox01").assertExists(false);
		noResultMessageCtrl.assertContains(reportSearchData.get("noResultMessage"), true);

		// Verify that the "Create "test" as a new Report" link should not be displayed. 
		sugar().reports.listView.assertContains(reportSearchData.get("newReportMessage"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}