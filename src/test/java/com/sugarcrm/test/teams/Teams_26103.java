package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_26103 extends SugarTest {
	DataSource teamName;
	AccountRecord myAccount;

	public void setup() throws Exception {
		teamName = testData.get(testName);
		myAccount = (AccountRecord) sugar.accounts.api.create();
		sugar.opportunities.api.create();			
		sugar.login();

		// Create two teams (Always create teams(s) with common string in it, so that they can be deleted in one move)
		sugar.teams.create(teamName);
	}

	/**
	 * Verify that all team can be selected to "team" field
	 * @throws Exception
	 */
	@Test
	public void Teams_26103_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Go to Opportunity module and select a record to edit 
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.clickRecord(1);
		sugar.opportunities.recordView.showMore();
		sugar.opportunities.recordView.edit();

		// choose account
		sugar.opportunities.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());

		for(int i = 0; i< teamName.size(); i++) {
			// Add two teams
			// TODO: VOOD-1005, VOOD-518
			new VoodooControl("button", "css", ".fld_team_name.edit button.btn.first").click();
			new VoodooControl("a", "css", ".fld_team_name.edit div:nth-child("+(i+2)+") a").click();
			new VoodooControl("div", "css", "#select2-drop ul:nth-child(3) li div").click();

			// Search team from select drawer
			// TODO: VOOD-1162
			new VoodooControl("input", "css", ".layout_Teams.drawer.active [data-voodoo-name='filter-quicksearch'] input").set(teamName.get(i).get("name"));
			sugar.alerts.waitForLoadingExpiration();
			new VoodooControl("input", "css", ".layout_Teams.drawer.active div[data-voodoo-name='selection-list'] tbody tr:nth-of-type(1) input").click();
		}

		// Save the Opportunity record
		sugar.opportunities.recordView.save();

		// Verify that selected teams are display properly on detail page
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.clickRecord(1);
		sugar.opportunities.recordView.showMore();

		// TODO: VOOD-1397
		VoodooControl teamsCtrl = new VoodooControl("div", "css", ".fld_team_name.detail");
		teamsCtrl.assertContains(teamName.get(0).get("name"), true);
		teamsCtrl.assertContains(teamName.get(1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}