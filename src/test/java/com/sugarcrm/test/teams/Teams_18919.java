package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_18919 extends SugarTest {
	public void setup() throws Exception {
		sugar.accounts.api.create();

		// Create a normal user
		UserRecord user1 = (UserRecord) sugar.users.api.create();
		sugar.login();

		// After creation of new normal user, delete user
		user1.delete();

		// clear search field
		sugar.users.listView.clearSearchForm();
		sugar.users.listView.submitSearchForm();

		// Go to created account
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.edit();
		sugar.accounts.recordView.showMore();

		// TODO: VOOD-518
		// Add private team for this deleted user to at least one record(Account)
		new VoodooControl("button","css", "span.fld_team_name.edit button.btn.first").click();
		new VoodooSelect("div","css", "span.fld_team_name div.control-group:nth-of-type(2) .select2-choice").set(sugar.users.getDefaultData().get("firstName"));
		sugar.accounts.recordView.save();
	}

	/**
	 * Delete private team from detail view with the corresponding user was deleted
	 * @throws Exception
	 */
	@Test
	public void Teams_18919_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet assertData = testData.get(testName).get(0);

		// Go to Admin -> Team Management
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("teamsManagement").click();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1011, VOOD-518
		// Click on a private team link, that the corresponding user was deleted
		VoodooControl teamNameCtrl = new VoodooControl("input", "id", "name_basic");
		VoodooControl searchBtnCtrl = new VoodooControl("input", "id", "search_form_submit");
		VoodooControl assignedToTeam = new VoodooControl("input", "id", "team_name");
		VoodooControl reassignBtn = new VoodooControl("input", "css", "#reassign_team [title='Reassign']");
		VoodooControl selectSearchTeam = new VoodooControl("li", "css", "div#reassign_team_team_name_results ul li:nth-of-type(1)");

		// Search the private team
		teamNameCtrl.set(sugar.users.getDefaultData().get("firstName"));
		searchBtnCtrl.click();	

		// Click on the team name and it navigates to detail view of the selected private team
		new VoodooControl("a", "css", "form#MassUpdate tbody tr:nth-of-type(3) td:nth-of-type(4) a").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Click the delete button on team's detail view page
		new VoodooControl("span", "css", "ul#team_action_menu span.ab").click();
		new VoodooControl("a", "css", "#delete_button").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Before delete this team records assigned to the another team
		assignedToTeam.set(sugar.users.getQAUser().get("userName"));
		selectSearchTeam.waitForVisible(); // Required for complete select team action
		selectSearchTeam.click();
		reassignBtn.click();
		VoodooUtils.acceptDialog();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the team is deleted
		new VoodooControl("div", "css", "div.list.view.listViewEmpty").assertElementContains(assertData.get("assert1") + " \"" +sugar.users.getDefaultData().get("firstName")+ "\"", true);

		// Clear Search field
		teamNameCtrl.set("");
		searchBtnCtrl.click();
		VoodooUtils.focusDefault();

		// Verify that the team is no more displays in records
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		VoodooControl teamCtrl = new VoodooControl("div","css", "span.fld_team_name");
		teamCtrl.assertContains(sugar.users.getDefaultData().get("firstName"), false);

		// Verify that the team is qauser instead of deleted team, as user already assigned the record to qauser.
		teamCtrl.assertContains(sugar.users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}