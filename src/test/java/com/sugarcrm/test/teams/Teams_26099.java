package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_26099 extends SugarTest {
	public void setup() throws Exception {
		sugar.contacts.api.create();
		sugar.login();		
	}

	/**
	 * Delete record in list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26099_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to contact record 
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		sugar.contacts.recordView.showMore();
		
		// Edit team to Administrator
		sugar.contacts.recordView.edit();
		sugar.contacts.recordView.getEditField("relTeam").set("Administrator");
		
		// Add qauser to team.
		// TODO: VOOD-1005
		new VoodooControl("button", "css", ".fld_team_name.edit button.btn.first").click();
		VoodooSelect teamNameCtrl = new VoodooSelect("a", "css", ".fld_team_name.edit div:nth-child(2) a");
		teamNameCtrl.scrollIntoView();
		teamNameCtrl.click();
		teamNameCtrl.clickSearchForMore();
		UserRecord qauser = new UserRecord(sugar.users.getQAUser());
		sugar.teams.searchSelect.selectRecord(qauser);
		sugar.contacts.recordView.save();

		// Delete the record.
		sugar.contacts.navToListView();
		sugar.contacts.listView.deleteRecord(1);
		sugar.contacts.listView.confirmDelete();
		VoodooUtils.waitForReady();
		
		// Verify that record is deleted and each user in "team" field((Admin & qauser) can't see the record
		sugar.contacts.listView.assertIsEmpty();
		sugar.logout();

		// Now login as qauser (non admin user).
		qauser.login();
		sugar.contacts.navToListView();

		// Verify that record is deleted and qauser (user in "team" field) can't see the record.
		sugar.contacts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}