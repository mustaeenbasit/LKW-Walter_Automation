package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27582 extends SugarTest {	
	FieldSet meetingData =  new FieldSet();
	String fromTime = "";
	String endDate = "";
	LinkedHashMap<String, String> monthlyDateSet = new LinkedHashMap<String, String>();
	
	public void setup() throws Exception {
		meetingData = testData.get(testName).get(0);
		
		// Set date as monthly
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt);
		
		// Get current hours
		int hour = c.get(Calendar.HOUR); 
		
		// Get current minutes and add 15 minutes to it
		int min = c.get(Calendar.MINUTE)+15; 
		
		// Get current 0/1 : am/pm
		String amPm =  c.get(Calendar.AM_PM) == 0 ? "am" : "pm"; 
		if (hour <= 0) {
			hour = 12;
		}
		if (min >= 0 && min < 15) {
			fromTime = (hour <= 9 ? "0" : "") + hour + ":15"+amPm;
		}
		else if (min >= 15 && min < 30) {
			fromTime = (hour <= 9 ? "0" : "") + hour +":30"+amPm;
		}
		else if (min >= 30 && min < 45) {
			fromTime = (hour <= 9 ? "0" : "") + hour + ":45"+amPm;
		}
		else if (min >= 45) {
			if(hour != 12) {
				hour++;
			}
			fromTime = (hour <= 9 ? "0" : "") + hour +":00pm";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		// Call Start date and End date are same
		endDate = sdf.format(dt); 
		monthlyDateSet.put("monthlyDt1",sdf.format(dt));
		
		// Now calculate Monthly date with 3 months Interval dates i.e. monthlyDt2, monthlyDt3, monthlyDt4,monthlyDt5
		for (int i = 2; i < 6; i++) {
			
			// Set future date within next 3 months
			c.add(Calendar.MONTH, 3); 
			dt = c.getTime();
			String str = String.format("monthlyDt%d", i);
			monthlyDateSet.put(str, sdf.format(dt));
		}
		
		// Create 5 Leads record with different lastName.
		FieldSet leadsFS = new FieldSet();
		for(int i = 1; i < 6; i++) {
			leadsFS.put("lastName", meetingData.get("leadName") + i);
			sugar().leads.api.create(leadsFS);
		}
		sugar().login();
	}

	/**
	 * Verify that a monthly recurring meeting is saved when select in Repeat Interval
	 * @throws Exception
	 */
	@Test
	public void Meetings_27582_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create meeting with a Monthly recurring, set Repeat Interval as 3, Repeat Occurrences set to 5. 
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		
		// Add 5 Lead invitees. 
		for(int i = 1; i < 6; i++) {
			sugar().meetings.createDrawer.clickAddInvitee();
			sugar().meetings.createDrawer.selectInvitee(meetingData.get("leadName") + i);
		}
		
		// Set Meeting Name
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		
		// Start Date
		sugar().meetings.createDrawer.getEditField("date_start_time").set(fromTime);
		sugar().meetings.createDrawer.getEditField("date_start_date").set(monthlyDateSet.get("monthlyDt1"));

		// End Date
		sugar().meetings.createDrawer.getEditField("date_end_date").set(endDate);
		sugar().meetings.createDrawer.getEditField("repeatType").set(meetingData.get("repeat_type"));
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(meetingData.get("repeatOccurences"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(meetingData.get("repeat_count"));
		sugar().meetings.createDrawer.getEditField("repeatInterval").set(meetingData.get("repeat_interval"));

		// Save meeting
		sugar().meetings.createDrawer.save();
		VoodooControl inviteeForMeetings = sugar().meetings.recordView.getControl("invitees");
		
		// Verify start date of 5 meeting records
		sugar().meetings.listView.sortBy("headerDatestart", true);
		for (int i = 1; i < 6; i++) {
			sugar().meetings.listView.getDetailField(i, "date_start_date").assertContains(monthlyDateSet.get("monthlyDt" + i), true);
			}	
		
		// TODO: VOOD-1354 - Need Lib Support for "More Guest..." link in Meetings/Calls sidecar module recordView.
		VoodooControl showMoreLink = new VoodooControl("button", "css", "[data-action='show-more']");
			
		// Verify 5 Leads are appearing in each recurring meeting
		for (int i = 1; i < 6; i++) {
			sugar().meetings.listView.clickRecord(i);
			showMoreLink.click();
			VoodooUtils.waitForReady();
				
			// Verify name of lead in invitees list
			for (int j = 1; j < 6; j++) {
				String leadFullName = (sugar().leads.getDefaultData().get("firstName") + " " + meetingData.get("leadName") + "" + j);
				inviteeForMeetings.assertContains(leadFullName, true);
			}
				sugar().meetings.navToListView();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}