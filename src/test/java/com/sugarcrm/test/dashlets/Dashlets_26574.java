package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_26574 extends SugarTest {
	FieldSet customFS;

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		AccountRecord myAccountRecord = (AccountRecord) sugar.accounts.api.create();
		sugar.login();

		// Create Opportunities with Revenue Line Item 
		sugar.opportunities.navToListView();
		for(int i = 0; i < 2; i++) {
			sugar.opportunities.listView.create();
			sugar.opportunities.createDrawer.getEditField("name").set(testName+"_"+i);
			sugar.opportunities.createDrawer.getEditField("relAccountName").set(myAccountRecord.getRecordIdentifier());
			sugar.opportunities.createDrawer.showMore();
			// One record assign to QAUser
			if(i == 0)
				sugar.opportunities.createDrawer.getEditField("relAssignedTo").set(sugar.users.qaUser.get("userName"));

			sugar.opportunities.createDrawer.getEditField("rli_name").set(testName);
			sugar.opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar.opportunities.getDefaultData().get("rli_expected_closed_date"));
			sugar.opportunities.createDrawer.getEditField("rli_likely").set(customFS.get("rli_likely"));
			sugar.opportunities.createDrawer.save();
		}
	}

	/**
	 * Chart from report is displaying correctly in Dashbord
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_26574_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-822
		// Create Matrix Report with Opportunities module
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");

		// Go to Reports -> Create reports -> Matrix -> Opportunities 
		sugar.navbar.selectMenuItem(sugar.reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "img[name='matrixImg']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("table", "id", "Opportunities").click();
		nextBtnCtrl.click();

		// On the 'define group by' step, select 'Assigned to User > User Name,Month: Expected Close Date, Sales Stage' and click next
		// TODO: VOOD-822
		new VoodooControl("a", "css", "#module_tree .ygtvchildren .ygtvchildren div:nth-child(2) table tr td:nth-child(3) a").click();
		 new VoodooControl("tr", "id", "Users_user_name").click();
		new VoodooControl("a", "css", "#module_tree .ygtvchildren table tr.ygtvrow td:nth-child(2) a.ygtvlabel").click();
		new VoodooControl("tr", "css", "[id='Opportunities_date_closed:month']").click();
		new VoodooControl("a", "css", "#module_tree .ygtvchildren .ygtvchildren div:nth-child(17) table tr td:nth-child(3) a").click();
		new VoodooControl("tr", "id", "RevenueLineItems_sales_stage").click();
		nextBtnCtrl.click();

		// Display Summaries with (Opportunities > Count, sort user name by ascending)
		new VoodooControl("tr", "id", "Opportunities_count").click();
		new VoodooControl("input", "css", "#display_summaries_row_group_by_row_1 [name='summary_order_by_radio']").click(); // set Order by ASC
		nextBtnCtrl.click();

		// Choose Chart Type > Vertical Bar
		new VoodooControl("select", "css", "select[id='chart_type']").set(customFS.get("chartType"));
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(1) tr td [id='nextButton']").click();

		// Name the report and then click 'Save and run'
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "css", "input[name='Save and Run']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// TODO: VOOD-960
		// Go to Home -> My Dashboard -> Edit
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		sugar.home.dashboard.edit();

		// Add a Dashlet
		sugar.home.dashboard.addRow();
		VoodooUtils.waitForReady();
		sugar.home.dashboard.addDashlet(4, 1);

		// Add a Dashlet -> Select "Saved Reports Chart Dashlet"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customFS.get("savedReportsChartDashlet"));
		VoodooUtils.waitForReady();
		
		// Click report
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();

		// Select a report: Select created report
		new VoodooSelect("div", "css", ".fld_saved_report_id .select2-container").set(testName);
		VoodooUtils.waitForReady(); 
		new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page
		// TODO: VOOD-1645
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();

		// The report will be aligned correctly and the chart displays correctly. 
		// Verify that Check legend, name of users, order of the name of users - user name by ascending(admin,qauser)
		// TODO: VOOD-960
		new VoodooControl("text", "css", "g.nv-scroll-wrap g.nv-x.nv-axis > g > g > g:nth-child(1) text").assertEquals(customFS.get("verifyUser1"), true);
		new VoodooControl("text", "css", "g.nv-scroll-wrap g.nv-x.nv-axis > g > g > g:nth-child(2) text").assertEquals(customFS.get("verifyUser2"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}