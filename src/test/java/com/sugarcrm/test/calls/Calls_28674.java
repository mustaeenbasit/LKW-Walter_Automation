package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_28674 extends SugarTest {
	UserRecord qaUser, chris;

	public void setup() throws Exception {
		qaUser = new UserRecord(sugar.users.getQAUser());
		sugar.login();
		
		// Create Chris(Jim) User
		chris = (UserRecord)sugar.users.create();
		sugar.logout();
	}

	/**
	 * Verify that a long range Call/Meeting is created
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_28674_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAuser user
		qaUser.login();
		
		// Create a Call, starting date is 1980-05-01, ending date is 2020-01-01
		sugar.calls.navToListView();
		sugar.calls.listView.create();
		FieldSet callData = new FieldSet();
		callData = testData.get(testName).get(0);
		VoodooControl nameCtrl = sugar.calls.createDrawer.getEditField("name");
		nameCtrl.set(callData.get("name"));
		VoodooControl startDateCtrl = sugar.calls.createDrawer.getEditField("date_start_date");
		startDateCtrl.set(callData.get("date_start_date1"));
		VoodooControl endDateCtrl = sugar.calls.createDrawer.getEditField("date_end_date");
		endDateCtrl.set(callData.get("date_end_date1"));
		sugar.calls.createDrawer.save();

		// Verify that call is created
		sugar.calls.listView.getDetailField(1, "name").assertEquals(callData.get("name"), true);

		// Logout as QAuser and Login as Chris
		sugar.logout();
		chris.login();
		
		// Create a Call, starting date is 1980-05-01, ending date is 2020-01-01.
		sugar.calls.navToListView();
		sugar.calls.listView.create();
		nameCtrl.set(testName);
		
		// Select Sally(qauser) as an invitee.
		sugar.calls.createDrawer.clickAddInvitee();
		sugar.calls.createDrawer.selectInvitee(qaUser);
		startDateCtrl.set(callData.get("date_start_date2"));
		endDateCtrl.set(callData.get("date_end_date2"));
		sugar.calls.createDrawer.save();

		// Verify Call is created
		sugar.calls.listView.sortBy("headerName", false);
		sugar.calls.listView.getDetailField(1, "name").assertEquals(testName, true);

		// Create another call
		sugar.calls.listView.create();
		FieldSet fs = sugar.calls.getDefaultData();
		nameCtrl.set(fs.get("name"));
		sugar.calls.createDrawer.save();

		// Verify call is created
		sugar.calls.listView.sortBy("headerName", true);
		sugar.calls.listView.getDetailField(1, "name").assertEquals(fs.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}