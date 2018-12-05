package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

public class Users_24730 extends SugarTest {
	UserRecord user1, user2;
	DataSource myUsers, customData;
	FieldSet multiPurposeFS;

	public void setup() throws Exception {
		myUsers = testData.get(testName);
		customData = testData.get(testName +"_1");
		sugar.contacts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		multiPurposeFS = new FieldSet();

		// Create user1 and user2 
		user1 = (UserRecord) sugar.users.create(myUsers.get(0));
		user2 = (UserRecord) sugar.users.create(myUsers.get(1));

		// Create team1 and team2
		multiPurposeFS.put("name", customData.get(0).get("name"));
		sugar.teams.create(multiPurposeFS);
		multiPurposeFS.clear();
		multiPurposeFS.put("name", customData.get(1).get("name"));
		sugar.teams.create(multiPurposeFS);
		multiPurposeFS.clear();

		BWCSubpanel userSubpanel = sugar.teams.detailView.subpanels.get(sugar.users.moduleNamePlural);

		for (int i = 1; i <= 2; i++){
			// Go to Team Management 
			sugar.admin.navToAdminPanelLink("teamsManagement");

			// TODO: VOOD-776
			// Assign user1 in team1; assign user2 in team2.
			sugar.teams.listView.clickRecord(i);
			VoodooUtils.focusFrame("bwc-frame");
			userSubpanel.getControl("teamMembership").click();
			VoodooUtils.focusWindow(1);
			VoodooUtils.focusDefault();
			if (i == 1)
				new VoodooControl("a", "css", "tr.oddListRowS1 td:nth-of-type(3) a").click();
			else
				new VoodooControl("a", "css", "tr.evenListRowS1 td:nth-of-type(3) a").click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusDefault();
		}
	}

	/**
	 * Verify that reasssign record functionality works properly
	 * @throws Exception
	 */
	@Test
	public void Users_24730_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// create Records
		// create Account Record
		multiPurposeFS.put("relAssignedTo", user1.getRecordIdentifier());
		multiPurposeFS.put("relTeam", customData.get(0).get("name"));
		multiPurposeFS.put("type", customData.get(3).get("name"));
		sugar.accounts.create(multiPurposeFS);
		multiPurposeFS.clear();

		// create Bug
		multiPurposeFS.put("relAssignedTo", user1.getRecordIdentifier());
		multiPurposeFS.put("relTeam", customData.get(0).get("name"));
		sugar.bugs.create(multiPurposeFS);
		multiPurposeFS.clear();

		// Goto User management
		sugar.admin.navToAdminPanelLink("userManagement");

		// Delete user1
		user1.navToRecord();
		sugar.users.detailView.delete();
		new VoodooControl("button", "css", ".first-child button").click();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Reassign records by different scenarios
		// First scenario: From user 1, to user2, set team to user2's team, Accounts module, with filter set="Analyst".
		// TODO: VOOD-1023
		VoodooControl fromUserCtrl = new VoodooControl("select", "css", "#fromuser");
		fromUserCtrl.click();
		fromUserCtrl.set(myUsers.get(0).get("fullName"));
		VoodooControl toUserCtrl = new VoodooControl("select", "id", "touser");
		toUserCtrl.click();
		toUserCtrl.set(myUsers.get(1).get("fullName"));	
		VoodooControl removeCtrl = new VoodooControl("button", "id", "remove_team_name_collection_0");
		removeCtrl.click();
		VoodooControl primaryCtrl = new VoodooControl("input", "id", "primary_team_name_collection_0");
		primaryCtrl.set(customData.get(5).get("check_box_val"));
		VoodooControl teamSelectCtrl = new VoodooControl("button", "id", "teamSelect");
		teamSelectCtrl.click();
		VoodooUtils.focusWindow(1);
		VoodooControl teamInputCtrl = new VoodooControl("input", "id", "team_name_input");
		teamInputCtrl.set(customData.get(1).get("name"));;
		VoodooControl submitSearchCtrl = new VoodooControl("input", "id", "search_form_submit");
		submitSearchCtrl.click();
		VoodooControl toggleAllCtrl = new VoodooControl("input", "id", "massall");
		toggleAllCtrl.click();
		VoodooControl searchCtrl = new VoodooControl("input", "id", "search_form_select");
		searchCtrl.click();
		
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		new VoodooControl("input", "xpath", "//select[@id='modulemultiselect']/option[text()='Accounts']").click();
		new VoodooControl("input", "css", "#reassign_Accounts select option:nth-child(2)").click();
		VoodooControl firstStepCtrl = new VoodooControl("input", "css", ".button[name='steponesubmit']");
		firstStepCtrl.click();
		VoodooControl successMsgCtrl = new VoodooControl("td", "css", "#contentTable");
		successMsgCtrl.assertContains(customData.get(6).get("success_msg"), true);
		VoodooControl submitCtrl = new VoodooControl("input", "css", ".button[type='submit']");
		submitCtrl.click();
		VoodooControl tableCtrl = new VoodooControl("input", "css", "#contentTable .button");
		tableCtrl.click();

		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Second scenario: From user 1 to Administrator, set team to Global, Bug tracker module with bug status set.
		fromUserCtrl.click();
		fromUserCtrl.set(myUsers.get(0).get("fullName"));
		toUserCtrl.click();
		toUserCtrl.set(customData.get(4).get("name"));

