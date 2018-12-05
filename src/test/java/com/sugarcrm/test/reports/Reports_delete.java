package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_delete extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-822
		// Create report by UI
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Contacts").click();
		VoodooControl nextBtn = new VoodooControl("input", "id", "nextBtn");
		nextBtn.click();
		new VoodooControl("input", "id", "Contacts_last_name").click();
		nextBtn.click();
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	@Test
	public void Reports_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-822
		// Delete the report using the UI.
		new VoodooControl("span", "css", ".sugar_action_button .ab").click();
		new VoodooControl("a", "id", "deleteReportButton").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();

		// Verify the report was deleted.
		VoodooControl submitSearch = new VoodooControl("input", "id", "search_form_submit_advanced"); 
		new VoodooControl("input", "css", "#Reportsadvanced_searchSearchForm [name='name']").set(testName);
		try {
			submitSearch.click();
			VoodooUtils.waitForReady();
			new VoodooControl("p", "css", ".listViewEmpty .msg").assertEquals("No results found.", true);
		} finally {
			// clear and submit search
			new VoodooControl("input", "id", "search_form_clear_advanced").click();
			submitSearch.click();
		}

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}