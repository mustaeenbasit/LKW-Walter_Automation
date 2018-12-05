package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooDate;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_28674 extends SugarTest {
	UserRecord qaUser, Jim;
	FieldSet meetingData = new FieldSet();

	public void setup() throws Exception {
		qaUser = new UserRecord(sugar().users.getQAUser());
		meetingData = testData.get(testName).get(0);
		sugar().login();
		// Create Jim User
		Jim = (UserRecord)sugar().users.create();
		sugar().logout();
	}

	/**
	 * Verify that a long range Meeting is created
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_28674_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as non-admin user
		qaUser.login();
		// Create a meeting, starting date is 1980-05-01, ending date is 2016-01-01
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		VoodooControl nameCtrl = sugar().meetings.createDrawer.getEditField("name");
		nameCtrl.set(meetingData.get("name"));

		VoodooDate startDateCtrl = new VoodooDate("input", "css", ".fld_date_start.edit input[data-type='date']");
		startDateCtrl.set(meetingData.get("date_start_date1"));
		VoodooDate endDateCtrl = new VoodooDate("input", "css", ".fld_date_end.edit input[data-type='date']");
		endDateCtrl.set(meetingData.get("date_end_date1"));
		sugar().meetings.createDrawer.save();

		// Verify that meeting is created
		sugar().meetings.listView.getDetailField(1, "name").assertEquals(meetingData.get("name"), true);

		sugar().logout();
		// Login as Jim
		Jim.login();
		// Create a meeting, starting date is 1980-05-01, ending date is 2016-01-01.
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		nameCtrl.set(testName);
		// Select Sally(qauser) as an invitee.
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(qaUser);
		startDateCtrl.set(meetingData.get("date_start_date2"));
		endDateCtrl.set(meetingData.get("date_end_date2"));
		sugar().meetings.createDrawer.save();

		// Verify meeting is created
		sugar().meetings.listView.sortBy("headerName", false);
		sugar().meetings.listView.getDetailField(1, "name").assertEquals(testName, true);

		// Create another meeting
		sugar().meetings.listView.create();
		FieldSet fs = sugar().meetings.getDefaultData();
		nameCtrl.set(fs.get("name"));
		sugar().meetings.createDrawer.save();

		// Verify meeting is created
		sugar().meetings.listView.sortBy("headerName", true);
		sugar().meetings.listView.getDetailField(1, "name").assertEquals(fs.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}