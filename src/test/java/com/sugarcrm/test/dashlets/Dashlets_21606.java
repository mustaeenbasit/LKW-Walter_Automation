package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21606 extends SugarTest{
	FieldSet userData, customField;
	UserRecord userRecord;
	DataSource teamSet,reportsDS;
	VoodooControl reportModuleCtrl;

	public void setup() throws Exception {
		teamSet = testData.get(testName+"_teams");
		reportsDS = testData.get(testName+"_reports");
		userData = testData.get(testName+"_user").get(0);
		customField = testData.get(testName).get(0);
		sugar.login();

		// Create user1
		userRecord = (UserRecord)sugar.users.create(userData);

		// TODO: VOOD-518
		// Create team and save
		VoodooControl teamCtrl = new VoodooControl("button", "css", ".dropdown.active .btn-group .dropdown-toggle");
		VoodooControl createTeamCtrl = new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll ul li:nth-of-type(1)");

		for (int i = 0; i < teamSet.size(); i++) {
			sugar.navbar.navToAdminTools();
			VoodooUtils.focusFrame("bwc-frame");
			sugar.admin.adminTools.getControl("teamsManagement").click();
			teamCtrl.waitForVisible();
			teamCtrl.click();
			createTeamCtrl.waitForVisible();
			createTeamCtrl.click();
			VoodooUtils.focusFrame("bwc-frame");

			// TODO: VOOD-518
			new VoodooControl("input", "css", "table.edit input").set(teamSet.get(i).get("teamName"));
			new VoodooControl("input", "id", "btn_save").click();
			VoodooUtils.waitForAlertExpiration();
			if(i==0){
				// TODO: VOOD-518
				// Select user1 to team1
				new VoodooControl("a", "css", "#team_memberships_select_button").click();
				// TODO: VOOD-1109 - VoodooUtils.focusWindow("SugarCRM") not working.
				VoodooUtils.focusWindow(1);
				new VoodooControl("input", "css", ".list.view tr:nth-child(3) td:nth-child(1) input").click();
				new VoodooControl("input", "css", "#MassUpdate_select_button").click();
				VoodooUtils.focusWindow(0);
			}
		}
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify Save Reports Chart dashlet show the team report that the user belongs to
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21606_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logout Admin and login as user1
		sugar.logout();
		sugar.login(userRecord);
		sugar.navbar.navToModule(customField.get("report_module_plural_name"));

		// TODO: VOOD-822
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		VoodooControl createReportCtrl = new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li:nth-of-type(1)");
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(3) table tbody tr:nth-child(1) td:nth-child(1)");
		VoodooControl accountsModuleCtrl = new VoodooControl("table", "id", "Accounts");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl fieldNameCtrl = new VoodooControl("tr", "id", "Accounts_count");
		VoodooControl nxtbtnCtrl = new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl searchTeamCtrl = new VoodooControl("input", "css", "#ReportsWizardForm_team_name_table tbody tr:nth-child(2) td:nth-child(1) span input.sqsEnabled.yui-ac-input");
		VoodooControl assignedToCtrl = new VoodooControl("input", "id", "assigned_user_name");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");

		// Create multiple reports
		for (int i = 0; i < reportsDS.size(); i++) {
			reportModuleCtrl.click();
			createReportCtrl.click();
			VoodooUtils.focusFrame("bwc-frame");
			createSummationReportCtrl.click();
			accountsModuleCtrl.click();
			nextBtnCtrl.click();
			nextBtnCtrl.click();
			fieldNameCtrl.click();
			nextBtnCtrl.click();

			if(i==0){
				new VoodooControl("tr", "id", "Accounts_account_type").click();
				nextBtnCtrl.click();
				new VoodooControl("option", "css", "#chart_type option:nth-of-type(2)").click();
			}else if(i==1){
				new VoodooControl("tr", "id", "Accounts_industry").click();
				nextBtnCtrl.click();
				new VoodooControl("option", "css", "#chart_type option:nth-of-type(3)").click();
			}else if(i==2){
				new VoodooControl("tr", "id", "Accounts_website").click();
				nextBtnCtrl.click();
				new VoodooControl("option", "css", "#chart_type option:nth-of-type(4)").click();
			}
			nxtbtnCtrl.click();
			reportNameCtrl.set(reportsDS.get(i).get("name"));
			if(i==0){
				// Report1: Assignee=>user1, Team=>Team1
				searchTeamCtrl.set(teamSet.get(0).get("teamName"));
			}else if(i==1){
				// Report2: Assignee=>Admin, Team=>Team2
				assignedToCtrl.set(customField.get("assigned_to"));
				searchTeamCtrl.set(teamSet.get(1).get("teamName"));
			}else if(i == 2){
				// Report3: Assignee=>Admin, Team=>Global
				assignedToCtrl.set(customField.get("assigned_to"));
			}
			VoodooUtils.pause(2000); // pause required for auto search to be completed
			saveAndRunCtrl.click();
			sugar.alerts.waitForLoadingExpiration();
			VoodooUtils.focusDefault();
		}

		// TODO: VOOD-592
		sugar.navbar.navToModule("Home");
		sugar.home.dashboard.edit();

		// TODO: VOOD-1004
		VoodooControl addRowCtrl =  new VoodooControl("div", "css", "#content div div div div ul li.span4.layout_Home ul li:nth-child(4) div div div div");
		addRowCtrl.click();
		VoodooControl addDashletCtrl = new VoodooControl("div", "css", "#content div div div div ul li.layout_Home.span4 ul li:nth-child(4) div ul.dashlet-cell.rows.row-fluid.layout_Home li div div div div");
		addDashletCtrl.click();
		VoodooControl spanSearch =  new VoodooControl("input", "css", ".span4.search");
		spanSearch.waitForVisible();
		spanSearch.set(customField.get("reports_dashlet"));
		VoodooControl dashletReportCtrl = new VoodooControl("a", "css", ".single td:nth-of-type(1) a");
		dashletReportCtrl.waitForVisible();
		dashletReportCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooControl reportDropdownCtrl = new VoodooControl("a", "css", "#drawers div div div.main-pane.span8 div div:nth-child(2) div div div:nth-child(1) div:nth-child(1) span span div a");
		reportDropdownCtrl.waitForVisible();
		reportDropdownCtrl.click();

		// TODO: VOOD-1004
		// Expected result: User will see Report1, Report3
		VoodooControl searchResult =  new VoodooControl("input", "css",".select2-drop-active .select2-search input");
		searchResult.set(reportsDS.get(0).get("name"));
		VoodooControl result = new VoodooControl("span", "css",".select2-results li div span");
		result.assertContains(reportsDS.get(0).get("name"), true);
		searchResult.set(reportsDS.get(1).get("name"));
		new VoodooControl("li", "css",".select2-results li").assertContains(customField.get("no_match_found"), true);
		searchResult.set(reportsDS.get(2).get("name"));
		result.assertContains(reportsDS.get(2).get("name"), true);
		result.click(); // critical important to execute below code (i.e always overlapping issue occurs)

		VoodooControl cancelReportCtrl =  new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_cancel_button a");
		cancelReportCtrl.click();
		VoodooControl cancelDashboardCtrl = new VoodooControl("a", "css", ".fld_cancel_button.detail a");
		cancelDashboardCtrl.click();
		// sugar.home.dashboard.cancel(); // Not working
		sugar.logout();

		// Login as admin
		sugar.login();
		sugar.home.dashboard.waitForVisible();
		sugar.home.dashboard.click();
		sugar.home.dashboard.edit();
		addRowCtrl.click();
		addDashletCtrl.click();
		spanSearch.waitForVisible();
		spanSearch.set(customField.get("reports_dashlet"));
		dashletReportCtrl.waitForVisible();
		dashletReportCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		reportDropdownCtrl.click();

		// Expected result: Admin will see all 3 reports
		searchResult.set(reportsDS.get(0).get("name"));
		result.assertContains(reportsDS.get(0).get("name"), true);
		searchResult.set(reportsDS.get(1).get("name"));
		result.assertContains(reportsDS.get(1).get("name"), true);
		searchResult.set(reportsDS.get(2).get("name"));
		result.assertContains(reportsDS.get(2).get("name"), true);
		result.click();
		cancelReportCtrl.click();
		cancelDashboardCtrl.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}