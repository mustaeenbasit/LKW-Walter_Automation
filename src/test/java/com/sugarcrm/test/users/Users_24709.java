package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24709 extends SugarTest {
	UserRecord customUser;
	
	public void setup() throws Exception {
		// Set unique name for customUser 
		FieldSet fs = new FieldSet();
		fs.put("userName", testName);
		fs.put("firstName", "");
		fs.put("lastName", testName);
		
		// Create new user. Note Private team with same name is also created 
		customUser = (UserRecord) sugar().users.api.create(fs);
		
		// Login
		sugar().login();
	}

	/**
	 * Verify that team memberships can be updated after changing a user's Reports To. 
	 * @throws Exception
	 */
	@Test
	public void Users_24709_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// 1. User A (customUser) reports to B (admin2User). Note: A belongs to team (customUser user pvt team)
		String userAName = testName; // customUser
		String userBName = testData.get(testName).get(0).get("userB");
		String userCName = sugar().users.getQAUser().get("userName");
		
		// Navigate to user record, edit, update "Reports to" to userAdmin2, save
		customUser.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		sugar().users.editView.getEditField("reportsTo").set(userBName);
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Verify customUser users pvt team now contains admin2User as a member
		// Navigate to team management
		// TODO: VOOD-2020 sugar().teams.navToListView(); Causing unhandled inspector on local only
		sugar().teams.navToListView();
				
		// Open customUser Private Team record 
		sugar().teams.listView.basicSearch(testName);
		sugar().teams.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify customUser users pvt team contains customUser, admin2User, not qaUser
		VoodooControl teamsUsersSubpanel = sugar().teams.detailView.subpanels.get(sugar().users.moduleNamePlural);
		teamsUsersSubpanel.assertContains(userAName, true);
		teamsUsersSubpanel.assertContains(userBName, true);
		teamsUsersSubpanel.assertContains(userCName, false);
		VoodooUtils.focusDefault();
		
		// 2. User A (customUser) reports to C (qaUser) 
		// Navigate to user record, edit, update "Reports to" to qaUser, save
		customUser.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getEditField("reportsTo").set(userCName);
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Verify customUser users pvt team now contains qaUser as a member and admin2User is removed
		// Navigate to team management
		// TODO: VOOD-2020 sugar().teams.navToListView(); Causing unhandled inspector on local only
		sugar().teams.navToListView();
		
		// Open customUser Private Team record 
		sugar().teams.listView.basicSearch(testName);
		sugar().teams.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify customUser users pvt team contains customUser, qaUser as a member and admin2User is removed
		teamsUsersSubpanel.assertContains(userAName, true);
		teamsUsersSubpanel.assertContains(userBName, false);
		teamsUsersSubpanel.assertContains(userCName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}