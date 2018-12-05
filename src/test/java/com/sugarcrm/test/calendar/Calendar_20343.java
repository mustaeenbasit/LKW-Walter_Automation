package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Calendar_20343 extends SugarTest {
	String today;

	public void setup() throws Exception {
		// Create a Meeting for today, default time
		FieldSet startDate = new FieldSet();
		today = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		startDate.put("date_start_date",today);
		sugar.meetings.api.create(startDate);
		sugar.login();
	}

	/**
	 * Edit meeting_Verify that invitees are added to (any existing) meeting
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20343_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Open an existing Meeting from "Calendar" grid, add invitee 
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Click an existing Meetings subject in the Calendar view.
		// TODO: VOOD-863  
		VoodooControl meetingCtrl = new VoodooControl("div", "css", "div[datetime='" 
		 + today + " " + sugar.meetings.getDefaultData().get("date_start_time") + "']");
		meetingCtrl.assertContains(sugar.meetings.getDefaultData().get("name"), true);
		meetingCtrl.click();
		
		// Edit meeting, add invitee Default User and save Meeting.
		sugar.meetings.recordView.edit();
		sugar.meetings.recordView.clickAddInvitee();
		sugar.meetings.recordView.selectInvitee(sugar.users.qaUser.get("userName"));
		sugar.meetings.recordView.save();
		
		// Assert user added is listed under intivees
		// TODO: VOOD-1350 (Need more appropriate controls for invitee list)
		sugar.meetings.recordView.getControl("invitees").assertContains
		 	(sugar.users.qaUser.get("userName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}