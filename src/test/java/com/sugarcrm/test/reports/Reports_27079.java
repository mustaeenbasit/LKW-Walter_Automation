package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_27079 extends SugarTest{
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Creating 3 Contacts records
		sugar().contacts.api.create();
		sugar().contacts.api.create();
		sugar().contacts.api.create();

		// Creating a Account record
		FieldSet newData = new FieldSet();
		newData.put("name", customData.get("account_name"));
		sugar().accounts.api.create(newData);
		sugar().login();
	}

	/**
	 * Verify that values on the scale of x-axis and y-axis are generated properly in horizontal chart of a summation report with details
	 * @throws Exception
	 */
	@Test
	public void Reports_27079_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-822 -need lib support of reports module
		VoodooControl createRowsAndColumnsReportCtrl = new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(3) table tbody tr:nth-child(1) td");
		VoodooControl accountsModuleCtrl = new VoodooControl("table", "id", "Contacts");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");

		// Creating Rows And Columns Report
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		createRowsAndColumnsReportCtrl.click();
		accountsModuleCtrl.click();
		nextBtnCtrl.click();

		// Define Group By
		new VoodooControl("tr", "id", "Contacts_primary_address_country").click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Contacts_count").click();
		nextBtnCtrl.click();

		// Displayed columns: Account > Name, Billing Street 
		new VoodooControl("tr", "id", "Contacts_first_name").click();
		new VoodooControl("tr", "id", "Contacts_last_name").click();
		new VoodooControl("tr", "id", "Contacts_primary_address_country").click();
		nextBtnCtrl.click();
		new VoodooControl("select", "id", "chart_type").set("Horizontal Bar");
		new VoodooControl("input", "css", "#chart_options_div > table:nth-child(1) input#nextButton").click();

		// Report name and save
		new VoodooControl("input", "id", "save_report_as").set(customData.get("name"));
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// Verify graph value
		// TODO: VOOD-1171 -Need chart report lib support for Reports module
		VoodooUtils.pause(5000); // Wait added to allow chart to populate before assertion.
		new VoodooControl("text", "css", "g.nv-y.nv-axis > g > g > g:nth-child(10) > text").assertEquals(customData.get("graphValue"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}