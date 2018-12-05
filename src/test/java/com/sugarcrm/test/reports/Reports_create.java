package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_create extends SugarTest {
	DataSource reportsDS = new DataSource();

	public void setup() throws Exception {
		reportsDS = testData.get("env_reports_verification");
		sugar().contacts.api.create();
		sugar().login();
	}

	@Test
	public void Reports_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-822
		// Create report (Rows and Columns) by UI
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Contacts").click();
		VoodooControl lastName = new VoodooControl("input", "id", "Contacts_last_name");
		lastName.click();
		new VoodooControl("img", "css", "input[name=text_input]").set(sugar().contacts.getDefaultData().get("lastName"));
		VoodooControl nextBtn = new VoodooControl("input", "id", "nextBtn");
		nextBtn.click();
		new VoodooControl("input", "id", "Contacts_first_name").click();
		lastName.click();
		nextBtn.click();
		new VoodooControl("input", "id", "save_report_as").set(reportsDS.get(0).get("value"));
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify report record
		new VoodooControl("b", "css", "#reportDetailsTable tr td b").assertEquals(reportsDS.get(0).get("label"), true);
		new VoodooControl("td", "css", "#reportDetailsTable tr td").assertContains(reportsDS.get(0).get("value"), true);

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

		new VoodooControl("td", "css", "tr.oddListRowS1 td").assertContains(sugar().contacts.getDefaultData().get("firstName"), true);
		new VoodooControl("td", "css", "tr.oddListRowS1 td:nth-of-type(2)").assertEquals(sugar().contacts.getDefaultData().get("lastName"), true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}