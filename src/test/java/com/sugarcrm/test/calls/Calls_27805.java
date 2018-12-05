package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_27805 extends SugarTest{
	UserRecord sally, will;
	DataSource dsTeams = new DataSource();
	DataSource dsUser = new DataSource();

	public void setup() throws Exception {
		dsTeams = testData.get(testName);
		dsUser = testData.get(testName+"_user");
		sugar.calls.api.create();

		// Login as admin
		sugar.login();

		// Create two user
		sally = (UserRecord)sugar.users.create(dsUser.get(0));
		will = (UserRecord)sugar.users.create(dsUser.get(1));
		VoodooUtils.focusDefault();

		// Assign Users to Teams
		for(int i = 0; i < dsTeams.size(); i++) {
			sugar.teams.navToListView();
			sugar.teams.listView.clickRecord(i+3);
			VoodooUtils.focusFrame("bwc-frame");

			// TODO: VOOD-518
			// Select Sally for team 'West' and 'Will' for team 'East'
			VoodooControl selectCtrl1=new VoodooControl("a", "id", "team_memberships_select_button");
			selectCtrl1.click();
			VoodooUtils.waitForReady();
			VoodooUtils.focusWindow(1);
			new VoodooControl("input", "id", "user_name_advanced").set(dsUser.get(i).get("userName"));
			new VoodooControl("input", "id", "search_form_submit").click();
			new VoodooControl("input", "css", ".list.view tr:nth-child(3) td:nth-child(1) input").click();
			new VoodooControl("input", "id", "MassUpdate_select_button").click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusDefault();
		}

		// Create Contact1 that has team West
		sugar.contacts.navToListView();
		sugar.contacts.listView.create();
		sugar.contacts.createDrawer.getEditField("lastName").set(sugar.contacts.getDefaultData().get("lastName"));
		sugar.contacts.createDrawer.showMore();
		sugar.contacts.createDrawer.getEditField("relTeam").set(dsTeams.get(0).get("name"));
		sugar.contacts.createDrawer.save();

		// Create Contact2 that has team East
		sugar.contacts.listView.create();
		sugar.contacts.createDrawer.getEditField("lastName").set(sugar.contacts.getDefaultData().get("lastName") + "_1");
		sugar.contacts.createDrawer.getEditField("relTeam").set(dsTeams.get(1).get("name"));
		sugar.contacts.createDrawer.save();
	}

	/**
	 * Verify that Contact or Lead can't be searched if they are not accessiable 
	 * @throws Exception
	 */
	@Test
	public void Calls_27805_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logout from Admin user and Log in as sally 
		sugar.logout();
		sugar.login(sally);

		// Go to Calls
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();

		// Click on + sign in Guests field
		sugar.calls.recordView.clickAddInvitee();

		// TODO: VOOD-692
		VoodooControl firstSearchResulrCtrl=new VoodooControl("ul", "css", "#select2-drop ul:nth-child(2) li:nth-child(1)");
		VoodooControl secondSearchResulrCtrl=new VoodooControl("ul", "css", "#select2-drop ul:nth-child(2) li:nth-child(2)");
		VoodooControl guestCtrl =  new VoodooControl("input", "css", "#select2-drop div input");

		// Search for contact
		guestCtrl.set(sugar.contacts.getDefaultData().get("lastName"));
		VoodooUtils.waitForReady();

		// Verify contact that has West team only appears
		firstSearchResulrCtrl.assertContains(sugar.contacts.getDefaultData().get("lastName"), true);

		// Verify contact that has East team doesn't appear	
		secondSearchResulrCtrl.assertExists(false);

		// Select the record
		firstSearchResulrCtrl.click();

		// Cancel the Calls record view
		sugar.calls.recordView.cancel();

		// Logout from Sally and Login as Will
		sugar.logout();
		sugar.login(will);

		// Go to Calls
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();

		// Click on + sign in Guests field
		sugar.calls.recordView.clickAddInvitee();

		// Search for contact 
		guestCtrl.set(sugar.contacts.getDefaultData().get("lastName"));
		VoodooUtils.waitForReady();

		// Verify contact that has East team only appears	
		firstSearchResulrCtrl.assertContains(sugar.contacts.getDefaultData().get("lastName")+"_1", true);
		// Verify contact that has West team doesn't appear		
		secondSearchResulrCtrl.assertExists(false);

		// Select the record
		firstSearchResulrCtrl.click();

		// Cancel the Calls record view
		sugar.calls.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}