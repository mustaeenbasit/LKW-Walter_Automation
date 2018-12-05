package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_update extends SugarTest {
	DataSource reportsDS = new DataSource();
	VoodooControl nextBtnCtrl, reportNameCtrl, saveAndRunCtrl;
	String reportName = "";

	public void setup() throws Exception {
		reportsDS = testData.get("env_reports_verification");
		sugar().contacts.api.create();
		reportName = reportsDS.get(0).get("value");

		// TODO: VOOD-822
		reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		saveAndRunCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");
		nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		sugar().login();

		// Create report (Rows and Columns) by UI
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Contacts").click();
		VoodooControl lastNameCtrl = new VoodooControl("input", "id", "Contacts_last_name");
		lastNameCtrl.click();
		new VoodooControl("img", "css", "input[name=text_input]").set(sugar().contacts.getDefaultData().get("lastName"));
		nextBtnCtrl.click();
		new VoodooControl("input", "id", "Contacts_first_name").click();
		lastNameCtrl.click();
		nextBtnCtrl.click();
		reportNameCtrl.set(reportName);
		saveAndRunCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
	}

	@Test
	public void Reports_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-822
		// Edit report via UI.
		new VoodooControl("span", "css", ".sugar_action_button .ab").click();
		new VoodooControl("a", "id", "editReportButton").click();
		new VoodooControl("img", "css", "input[name=text_input]").set(sugar().contacts.getDefaultData().get("firstName"));
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
		reportName = testName;

		// Verify report record with new values
		new VoodooControl("b", "css", "#reportDetailsTable tr td b").assertEquals(reportsDS.get(0).get("label"), true);
		new VoodooControl("td", "css", "#reportDetailsTable tr td").assertContains(reportName, true);

		new VoodooControl("b", "css", "#reportDetailsTable tr td:nth-of-type(2) b").assertEquals(reportsDS.get(1).get("label"), true);
		new VoodooControl("td", "css", "#reportDetailsTable tr td:nth-of-type(2)").assertContains(reportsDS.get(1).get("value"), true);

		new VoodooControl("b", "css", "#reportDetailsTable tr:nth-of-type(2) td b").assertEquals(reportsDS.get(2).get("label"), true);
		new VoodooControl("td", "css", "#reportDetailsTable tr:nth-of-type(2) td").assertContains(reportsDS.get(2).get("value"), true);

		new VoodooControl("b", "css", "#reportDetailsTable tr:nth-of-type(2) td:nth-of-type(2) b").assertEquals(reportsDS.get(3).get("label"), true);
		new VoodooControl("td", "css", "#reportDetailsTable tr:nth-of-type(2) td:nth-of-type(2)").assertContains(reportsDS.get(3).get("value"), true);

		new VoodooControl("b", "css", "#reportDetailsTable tr:nth-of-type(3) td b").assertEquals(reportsDS.get(4).get("label"), true);
		new VoodooControl("td", "css", "#reportDetailsTable tr:nth-of-type(3) td").assertContains(reportsDS.get(4).get("value"), true);

		new VoodooControl("b", "css", "#reportDetailsTable tr:nth-of-type(3) td:nth-of-type(2) b").assertEquals(reportsDS.get(5).get("label"), true);
		new VoodooControl("td", "css", "#reportDetailsTable tr:nth-of-type(3) td:nth-of-type(2)").assertContains(reportsDS.get(5).get("value"), true);

		new VoodooControl("td", "css", "tr.evenListRowS1 td").assertContains("No results.", true);
		new VoodooControl("td", "css", "tr.evenListRowS1 td:nth-of-type(2)").assertContains("No results.", true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}