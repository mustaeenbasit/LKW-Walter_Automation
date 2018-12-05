package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_30198 extends SugarTest {
	String teamName = "";

	public void setup() throws Exception {
		FieldSet customFS = testData.get(testName).get(0);
		teamName = customFS.get("teamName");
		sugar().accounts.api.create();
		sugar().login();

		// Navigate to User Management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("teamsManagement").click();

		// Search the West team
		VoodooUtils.focusDefault();
		sugar().teams.listView.basicSearch(teamName);
		sugar().teams.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-518
		// Assign West team to QAuser
		new VoodooControl("a", "id", "team_memberships_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", ".list.view tr:nth-child(4) td:nth-child(1) input").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Logout as admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Set up the user Max's default team as West in his user profile, make sure Global has been removed and West is the Primary team
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();

		// TODO VOOD-1040 & VOOD-776
		new VoodooControl("button", "css", "#EditView_team_name_table .id-ff-remove").click();
		new VoodooControl("input", "css", "input[title='Team Selected ']").set(teamName);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#EditView_team_name_table ul li:nth-of-type(1)").click();
		new VoodooControl("input", "css", "input[title='Select to make this team primary']").click();
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
	}

	/**
	 * Verify that default team is correct for RLI when create it from Opportunity create view
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30198_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity and Create an Opp + RLI and click Show More to see that Opps Team is assigned to newWest only
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().opportunities.createDrawer.showMore();

		// Verify that Team is assigned to West only
		sugar().opportunities.createDrawer.getEditField("relTeam").assertContains(teamName, true);
		sugar().opportunities.createDrawer.save();

		// Go to Opportunity recordView
		sugar().opportunities.listView.clickRecord(1);

		// Verify that the created Opportunity should only show West as the team assigned to the Opportunity
		sugar().opportunities.recordView.getDetailField("relTeam").assertContains(teamName, true);

		// View the created RLI
		StandardSubpanel rliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubPanel.expandSubpanel();
		rliSubPanel.clickRecord(1);

		// Verify that the RLI's Team is set to West only
		sugar().revLineItems.recordView.showMore();
		sugar().revLineItems.recordView.getDetailField("relTeam").assertContains(teamName, true);

		// Go back to Opportunity recordView
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Click "+" to create another RLI from RLI subpanel of Opportunity record view 
		rliSubPanel.addRecord();

		// Verify that the RLI's Team in RLI create drawer is set to West
		sugar().revLineItems.createDrawer.showMore();
		sugar().revLineItems.createDrawer.getEditField("relTeam").assertContains(teamName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}