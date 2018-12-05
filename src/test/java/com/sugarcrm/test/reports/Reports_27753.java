package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Reports_27753 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		
		sugar().login();
		sugar().opportunities.create();
	}

	/**
	 * Verify that Reporting shows Revenue Line Items as an available module for inclusion in a report
	 * when the Opportunities setting is set to "Opportunities + RLI"
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_27753_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.selectMenuItem(sugar().reports, "createReport");

		// TODO: VOOD-822
		VoodooControl createRowsAndColumnsReportCtrl = new VoodooControl("img", "css", "[name='rowsColsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Opportunity report which uses Opportunity -> Sales Stage is created 
		createRowsAndColumnsReportCtrl.click();
		VoodooControl opportunitiesCtrl = new VoodooControl("table", "id", sugar().opportunities.moduleNamePlural);
		VoodooControl salesStageCtrl = new VoodooControl("tr", "id", "Opportunities_sales_stage");

		opportunitiesCtrl.click();
		salesStageCtrl.click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Opportunities_name").click();
		salesStageCtrl.click();
		nextBtnCtrl.click();
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
		VoodooControl modulesCtrl= new VoodooControl("td", "css", "#reportDetailsTable > tbody > tr:nth-child(2) > td:nth-child(1)");

		// verify that Opportunities module is displayed in module list 
		modulesCtrl.assertContains(sugar().opportunities.moduleNamePlural, true);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// admin -> Opportunities and set it to be Opportunities + RLIs 
		sugar().admin.api.switchToRevenueLineItemsView();
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		createRowsAndColumnsReportCtrl.click();

		// Verify that Reporting shows Revenue Line Items, as well as Opportunities as available modules for inclusion in a report.
		new VoodooControl("table", "css", "table[id='Revenue Line Items']").assertVisible(true);
		opportunitiesCtrl.assertVisible(true);
		VoodooUtils.focusDefault();

		sugar().reports.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "basic_search_link").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "[name='name_basic']").set(testName);
		VoodooUtils.focusDefault();
		sugar().reports.listView.submitSearchForm();
		
		// TODO: VOOD-1408 
		//sugar().reports.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#MassUpdate  tr:nth-child(3) td:nth-child(4) a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		// verify that Revenue Line Items module is displayed in module list 
		modulesCtrl.assertContains("Revenue Line Items", true);
		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}