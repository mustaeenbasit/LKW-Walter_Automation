package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20214 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Verify module name in report wizard sync with updating module name
	 * @throws Exception
	 */
	@Test
	public void Admin_20214_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Rename Cases Module
		sugar().admin.renameModule(sugar().cases, customData.get("caseSingularName"), customData.get("casePluralName"));

		// Navigate to report module 
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		
		// VOOD-1057 & VOOD-822 
		// click Summation Report
		new VoodooControl("td", "css", "#report_type_div > table > tbody > tr:nth-child(2) > td:nth-child(1) > table > tbody > tr:nth-child(3) > td:nth-child(1)").click();
		VoodooControl casesModuleCtrl = new VoodooControl("table", "id", "Cas");

		// Assert that Cases should be "Cas"
		casesModuleCtrl.assertContains(customData.get("casePluralName"), true);
		casesModuleCtrl.click();
		new VoodooControl("div", "id", "module_tree").assertContains(customData.get("casePluralName"), true);
		new VoodooControl("tr", "id", "Cases_name").click();

		// select the case record
		new VoodooControl("input", "css", "td:nth-child(4) table tbody tr td:nth-child(2) input").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "body > table.list.view > tbody > tr.oddListRowS1 > td:nth-child(2) > a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Cases_count").click();
		nextBtnCtrl.click();
		new VoodooControl("input", "css", "#chart_options_div > table:nth-child(1) input:nth-child(2)").click();
		new VoodooControl("input", "id", "save_report_as").set(customData.get("reportName"));
		// save and run  the report 
		new VoodooControl("input", "id", "saveAndRunButton").click();
		sugar().alerts.waitForLoadingExpiration();

		// Assert that inside report module name is Cas
		new VoodooControl("td", "css", "#reportDetailsTable  tr:nth-child(2) > td:nth-child(1)").assertContains(customData.get("casePluralName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}