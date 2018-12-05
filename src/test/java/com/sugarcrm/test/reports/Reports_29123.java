package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Reports_29123 extends SugarTest {
	FieldSet customFS;
	
	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		AccountRecord myAccountRecord = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
		
		// Create a opportunity with negative value and assign it to QAuser 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(myAccountRecord.getRecordIdentifier());
		sugar().opportunities.createDrawer.showMore();
		sugar().opportunities.createDrawer.getEditField("relAssignedTo").set(sugar().users.qaUser.get("userName"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(testName);
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(customFS.get("negetive_value"));
		sugar().opportunities.createDrawer.save();
		
		// Logout as Admin
		sugar().logout();
	}

	/**
	 * Verify that Total should be displayed the numeric of negative amount.
	 * @throws Exception
	 */
	@Test
	public void Reports_29123_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Logout as QAuser
		sugar().login(sugar().users.getQAUser());
				
		// TODO: VOOD-822
		// Create Custom(Summation) Report in Opportunities module
		VoodooControl summationWithDetailsImgCtrl = new VoodooControl("td", "css", "img[name='summationWithDetailsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");

		// Go to Reports -> Create reports -> summation report -> Opportunities 
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		summationWithDetailsImgCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("table", "id", "Opportunities").click();
		nextBtnCtrl.click();

		// On the 'define group by' step, select 'Month: Date created' and click next
		// TODO: VOOD-822
		VoodooControl assignedToUser = new VoodooControl("a", "css", "#module_tree .ygtvchildren .ygtvchildren div:nth-child(2) table tr td:nth-child(3) a");
		assignedToUser.click();
		VoodooControl selectUserNamectrl = new VoodooControl("tr", "id", "Users_user_name");
		selectUserNamectrl.click();
		nextBtnCtrl.click();

		// On the 'choose display summaries' step, select 'AVG:Probability (%)', 'SUM: Probability(%)' and 'count' click to next.
		new VoodooControl("tr", "css", "tr[id='Opportunities_amount:sum']").click();
		nextBtnCtrl.click();

		// Choose display columns
		new VoodooControl("tr", "css", "tr[id='Opportunities_amount']").click();
		new VoodooControl("a", "css", "#module_tree > div > div > div > div > div:nth-child(1) > table > tbody > tr > td.ygtvcell.ygtvcontent > a").click();
		new VoodooControl("tr", "css", "tr[id='Accounts_name']").click();
		assignedToUser.click();
		selectUserNamectrl.click();
		nextBtnCtrl.click();
		
		// Choose Vertical chart
		new VoodooControl("select", "css", "select[id='chart_type']").set("Vertical Bar");
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(1) tr td [id='nextButton']").click();
		
		// Name the report and then click 'Save and run'
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();
		VoodooUtils.waitForReady();
		
		 // Verify that total should be displayed the numeric of negative amount.
		new VoodooControl("text", "css", ".reportChartContainer div.chartContainer div.scrollBars svg g.nv-titleWrap text.nv-title").assertContains(customFS.get("numeric_of_negative_amount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}