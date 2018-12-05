package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27302 extends SugarTest {
	DataSource customData;
	String startTime, repeatCount, checkFirstElement, myDate;
	Integer count;
	MeetingRecord meetingRecord;
	
	public void setup() throws Exception {
		customData = testData.get(testName);
		meetingRecord = (MeetingRecord) sugar().meetings.api.create();
	}

	/**
	 * Verify that Repeat Type on record view should be read only when there are recurrences and not in "edit all recurrences" mode.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27302_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Login with qauser
		sugar().login(sugar().users.getQAUser());
		
		// Create meetings record
		meetingRecord.navToRecord();
		sugar().meetings.recordView.edit();
		startTime = sugar().meetings.recordView.getEditField("date_start_time").getText(); // store start time to a string
		myDate = sugar().meetings.recordView.getEditField("date_start_date").getText(); // store start time to a string
		VoodooUtils.voodoo.log.info("Get start time:"+startTime + ">>>");
		
		// Check if first element have 0 then use substring
		String checkFirstElement = startTime.substring(0, 1);
		VoodooUtils.voodoo.log.info("Get start char:"+checkFirstElement + ">>>");
		if(checkFirstElement.contentEquals("0")){
			startTime = startTime.substring(1); 
		}		
		
		VoodooUtils.voodoo.log.info("Get start time2:"+startTime + ">>>");
		
		// TODO: VOOD-1169 -Provide support for Calls and Meetings Scheduling functionality 
		// Fill Repeat Type = Weekly and Occurrences = 3 times
		new VoodooSelect("a", "css", ".fld_repeat_type.edit a").set(customData.get(0).get("repeat_type"));
		new VoodooControl("input", "css", ".fld_repeat_count.edit input").set(customData.get(0).get("repeat_count"));
		sugar().meetings.recordView.save();
		
		sugar().meetings.navToListView();
		
		// Verify that 3 recurring meetings listed.  Start Date is in the same time of each week.
		repeatCount =  customData.get(0).get("repeat_count");
		count = Integer.parseInt(repeatCount);
		for(int i = 1; i <= count; i++) {
			VoodooUtils.voodoo.log.info("Get start time on list view:"+new VoodooControl("div", "css", "div.flex-list-view-content > table > tbody > tr:nth-child("+i+") > td:nth-child(4) > span > div").getText() + ">>>");
			new VoodooControl("div", "css", "div.flex-list-view-content > table > tbody > tr:nth-child("+i+") > td:nth-child(4) > span > div").assertContains(startTime, true);
		}
		
		// VOOD-757 -need lib support of the recurrence controls on call/meeting's edit view
		// Open the parent(latest start date) meeting record view and click on "Edit All Recurrences"
		new VoodooControl("div", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+myDate+"')]/td[2]/span/div").click();
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_edit_recurrence_button.detail a").click();
		
		// Fill Repeat Type = Monthly and Occurrences = 4 times
		new VoodooSelect("a", "css", ".fld_repeat_type.edit a").set(customData.get(1).get("repeat_type"));
		new VoodooControl("input", "css", ".fld_repeat_count.edit input").set(customData.get(1).get("repeat_count"));
		sugar().meetings.recordView.save();
		
		sugar().meetings.navToListView();
		
		// Verify that 4 recurring meetings listed.  Start Date is in the same time of each month.
		repeatCount =  customData.get(1).get("repeat_count");
		count = Integer.parseInt(repeatCount);
		for(int i = 1; i <= count; i++) {
			new VoodooControl("div", "css", "div.flex-list-view-content > table > tbody > tr:nth-child("+i+") > td:nth-child(4) > span > div").assertContains(startTime, true);
		}
		
		// Verify that repeat field is read-only.
		new VoodooControl("input", "css", ".fld_repeat_type.detail input").assertExists(false);		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}