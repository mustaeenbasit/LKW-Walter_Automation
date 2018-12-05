package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_26108 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar.login();
		
		myAccount = (AccountRecord) sugar.accounts.api.create();
		sugar.cases.api.create(); // First Rec
		sugar.cases.api.create(); // Second Rec
	}

	/**
	 * Merge Duplicates in detail view
	 * @throws Exception
	 */
	@Test
	public void Teams_26108_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet teamsName = testData.get(testName).get(0);

		// Go to Cases module, choose a record
		sugar.cases.navToListView();
		sugar.cases.listView.clickRecord(1);
		sugar.cases.recordView.showMore();
		sugar.cases.recordView.edit();
		sugar.cases.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());

		// Select team "qaUser" 
		// TODO: VOOD-1005, VOOD-518
		VoodooControl addTeamButton = new VoodooControl("button", "css", ".fld_team_name.edit button.btn.first");
		VoodooControl selectTeam = new VoodooControl("div", "css", "#select2-drop ul:nth-child(3) li div");
		VoodooControl teamSearchField = new VoodooControl("a", "css", ".fld_team_name.edit div:nth-child(2) a");
		addTeamButton.click();
		teamSearchField.click();
		selectTeam.click();
		new VoodooControl("input", "xpath", "//table[@class='table table-striped dataTable search-and-select']/tbody/tr[contains(.,'qauser')]/td[1]/span/input").click(); // choose team
		sugar.cases.recordView.save();

		sugar.alerts.waitForLoadingExpiration();
		
		// Go to Cases module again and select second record
		sugar.cases.navToListView();
		sugar.cases.listView.clickRecord(2);
		sugar.cases.recordView.showMore();
		sugar.cases.recordView.edit();
		sugar.cases.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());

		// Select team "Administrator" 
		// TODO: VOOD-1005, VOOD-518
		addTeamButton.click();
		teamSearchField.click();
		selectTeam.click();
		new VoodooControl("input", "xpath", "//table[@class='table table-striped dataTable search-and-select']/tbody/tr[contains(.,'Administrator')]/td[1]/span/input").click(); // choose team
		sugar.cases.recordView.save();

		sugar.alerts.waitForLoadingExpiration();
		
		// Go to Cases module, choose a record
		sugar.cases.navToListView();
		sugar.cases.listView.clickRecord(1);

		// Click "find duplicates",set merge condition,"next"
		sugar.cases.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-568 -library support for all of the actions in the RowActionDropdown
		new VoodooControl("a", "css", ".fld_find_duplicates_button a").click();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("input", "css", ".btn.checkall .toggle-all").click();

		// TODO: VOOD-681 -Create a Lib for Merge Duplicates
		new VoodooControl("a", "css", ".fld_merge_duplicates_button").click();
		sugar.alerts.cancelAllAlerts();
		new VoodooControl("input", "css", ".fld_team_name.edit > div:nth-child(3) > div > input").click();

		// Save merge 
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		sugar.alerts.confirmAllWarning();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that selected teams are display properly on detail page
		sugar.cases.navToListView();
		sugar.cases.listView.clickRecord(1);
		sugar.cases.recordView.showMore();

		// TODO: VOOD-1397
		VoodooControl teamsCtrl = new VoodooControl("div", "css", ".fld_team_name.detail");
		teamsCtrl.assertContains(sugar.users.getQAUser().get("userName"), true);
		teamsCtrl.assertContains(teamsName.get("administrator"), true);

		// Logout from admin and login as QAUser
		sugar.logout();
		sugar.login(sugar.users.getQAUser());

		// Verify that selected teams are display properly on detail page(for QAUser)
		sugar.cases.navToListView();
		sugar.cases.listView.clickRecord(1);
		sugar.alerts.waitForLoadingExpiration();
		sugar.cases.recordView.showMore();
		teamsCtrl.assertContains(sugar.users.getQAUser().get("userName"), true);
		teamsCtrl.assertContains(teamsName.get("administrator"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}