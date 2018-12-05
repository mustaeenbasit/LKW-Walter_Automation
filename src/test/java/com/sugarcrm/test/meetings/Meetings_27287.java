package com.sugarcrm.test.meetings;

import org.junit.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27287 extends SugarTest {
	UserRecord chris, max;
	MeetingRecord myMeeting;
	FieldSet ds, user;

	public void setup() throws Exception {
		sugar().login();

		ds = testData.get(testName).get(0);
		user = testData.get(testName+"_Max").get(0);
		sugar().meetings.api.create();

		chris = (UserRecord) sugar().users.create();
		VoodooUtils.focusDefault();
		max = (UserRecord)sugar().users.create(user);
	}

	/**
	 * Verify that no warn message when double schedule a meeting at the same time slot
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27287_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logout as Admin and logged in as Chris
		sugar().logout();
		sugar().login(chris);
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		
		// Invites Max in Chris's meeting
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(max.get("lastName"));
		
		// Add Chris in guest list (API creation of record has Admin in guest list)
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(chris.get("lastName"));
		sugar().meetings.recordView.save();
		
		// Navigate to meeting record view
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		
		// Verify that Max and Chris both are in the meeting scheduler
		FieldSet chrisUser = new FieldSet();
		chrisUser.put("name", chris.get("fullName"));
		sugar().meetings.recordView.verifyInvitee(3, chrisUser);
		
		FieldSet maxUser = new FieldSet();
		maxUser.put("name", max.get("fullName"));
		sugar().meetings.recordView.verifyInvitee(4, maxUser);
		
		// TODO: VOOD-1699
		VoodooControl busyTimeSlot1 = new VoodooControl("span", "css", ".record div:nth-child(5) div:nth-child(5) .busy");
		VoodooControl busyTimeSlot2 = new VoodooControl("span", "css", ".normal.index div:nth-child(7) div:nth-child(5) .busy");
		VoodooControl busyTimeSlot3 = new VoodooControl("span", "css", ".normal.index div:nth-child(7) div:nth-child(5) .busy");
		
		// Verify in the scheduler that Chris and Max both shows dark grey color.
		busyTimeSlot1.assertCssAttribute("background-color", ds.get("grey_color"));
		busyTimeSlot2.assertCssAttribute("background-color", ds.get("grey_color"));
		busyTimeSlot3.assertCssAttribute("background-color", ds.get("grey_color"));
		sugar().logout();
		
		// Logged in as Max
		sugar().login(max);
		FieldSet fs = new FieldSet();
		fs.put("name", ds.get("meeting"));
		
		// Max schedules a meeting at the same time the Chris meeting scheduled. 
		myMeeting = (MeetingRecord) sugar().meetings.api.create(fs);
		
		myMeeting.navToRecord();
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.showMore();
		
		// Invites Chris in Max's meeting
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(chris.get("lastName"));

		// Add Max in guest list (API creation of record has Admin in guest list)
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(max.get("lastName"));
		sugar().meetings.recordView.getControl("saveButton").click();
		
		// Verify that Meeting is allowed to save
		String alertText = sugar().alerts.getAlert().getText().toString();
		Assert.assertTrue("Meeting is not allowed to save",alertText.contains(ds.get("alertText")));
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		
		// Verify in the scheduler that Max and Chris both show as dark grey color (conflicting)
		// TODO: VOOD-1699
		busyTimeSlot1.assertCssAttribute("background-color", ds.get("grey_color"));
		busyTimeSlot2.assertCssAttribute("background-color", ds.get("grey_color"));
		busyTimeSlot3.assertCssAttribute("background-color", ds.get("grey_color"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}