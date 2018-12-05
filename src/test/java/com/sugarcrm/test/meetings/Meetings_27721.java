package com.sugarcrm.test.meetings;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27721 extends SugarTest {
	MeetingRecord meetingRecord;
	String newDate;
	FieldSet meetingData;
	
	public void setup() throws Exception {
		// Set past date from current date
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt);
		c.add(Calendar.DATE, -1);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		newDate = sdf.format(dt);		
		VoodooUtils.voodoo.log.info("set past date: " + newDate + "...");
		meetingData = testData.get(testName).get(0);
		meetingRecord = (MeetingRecord) sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that Start Date should be red if Start Date is past and is not marked as Held status in Meetings list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27721_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		meetingRecord.navToRecord();
		sugar().meetings.recordView.edit();
		
		// date_start_date
		sugar().meetings.recordView.getEditField("date_start_date").set(newDate);
		sugar().meetings.recordView.save();
		
		// Verify that Start Date is red
		sugar().meetings.navToListView();
		new VoodooControl("span", "css", "[data-voodoo-name='recordlist'] table tbody tr:nth-child(1) td:nth-child(4) span").assertAttribute("class", "label-important", true);
	
		// Edit existing record and set status = Canceled
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// TODO: VOOD-1216 - Once this VOOD is resolved remove line no.: 61 to 62 and uncomment line no.: 60
		// sugar().meetings.recordView.getEditField("status").set(meetingData.get("status"));
		new VoodooControl("a", "css", ".fld_status.edit div a").click();
		VoodooControl statusList = new VoodooControl("li", "css", "#select2-drop > ul > li:nth-of-type(3)");
		statusList.waitForVisible();
		statusList.click();
		sugar().meetings.recordView.save();
		sugar().meetings.navToListView();
		
		// Verify that Status color is red "class='label-important'" 
		new VoodooControl("span", "css", "[data-voodoo-name='recordlist'] table tbody tr:nth-child(1) td:nth-child(5) span > span").assertAttribute("class", "label-important", true);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}