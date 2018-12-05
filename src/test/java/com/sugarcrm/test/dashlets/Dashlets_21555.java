package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21555 extends SugarTest{
	UserRecord sally, sarah;
	DataSource ds = new DataSource();
	String teamName, reportName;

	public void setup() throws Exception {
		sugar.login();

		ds = testData.get(testName);
		FieldSet fsTeam = testData.get(testName+"_team").get(0);
		teamName = fsTeam.get("teamName");
		reportName = fsTeam.get("reportName");

		// TODO: VOOD-822
		VoodooControl createSummationReportCtrl = new VoodooControl("img", "name", "summationImg");
		VoodooControl leadsModuleCtrl = new VoodooControl("table", "id", "Leads");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl fieldNameCtrl = new VoodooControl("tr", "id", "Leads_lead_source");
		VoodooControl mktonextBtnCtrl = new VoodooControl("tr", "id", "Leads_mkto_lead_score:sum");
		VoodooControl chartbtnCtrl = new VoodooControl("option", "css", "select[name='chart_type'] option:nth-of-type(2)");
		VoodooControl nextoBtnCtrl = new VoodooControl("input", "css", "div#chart_options_div table:nth-of-type(1) input#nextButton");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl searchTeamCtrl = new VoodooControl("input", "css", "#ReportsWizardForm_team_name_table tbody tr:nth-child(2) td:nth-child(1) span input.sqsEnabled.yui-ac-input");
		VoodooControl assignedToCtrl = new VoodooControl("input", "id", "assigned_user_name");
		VoodooControl removeTeamCtrl = new VoodooControl("input", "id", "remove_team_name_collection_0");
		VoodooControl setPrimaryCtrl = new VoodooControl("input", "id", "primary_team_name_collection_0");
		VoodooControl selectTeamCtrl = new VoodooControl("li", "css", "div#ReportsWizardForm_ReportsWizardForm_team_name_collection_0_results ul li:nth-of-type(1)");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "div#report_details_div table:nth-child(1) #saveAndRunButton");

		// Create two user
		sally = (UserRecord)sugar.users.create(ds.get(0));
		sarah = (UserRecord)sugar.users.create(ds.get(1));

		// Select Sally and Sarah for team 'West'
		for(int i = 0; i < ds.size(); i++) {
			sugar.teams.navToListView();
			sugar.teams.listView.clickRecord(3);
			VoodooUtils.focusFrame("bwc-frame");

			// TODO: VOOD-518
			// Select Sally for team 'West' and 'Will' for team 'East'
			new VoodooControl("a", "id", "team_memberships_select_button").click();
			VoodooUtils.waitForReady();
			VoodooUtils.focusWindow(1);
			new VoodooControl("input", "id", "user_name_advanced").set(ds.get(i).get("userName"));
			new VoodooControl("input", "id", "search_form_submit").click();
			new VoodooControl("input", "css", ".list.view tr:nth-child(3) td:nth-child(1) input").click();
			new VoodooControl("input", "id", "MassUpdate_select_button").click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusDefault();
		}

		// Logout as admin
		sugar.logout();

		// Log in sally and create a chart report called "Team report", make sure "Assigned To" field is 
		// "Sally" and set "Teams" field to "West" team
		sugar.login(sally);
		sugar.navbar.navToModule("Reports");
		VoodooUtils.focusDefault();
		sugar.navbar.clickModuleDropdown(sugar.reports);
		sugar.reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.click();
		leadsModuleCtrl.click();
		nextBtnCtrl.click();
		fieldNameCtrl.click();
		nextBtnCtrl.click();
		mktonextBtnCtrl.click();
		nextBtnCtrl.click();
		chartbtnCtrl.click();
		nextoBtnCtrl.click();
		reportNameCtrl.set(reportName);
		assignedToCtrl.set(ds.get(0).get("userName"));
		removeTeamCtrl.click();
		searchTeamCtrl.set(teamName);
		selectTeamCtrl.waitForVisible();
		selectTeamCtrl.click();
		setPrimaryCtrl.set("true");
		saveAndRunCtrl.click();

		VoodooUtils.focusDefault();
	}

	/**
	 * Verify report is shown in Dashlet and report module for team members
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21555_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1004
		VoodooControl addRowCtrl =  new VoodooControl("div", "css", "#content div div div div ul li.span4.layout_Home ul li:nth-child(4) div div div div");
		VoodooControl addDashletCtrl = new VoodooControl("div", "css", "#content div div div div ul li.layout_Home.span4 ul li:nth-child(4) div ul.dashlet-cell.rows.row-fluid.layout_Home li div div div div");
		VoodooControl spanSearch =  new VoodooControl("input", "css", ".span4.search");
		VoodooControl dashletReportCtrl = new VoodooControl("a", "css", ".single td:nth-of-type(1) a");
		VoodooControl reportDropdownCtrl = new VoodooControl("a", "css", "#drawers div div div.main-pane.span8 div div:nth-child(2) div div div:nth-child(1) div:nth-child(1) span span div a");
		VoodooControl searchResult =  new VoodooControl("input", "css",".select2-drop-active .select2-search input");
		VoodooControl saveReportCtrl =  new VoodooControl("a", "css", "div[data-voodoo-name='dashletconfiguration-headerpane'] a.btn.btn-primary");
		VoodooControl result = new VoodooControl("span", "css",".select2-results li div span");
		VoodooControl cancelDashboardCtrl = new VoodooControl("a", "css", ".fld_cancel_button.detail a");

		// Logout as sally
		sugar.logout();

		// Now Sarah logs in and in a Dashlet should be able to see the Team Report created by Sally as
		// both belong to "West" team
		sugar.login(sarah);
		sugar.navbar.navToModule(sugar.reports.moduleNameSingular);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "name", "name").set(reportName);
		new VoodooControl("input", "id", "search_form_submit_advanced").click();

		// Assert
		new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[3]/td[4]/b/span/a").assertExists(true);

		VoodooUtils.focusDefault();
		new VoodooControl("a", "css", ".logo").click(); // Goto Home
		sugar.alerts.waitForLoadingExpiration();

		// Create a Dashlet
		sugar.home.dashboard.edit();
		addRowCtrl.click();
		addDashletCtrl.click();
		spanSearch.waitForVisible();
		spanSearch.set("Saved Reports Chart Dashlet");
		VoodooUtils.waitForReady();

		dashletReportCtrl.waitForVisible();
		dashletReportCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		reportDropdownCtrl.waitForVisible();
		reportDropdownCtrl.click();

		// Expected result: User will see Team Report
		searchResult.set(reportName);

		// Assert
		result.assertContains(reportName, true);
		result.click();
		saveReportCtrl.click();
		cancelDashboardCtrl.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}