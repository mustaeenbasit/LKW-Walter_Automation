package com.sugarcrm.test.meetings;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27299 extends SugarTest {
	String fromTime, endTime, fromDate, endDate, weeklyDt2;
	String newStartDt1, newWeeklyDt2, newWeeklyDt3, dayName;
	
	public void setup() throws Exception {
		// Set date as weekly
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt);
		
		int hour = c.get(Calendar.HOUR); // Get current hours
		int min = c.get(Calendar.MINUTE)+15; // Get current minutes
		int booleanAmPm = c.get(Calendar.AM_PM); // Get current 0/1 : am/pm
		String amPm = "am";
		
		// Set time
		if(booleanAmPm > 0) amPm = "pm";		
		if(hour <= 0) hour = 12;
		if(min >= 0 && min < 15)
			fromTime = (hour <= 9 ? "0" : "") + hour + ":15"+amPm;
		else if(min >= 15 && min < 30)
			fromTime = (hour <= 9 ? "0" : "") + hour +":30"+amPm;
		else if(min >= 30 && min < 45)
			fromTime = (hour <= 9 ? "0" : "") + hour + ":45"+amPm;
		else if(min >= 45) {
			if(hour != 12) hour++;
			fromTime = (hour <= 9 ? "0" : "") + hour +":00pm";
		}
		
		endTime = fromTime; // Instant meetings duration!!

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		fromDate = sdf.format(dt); 		
		endDate = sdf.format(dt); // Call Start date and End date are same
		
		// Now calculate weekly dates
		dt.setTime(dt.getTime() + 7 * 1000 * 60 * 60 * 24);
		weeklyDt2 = sdf.format(dt)+" "+fromTime;

		// Update start date of parent record as edit all reccurance
		dt.setTime(dt.getTime() + 1 * 1000 * 60 * 60 * 24);
		newStartDt1 = sdf.format(dt);
		
		// Get week day name i.e.: Mom, Tue, Wed etc..
		Format formatter = new SimpleDateFormat("EEEE"); 
		dayName = formatter.format(dt).substring(0, 3); // week day name i.e.: Mom, Tue, Wed etc..
	   		   
		dt.setTime(dt.getTime() + 7 * 1000 * 60 * 60 * 24);
		newWeeklyDt2 = sdf.format(dt);

		dt.setTime(dt.getTime() + 7 * 1000 * 60 * 60 * 24);
		newWeeklyDt3 = sdf.format(dt);
		
		sugar().revLineItems.api.create();
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that editing any field on recurring parent meeting will update all occurrences of this meeting
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27299_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a meeting record
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));

		// Start Date
		sugar().meetings.createDrawer.getEditField("date_start_time").set(fromTime);
		sugar().meetings.createDrawer.getEditField("date_start_date").set(fromDate);

		// End Date
		sugar().meetings.createDrawer.getEditField("date_end_date").set(endDate);
		
		// Fill Repeat Type = Weekly, repeat 4 times related to an Opportunity record and Reminder in 10 minutes
		FieldSet customData = testData.get(testName).get(0);
		sugar().meetings.createDrawer.getEditField("repeatType").set(customData.get("repeat_type"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(customData.get("repeat_count"));
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().opportunities.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().meetings.createDrawer.getEditField("remindersEmail").set(customData.get("remindersEmail"));
		sugar().meetings.createDrawer.save();
		
		// TODO: VOOD-1288 -Need Lib support to identify Meetings/Calls > listView > parent record
		String sourcePath = "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'%s')]/td[2]/span/div/a";
		VoodooControl parentRecord = new VoodooControl("a", "xpath", String.format(sourcePath, fromDate));
		parentRecord.click();
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		
		// Click on action drop down, select "Edit All Recurrences".
		sugar().meetings.recordView.getControl("editAllReocurrences").click();
		
		// Change Related to  Opportunity record to RLI record.
		sugar().meetings.recordView.getEditField("relatedToParentType").set("Revenue Line Items");
		sugar().meetings.recordView.getEditField("relatedToParentName").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().meetings.recordView.save();
		
		// Open one of the children meetings
		sugar().meetings.navToListView();
		
		// TODO: VOOD-1288
		new VoodooControl("a", "xpath", String.format(sourcePath, weeklyDt2)).click();
		

		// Verify the Related to field is updated with the RLI record.
		sugar().meetings.recordView.getDetailField("relatedToParentName").assertContains(sugar().revLineItems.getDefaultData().get("name"), true);
		
		// Go to ListView and open parent meetings
		sugar().meetings.navToListView();
		parentRecord.click();
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		
		// Click on action drop down, select "Edit All Recurrences".
		sugar().meetings.recordView.getControl("editAllReocurrences").click();
		
		// TODO: VOOD-1354
		new VoodooSelect("a", "css", ".fld_repeat_dow.edit ul li.select2-search-choice a").click();

		// Set new start date, end date and repeat 4 times
		sugar().meetings.recordView.getEditField("repeatDays").set(dayName);
		sugar().meetings.recordView.getEditField("date_start_date").set(newStartDt1);
		sugar().meetings.recordView.getEditField("repeatOccur").set(customData.get("new_repeat_count"));
		sugar().meetings.recordView.save();
		
		// Go to meetings listView
		sugar().meetings.navToListView();
		
		// TODO: VOOD-1252
		// Verify 5 meeting records
		for(int i = 1; i <= 6; i++) {
			if(i < 6)
				sugar().meetings.listView.getDetailField(i, "name").assertExists(true); // true return for 1 to 5
			else
				sugar().meetings.listView.getDetailField(i, "name").assertExists(false); // false return for 6
		}		

		// TODO: VOOD-1288
		// Open one of the children meeting.
		new VoodooControl("a", "xpath", String.format(sourcePath, newWeeklyDt2)).click();
		
		// Verify the Start Date and End Date have been updated.
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(newWeeklyDt2, true);
		sugar().meetings.recordView.getDetailField("date_end_date").assertContains(newWeeklyDt2, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}