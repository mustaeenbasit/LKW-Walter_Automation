package com.sugarcrm.test.calls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_27724 extends SugarTest {
	CallRecord callRecord;
	String newDate, futureDate, amPm, newTime;
	FieldSet callsData,fs;

	public void setup() throws Exception {
		// Set past date from current date
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		newDate = sdf.format(dt);
		c.add(Calendar.DATE, 2); // set future date
		dt = c.getTime();
		futureDate = sdf.format(dt);
		int hour = c.get(Calendar.HOUR); // Get current hours
		int min = c.get(Calendar.MINUTE); // Get current minutes
		// Get current 0/1 : am/pm
		amPm = c.get(Calendar.AM_PM) > 0 ? "pm" : "am";
		if(hour <= 0) hour = 12;
		
		// Set hours and minutes as per requirement
		if(min >= 0 && min < 15) {			
			newTime = hour + ":15" + amPm;
		} else if(min >= 15 && min < 30)
			newTime = hour + ":30" + amPm;
		else if(min >= 30 && min < 45)
			newTime = hour + ":45" + amPm;
		else if(min >= 45 && min < 59) {
			if(hour != 12) hour = (hour+1);
			newTime = hour + ":00pm";
		}

		VoodooUtils.voodoo.log.info("log set time: " + newTime + ">> current date: "+newDate+ ">> current Am/PM : "+amPm);
		VoodooUtils.voodoo.log.info("set future date: " + futureDate + "...");
		callsData = testData.get(testName).get(0);
		fs = new FieldSet();
		fs.put("date_start_date", futureDate);
		fs.put("date_start_time", newTime);
		callRecord = (CallRecord) sugar.calls.api.create(fs);
		sugar.login();
	}

	/**
	 * Verify that Start Date should be blue if Start Date is within next 24 hours and is not marked as Held status in Calls listview
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_27724_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();

		// Verify that Start Date should not have blue or red color
		sugar.calls.navToListView();
		VoodooControl startDate = new VoodooControl("span", "css", "[data-voodoo-name='recordlist'] table tbody tr:nth-child(1) td:nth-child(4) span");
		startDate.assertAttribute("class", "label-info", false);
		startDate.assertAttribute("class", "label-important", false);
			
		// TODO: SC-2765 -Spike: Cannot fill in date fields in CI.
		// TODO: Once SC-2765 resolved remove line#78 to #82 and uncomment line#84 to #89
		sugar.calls.api.deleteAll();
		fs.clear();
		fs.put("date_start_date", newDate);
		fs.put("date_start_time", newTime);
		callRecord = (CallRecord) sugar.calls.api.create(fs);
		
		//		// set start date and time
		//		callRecord.navToRecord();
		//		sugar.calls.recordView.edit();
		//		sugar.calls.recordView.getEditField("date_start_date").set(newDate);
		//		sugar.calls.recordView.getEditField("date_start_time").set(newTime);
		//		sugar.calls.recordView.save();

		// Verify that Start Date is blue
		sugar.calls.navToListView();
		startDate.assertAttribute("class", "label-info", true);

		
		// Edit existing record and set status = Canceled
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();

		// TODO: VOOD-1216 - Once this VOOD is resolved remove line#102 to #105 and uncomment line#101
		// sugar.meetings.recordView.getEditField("status").set(callsData.get("status"));
		new VoodooControl("a", "css", ".fld_status.edit div a").click();
		VoodooControl statusList = new VoodooControl("li", "css", "#select2-drop > ul > li:nth-of-type(3)");
		statusList.waitForVisible();
		statusList.click();
		sugar.calls.recordView.save();		

		// Verify that Start Date color should be blue "class='label-info'" and Status color should be red "class='label-important'" 
		sugar.calls.navToListView();
		startDate.assertAttribute("class", "label-info", true);
		new VoodooControl("span", "css", "[data-voodoo-name='recordlist'] table tbody tr:nth-child(1) td:nth-child(5) span span").assertAttribute("class", "label-important", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}