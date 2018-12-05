package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24715 extends SugarTest {
	DataSource teamDS = new DataSource();
	
	public void setup() throws Exception {
		
		// Initialize data Source
		teamDS = testData.get(testName);
		String qaUserName = sugar().users.getQAUser().get("userName");
		
		// Login as admin
		sugar().login();
		
		// Create a record assigned to Admin's Pvt team "Administrator"
		// Create 3 records assigned to qauser user, qauser user's private team "qauser", normal team "West", "Global" team.
		// TODO: VOOD-444 Create records with related fields via api
		sugar().accounts.navToListView();
		for (int i = 0; i < teamDS.size(); i++) {
			sugar().accounts.listView.create();
			sugar().accounts.createDrawer.getEditField("name").set(teamDS.get(i).get("team") + "_" + testName);
			if (i != 0) // First record assigned to Admin, rest assigned to qauser
				sugar().accounts.createDrawer.getEditField("relAssignedTo").set(qaUserName);
			sugar().accounts.createDrawer.showMore();
			sugar().accounts.createDrawer.getEditField("relTeam").set(teamDS.get(i).get("team"));
			sugar().accounts.createDrawer.save();
		}
		
		// Add a non-admin user (qauser) to a team other than Global and their private team, i.e. "West"
		sugar().teams.navToListView(); 
		sugar().teams.listView.basicSearch(teamDS.get(3).get("team"));
		sugar().teams.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-518
		// Add qauser to team 'West'
		new VoodooControl("a", "id", "team_memberships_select_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "user_name_advanced").set(qaUserName);
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("input", "css", ".list.view tr:nth-child(3) td:nth-child(1) input").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		
		// Logout from admin user
		sugar().logout();
	}

	/**
	 * User-Admin_Verify that admin privilege user can access all records
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24715_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
	
		// Initialize data Source
		FieldSet userDS = testData.get(testName+"_user").get(0);
				
		// Login into the application with another admin user admin2		
		sugar().login(userDS);
		
		// Nav to Admin list view
		sugar().accounts.navToListView();
		
		// Verify all the records created are displayed for admin2 user.
		for (int i = 0; i < teamDS.size(); i++) 
			sugar().accounts.listView.verifyField(i+1, "name", teamDS.get(teamDS.size()-1-i).get("team") + "_" + testName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}