package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_19001 extends SugarTest {
	VoodooControl reportModuleCtrl;
	FieldSet myData = new FieldSet();

	public void setup() throws Exception {
		// Create account record
		sugar().accounts.api.create();
		sugar().login();
		myData = testData.get(testName).get(0);

		// Create two Calls records and assigned it to created account
		FieldSet data = new FieldSet();	
		data.put("relatedToParentType", sugar().accounts.moduleNameSingular);
		data.put("relatedToParentName", sugar().accounts.getDefaultData().get("name"));
		sugar().calls.create(data);
		data.clear();
		data.put("name", myData.get("name"));
		data.put("relatedToParentType", sugar().accounts.moduleNameSingular);
		data.put("relatedToParentName", sugar().accounts.getDefaultData().get("name"));
		sugar().calls.create(data);
	}

	/**
	 * Summation Report Group By results should be sorted correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_19001_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Custom Report in Calls module
		// TODO: VOOD-822
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		VoodooControl createSummationReportCtrl = new VoodooControl("img", "css", "img[name='summationImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl nxtbtnCtrl = new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		VoodooUtils.focusDefault();
		reportModuleCtrl.click();
		new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li:nth-of-type(1)").click();
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", "Calls").click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Calls_date_entered").click();
		new VoodooControl("tr", "id", "Calls_name").click();
		new VoodooControl("a", "id", "ygtvlabelel2").click();
		new VoodooControl("tr", "id", "Accounts_name").click();
		nextBtnCtrl.click();
		new VoodooControl("input", "id", "summary_order_by_radio_display_summaries_row_group_by_row_1").click();
		new VoodooControl("tr", "id", "Calls_count").click();
		nextBtnCtrl.click();
		new VoodooControl("option", "css", "select#chart_type option[value='hBarF']").click();
		nxtbtnCtrl.click();
		reportNameCtrl.set(myData.get("report_name"));
		saveAndRunCtrl.click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify, the correct account name is displayed under each group by heading
		new VoodooControl("td", "css", ".reportlistView tr:nth-child(2) td:nth-child(3)").assertContains(sugar().accounts.getDefaultData().get("name"),true);
		new VoodooControl("td", "css", ".reportlistView tr:nth-child(3) td:nth-child(3)").assertContains(sugar().accounts.getDefaultData().get("name"),true);
		new VoodooControl("td", "css", ".reportlistView tr:nth-child(2) td:nth-child(2)").assertContains(sugar().calls.getDefaultData().get("name"),true);
		new VoodooControl("td", "css", ".reportlistView tr:nth-child(3) td:nth-child(2)").assertContains(myData.get("name"),true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}