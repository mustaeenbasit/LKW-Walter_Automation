package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_28066 extends SugarTest {
	FieldSet reportData; 
	VoodooControl nameCtrl, advanceSearchCtrl;

	public void setup() throws Exception {
		reportData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that Refreshing report detail view should stay on the report page after updating run time filter
	 * @throws Exception
	 */
	@Test
	public void Reports_28066_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-822
		// Open a report with run time filters
		sugar().reports.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		nameCtrl = new VoodooControl("input", "name", "name");
		nameCtrl.set(reportData.get("reportName"));
		advanceSearchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		advanceSearchCtrl.click();
		new VoodooControl("a", "css", "#MassUpdate tr.oddListRowS1 td:nth-child(4) span a").click();

		// Change a run time filter and click "Apply". Wait for the report to run.
		VoodooControl filterCtrl = new VoodooControl("select", "css", "#filters select[name='qualify']");
		VoodooControl filterValueCtrl = new VoodooControl("option", "css", "#filters_top option[value='not_empty']");
		VoodooControl applyBtnCtrl = new VoodooControl("input", "css", "#filters_tab input[value='Apply']");
		VoodooUtils.focusFrame("bwc-frame");
		filterCtrl.click();
		filterValueCtrl.click();

		// Click Apply
		applyBtnCtrl.click();

		// Click refresh in the browser.
		VoodooUtils.refresh();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the user should stay on the report page after refresh instead of redirects to home page.
		sugar().reports.detailView.assertVisible(true);
		filterCtrl.assertVisible(true);
		filterValueCtrl.assertVisible(true);
		applyBtnCtrl.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}