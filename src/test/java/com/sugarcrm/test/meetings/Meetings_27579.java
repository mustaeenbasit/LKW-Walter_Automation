package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27579 extends SugarTest {
	MeetingRecord meetingRecord;
	String repeatCount;
	
	public void setup() throws Exception {		
		meetingRecord = (MeetingRecord) sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that a daily recurring meeting is saved when select in Repeat Interval
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27579_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		meetingRecord.navToRecord();
		sugar().meetings.recordView.edit();
		
		// TODO: VOOD-1169 -Provide support for Calls and Meetings Scheduling functionality 
		// Fill Repeat Type = Daily, Occurrences = 10 times and Repeat Interval = 3
		FieldSet customData = testData.get(testName).get(0);
		new VoodooSelect("a", "css", ".fld_repeat_type.edit a").set(customData.get("repeat_type"));
		new VoodooControl("a", "css", ".fld_repeat_interval.edit a").click();
		new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child(3)").click();
		sugar().meetings.recordView.save();
	
		sugar().meetings.navToListView();
		DataSource occurrencesDate = testData.get(testName+"_occurrences_date");
		
		// TODO: VOOD-1217 & VOOD-1288
		// Verify that A recurring meeting is saved with 10 meeting records (Including today's meeting).  Each one has same meeting info except Start Date and End Date.  Start Date is 3 days apart and the first meeting is start at today.
		for(int i = 0; i < occurrencesDate.size(); i++) {
			VoodooUtils.voodoo.log.info("Jenkins datetime: >>>>"+new VoodooControl("tr", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[4]").getText());
			new VoodooControl("tr", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+occurrencesDate.get(i).get("date_start_date")+" "+occurrencesDate.get(i).get("date_start_time")+"')]").assertExists(true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}