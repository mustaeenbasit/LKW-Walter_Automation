package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_26106 extends SugarTest {
	public void setup() throws Exception {		
		sugar.targetlists.api.create(); 
		sugar.login();
	}

	/**
	 * Verify that the record is deleted and each user in "team" field can't see the record
	 * @throws Exception
	 */
	@Test
	public void Teams_26106_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Go to Target List and edit a record
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);
		sugar.targetlists.recordView.showMore();
		sugar.targetlists.recordView.edit();
		sugar.targetlists.recordView.getEditField("teams").set("Administrator");

		// Add team QAUser
		// TODO: VOOD-1005
		new VoodooControl("button", "css", ".fld_team_name.edit button.btn.first").click();
		VoodooSelect teamNameCtrl = new VoodooSelect("a", "css", ".fld_team_name.edit div:nth-child(2) a");
		teamNameCtrl.scrollIntoView();
		teamNameCtrl.click();
		teamNameCtrl.clickSearchForMore();
		UserRecord qauser = new UserRecord(sugar.users.getQAUser());
		sugar.teams.searchSelect.selectRecord(qauser);	
		sugar.targetlists.recordView.save();

		// Go to Target List again
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);
		sugar.targetlists.recordView.showMore();

		// Verify that selected team QAUser display properly on detail page
		// TODO: VOOD-1005
		new VoodooControl("div", "css", ".fld_team_name.detail div:nth-of-type(2)").assertEquals(qauser.get("userName"), true);

		// In detail view, delete the target list record
		sugar.targetlists.recordView.delete();
		sugar.targetlists.listView.confirmDelete();
		VoodooUtils.waitForReady();

		// Verify that deleted targetList not display to Admin user
		sugar.targetlists.listView.assertIsEmpty();	

		// logout from the admin user and Login as QAUser
		sugar.logout();
		qauser.login();

		// Verify that deleted targetList is not displaying to QAUser(team:QAUser)
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.assertIsEmpty();		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}