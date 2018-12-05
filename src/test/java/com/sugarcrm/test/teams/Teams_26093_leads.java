package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26093_leads extends SugarTest {
	FieldSet teamData = new FieldSet();
	VoodooControl leadsmoduleCtrl,studioCtrl;
	String qauser;

	public void setup() throws Exception {
		teamData = testData.get(testName).get(0);
		sugar().leads.api.create();
		sugar().login();

		// Studio -> Leads -> Layout -> Listview
		// TODO: VOOD-1507
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		leadsmoduleCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();
		leadsmoduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Drag Teams field from Hidden panel to Default panel 
		new VoodooControl("li", "css", "li[data-name='team_name']").dragNDrop(new VoodooControl("td", "id", "Default"));
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Edit Leads record, assign multiple teams.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.edit();

		// Add teams
		// TODO: VOOD-1397
		VoodooControl addTeamCtrl = new VoodooControl("button","css", "span.fld_team_name.edit button.btn.first");
		addTeamCtrl.click();
		VoodooUtils.waitForReady();
		qauser = sugar().users.getQAUser().get("userName");
		new VoodooSelect("div","css", ".fld_team_name.edit div:nth-child(2) div.select2-container.select2.inherit-width").set(qauser);
		addTeamCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooSelect("div","css", ".fld_team_name.edit div:nth-child(3) div.select2-container.select2.inherit-width").set(teamData.get("team2"));

		// Make QAUser team as primary team, Global team is by Default primary team but change it to QAUser
		new VoodooControl("i", "css", ".fld_team_name.edit div:nth-child(2) button[name='primary']").click();
		VoodooUtils.waitForReady();
		sugar().leads.recordView.save();
	}

	/**
	 * For Sidecar Module: Verify team in list view is the "primary" team in edit view.
	 * @throws Exception
	 */
	@Test
	public void Teams_26093_leads_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.recordView.showMore();

		// Verify that all teams display in detail view.
		// TODO: VOOD-1397
		VoodooControl teamsDetailViewCtrl = new VoodooControl("div","css", ".fld_team_name.detail");
		teamsDetailViewCtrl.assertContains(teamData.get("team1"), true);
		teamsDetailViewCtrl.assertContains(teamData.get("team2"), true);

		// Verify QAUser as a primary team in detail view
		teamsDetailViewCtrl.assertContains(qauser+ " " +teamData.get("primaryTeam") , true);

		// Navigate to Edit view.
		sugar().leads.recordView.edit();

		// Verify that all teams display in edit view.
		VoodooControl teamsEditViewCtrl = new VoodooControl("span", "css", ".fld_team_name.edit");
		teamsEditViewCtrl.assertContains(teamData.get("team1"), true);
		teamsEditViewCtrl.assertContains(qauser, true);
		teamsEditViewCtrl.assertContains(teamData.get("team2"), true);

		// Verify that team in list view is the "primary" team in edit view.
		sugar().leads.navToListView();
		sugar().leads.listView.verifyField(1,"relTeam", qauser);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}