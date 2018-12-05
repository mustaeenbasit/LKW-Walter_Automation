package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27288 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that no busy appears in the meeting that is scheduled 2 month later.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27288_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// Date of two months after today.
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.MONTH, 2);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String dateAfter2Months = sdf.format(dt);
		
		// Create a meeting that has Start time after 2 months today
		FieldSet fs = new FieldSet();
		fs.put("date_start_date", dateAfter2Months);
		fs.put("date_end_date", dateAfter2Months);
		sugar().meetings.create(fs);
		
		// Create another meeting that has Start time after 2 months today
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.copy();
		sugar().meetings.recordView.getEditField("name").set(customData.get("name"));
		
		// Verify booked time slot shows in Gray color having green line in starting and Red line at end
		// TODO: VOOD-1699
		new VoodooControl("div", "css", "#drawers .participants-schedule .start_end_overlay").assertCssAttribute("border-left", customData.get("green_color"));
		new VoodooControl("div", "css", "#drawers .participants-schedule .start_end_overlay").assertCssAttribute("border-right", customData.get("red_color"));
		
		// Verify that all scheduler are in white color except booked time slot
		// TODO: VOOD-1699
		for (int i = 1; i < 10; i++) {
			if (i!=5)
				new VoodooControl("span", "css", "#drawers .participant .times .container div:nth-child("+i+") .busy").assertVisible(false);
		}		
		sugar().meetings.createDrawer.save();
		
		// Verify, Meeting status for both meeting will be 'Attending'.
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		
		FieldSet meetingStatus = new FieldSet();
		meetingStatus.put("name", "Administrator");
		meetingStatus.put("status", customData.get("status"));
		sugar().meetings.recordView.verifyInvitee(1, meetingStatus);
		
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(2);
		sugar().meetings.recordView.verifyInvitee(1, meetingStatus);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}