		// Remove team and select another team
		removeCtrl.click();
		primaryCtrl.set(customData.get(5).get("check_box_val"));
		teamSelectCtrl.click();
		VoodooUtils.focusWindow(1);
		teamInputCtrl.set(customData.get(2).get("name"));
		submitSearchCtrl.click();
		toggleAllCtrl.click();
		searchCtrl.click();
		
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		new VoodooControl("body", "css", "body").click();
		new VoodooControl("option", "xpath", "//select[@id='modulemultiselect']/option[text()='Bugs']").click();
		new VoodooControl("option", "css", "#reassign_Bugs option:nth-child(1)").click();
		firstStepCtrl.click();
		successMsgCtrl.assertContains(customData.get(7).get("success_msg"), true);
		submitCtrl.click();
		tableCtrl.click();

		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Third scenario :From admin to user 2, set team to team2, Contacts.
		fromUserCtrl.click();
		fromUserCtrl.set(customData.get(4).get("name"));
		toUserCtrl.click();
		toUserCtrl.set(myUsers.get(1).get("fullName"));

		// Remove team and select team2
		removeCtrl.click();
		primaryCtrl.set(customData.get(5).get("check_box_val"));
		teamSelectCtrl.click();
		VoodooUtils.focusWindow(1);
		teamInputCtrl.set(customData.get(1).get("name"));
		submitSearchCtrl.click();
		toggleAllCtrl.click();
		searchCtrl.click();
		
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		new VoodooControl("body", "css", "body").click();
		// Deselect Accounts, Bugs
		new VoodooControl("option", "xpath", "//select[@id='modulemultiselect']/option[text()='Accounts']").click();
		new VoodooControl("option", "xpath", "//select[@id='modulemultiselect']/option[text()='Bugs']").click();
		// Select Contacts
		new VoodooControl("option", "xpath", "//select[@id='modulemultiselect']/option[text()='Contacts']").click();
		firstStepCtrl.click();
		successMsgCtrl.assertContains(customData.get(8).get("success_msg"), true);
		submitCtrl.click();
		tableCtrl.click();

		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Fourth scenario : :From admin to user 2, set team to team2, Reassigning to multiple modules BUT cancelled
		fromUserCtrl.click();
		fromUserCtrl.set(customData.get(4).get("name"));
		toUserCtrl.click();
		toUserCtrl.set(myUsers.get(1).get("fullName"));

		// Remove team and select team2
		removeCtrl.click();
		primaryCtrl.set(customData.get(5).get("check_box_val"));
		teamSelectCtrl.click();
		VoodooUtils.focusWindow(1);
		teamInputCtrl.set(customData.get(1).get("name"));
		submitSearchCtrl.click();
		toggleAllCtrl.click();
		searchCtrl.click();
		
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("body", "css", "body").click();
		// Deselect Contacts
		new VoodooControl("option", "xpath", "//select[@id='modulemultiselect']/option[text()='Contacts']").click();
		// Select Campaigns, Contracts, Documents
		new VoodooControl("option", "xpath", "//select[@id='modulemultiselect']/option[text()='Campaigns']").click();
		new VoodooControl("option", "xpath", "//select[@id='modulemultiselect']/option[text()='Contracts']").click();
		new VoodooControl("option", "xpath", "//select[@id='modulemultiselect']/option[text()='Documents']").click();
		submitCtrl.click();
		tableCtrl.click();

		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("button", "css", ".button[value='Cancel']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigating to Accounts module to verify the Changes
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.showMore();
		sugar.accounts.recordView.getDetailField("relAssignedTo").assertEquals(myUsers.get(1).get("fullName"), true);
		sugar.accounts.recordView.getDetailField("relTeam").assertContains(customData.get(1).get("name"), true);

		// Navigating to Contacts Module to verify the changes
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		sugar.contacts.recordView.showMore();
		sugar.contacts.recordView.getDetailField("relAssignedTo").assertEquals(myUsers.get(1).get("fullName"), true);

		// TODO: VOOD-1397
		new VoodooControl("span", "css", ".fld_team_name.detail").assertContains(customData.get(1).get("name"), true);

		// Navigating to Bugs module to Verify the changes
		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);
		sugar.bugs.recordView.showMore();
		sugar.bugs.recordView.getDetailField("relAssignedTo").assertEquals(customData.get(4).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}