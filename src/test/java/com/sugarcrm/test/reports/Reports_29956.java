package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_29956 extends SugarTest {
	public void setup() throws Exception {
		// Create a contact record with First name and Last name
		sugar().contacts.api.create();

		// Login
		sugar().login();
	}

	/**
	 * Verify that Summation Report records should be shown in chart
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_29956_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		DataSource contactsData = testData.get(testName + "_" + sugar().contacts.moduleNamePlural);

		// Define Controls
		// TODO: VOOD-822
		// Create Custom(Summation) Report in Opportunities module
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "img[name='summationImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");
		VoodooControl lastNameCtrl = new VoodooControl("tr", "id", "Contacts_last_name");

		// Define controls for summary or chart page of reports module
		// TODO: VOOD-822
		VoodooControl countCtrl = new VoodooControl("text", "css", ".nv-titleWrap text");
		VoodooControl lastNameAxisCtrl = new VoodooControl("text", "css", ".chartContainer .nv-chart.nv-barChart .nv-x text");
		VoodooControl firstRowLastNameCtrl = new VoodooControl("td", "css", ".oddListRowS1");
		VoodooControl firstRowCountCtrl = new VoodooControl("td", "css", ".oddListRowS1:nth-child(2)");
		VoodooControl grandTotalCtrl = new VoodooControl("td", "css", ".list.view .Array td:nth-child(2)");
		VoodooControl warningMessageCtrl = new VoodooControl("div", "css", ".nv-data-error");
		String lastName = sugar().contacts.getDefaultData().get("lastName");

		// Create a Summation Report with detail on contact module without any filter, group by name, choose any chart
		// Go to report modules and create Summation Reports for Contacts module. 
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("table", "id", "Contacts").click();
		VoodooUtils.waitForReady();

		// Define Filters : Without filters
		nextBtnCtrl.click();

		// Define Group By: Name
		lastNameCtrl.scrollIntoView();
		lastNameCtrl.click();
		nextBtnCtrl.click();

		// Display Summaries : Count
		// TODO: VOOD-822
		new VoodooControl("tr", "id", "Contacts_count").click();
		nextBtnCtrl.click();

		// Chart Options : choose any chart
		// TODO: VOOD-822
		new VoodooControl("select", "id", "chart_type").set("Horizontal Bar");
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton").click();

		// Select Name file to show on the detail list. Save and Run Report.
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();
		VoodooUtils.waitForReady(30000); // Needed extra wait

		// Verify that Chart should show the correct result
		countCtrl.waitForVisible();
		countCtrl.assertEquals(customData.get("count"), true);
		lastNameAxisCtrl.assertEquals(lastName, true);
		firstRowCountCtrl.assertEquals(customData.get("oneRecord"), true);
		firstRowLastNameCtrl.assertEquals(lastName, true);
		grandTotalCtrl.assertEquals(customData.get("oneRecord"), true);

		// Need to create 60+ records
		sugar().contacts.api.create(contactsData);

		// Rerun the report
		new VoodooControl("a", "id", "runReportButton").click();
		VoodooUtils.waitForReady(30000); // Extra wait needed

		// Verify that if it contains more than 60 items it will display the message 'The chart cannot be displayed due to its configuration'
		warningMessageCtrl.waitForVisible();
		warningMessageCtrl.assertEquals(customData.get("warningMessage"), true);
		grandTotalCtrl.assertEquals(customData.get("moreRecords"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}