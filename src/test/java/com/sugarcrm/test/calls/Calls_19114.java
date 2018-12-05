package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_19114 extends SugarTest {
	UserRecord myUser;
	String contactName;

	public void setup() throws Exception {
		sugar().calls.api.create();

		// Login as an admin user
		sugar().login();

		// Create a custom user
		myUser = (UserRecord) sugar().users.create();

		// Logout from Admin user and Login as custom user
		sugar().logout();
		sugar().login(myUser);

		// Create an Account record with teams has global and custom user's private team
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("name").set(testName + "_" + sugar().accounts.moduleNameSingular);
		// TODO: VOOD-518
		VoodooControl addTeamCtrl = new VoodooControl("button", "css", ".fld_team_name button[name='add']");
		addTeamCtrl.click();
		sugar().accounts.createDrawer.getEditField("relTeam").set(myUser.getRecordIdentifier());
		sugar().accounts.createDrawer.save();

		// Create an Contact record with teams has global and custom user's private team
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.showMore();
		contactName = testName + "_" + sugar().contacts.moduleNameSingular;
		sugar().contacts.createDrawer.getEditField("lastName").set(contactName);
		addTeamCtrl.click();
		sugar().contacts.createDrawer.getEditField("relTeam").set(myUser.getRecordIdentifier());
		sugar().contacts.createDrawer.save();
	}

	/**
	 * Verify that user is able to view and sort calls records in Calls module without any errors
	 * @throws Exception
	 */
	@Test
	public void Calls_19114_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Go to Accounts module, and open account record created in its record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click on Create button under Calls subpanel
		StandardSubpanel callsSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanelCtrl.addRecord();

		// Add created Contact record (from setup) as an invitee
		sugar().calls.createDrawer.clickAddInvitee();
		sugar().calls.createDrawer.selectInvitee(contactName);

		// Fill all other required information and Save the call record
		String callName = testName + "_" + sugar().calls.moduleNamePlural;
		sugar().calls.createDrawer.getEditField("name").set(callName);
		sugar().calls.createDrawer.save();

		// Logout from the custom user and login as Admin user
		sugar().logout();
		sugar().login();

		// As the Admin user, set custom user to be inactive
		myUser.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-563
		new VoodooControl("select", "id", "status").set(customFS.get("status"));
		sugar().users.editView.getControl("saveButton").click();

		// Click No when asked if you want to reassign Sarah's records
		new VoodooControl("button", "id", "yui-gen1-button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Logout from and Login as another non admin user, such as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to the Calls module
		sugar().calls.navToListView();

		// Controls for call records
		VoodooControl firstCall = sugar().calls.listView.getDetailField(1, "name");
		VoodooControl secondCall = sugar().calls.listView.getDetailField(2, "name");

		// Verify that the User is able to see calls listed in the call module
		firstCall.assertEquals(callName, true);
		sugar().calls.listView.getDetailField(1, "assignedTo").assertEquals(myUser.get("fullName"), true);
		secondCall.assertEquals(sugar().calls.getDefaultData().get("name"), true);

		// Sort the call list view
		sugar().calls.listView.sortBy("headerName", false);

		// Verify that the call records are sorted accordingly
		firstCall.assertEquals(callName, true);
		secondCall.assertEquals(sugar().calls.getDefaultData().get("name"), true);

		// Sort the call list view
		sugar().calls.listView.sortBy("headerName", true);

		// Verify that the call records are sorted accordingly
		secondCall.assertEquals(callName, true);
		firstCall.assertEquals(sugar().calls.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}