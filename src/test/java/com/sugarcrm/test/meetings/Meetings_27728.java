package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27728 extends SugarTest {

	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that Start Date has no background color if Meeting/Calls Status is Held in list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27728_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Set previous date from current date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		date = cal.getTime();
		String yesterdayDate = sdf.format(date);

		// Set next date from current date
		cal.add(Calendar.DATE, 1);
		date = cal.getTime();
		String tomorrowDate = sdf.format(date);

		// Open Meetings record view to edit
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		FieldSet fs = testData.get(testName).get(0);
		String startTime = fs.get("start_time");
		String bgColor = fs.get("bg_color");
		
		// Change Status = Held and Start Date is within 24 hours comparing now
		sugar().meetings.recordView.getEditField("status").set(fs.get("status"));
		VoodooControl startDateCtrl = sugar().meetings.recordView.getEditField("date_start_date");
		startDateCtrl.set(todaysDate);
		sugar().meetings.recordView.getEditField("date_start_time").set(fs.get("start_time"));
		sugar().meetings.recordView.save();
		
		// Go to Meetings Listview
		sugar().meetings.navToListView();
		VoodooControl dateTimeField = sugar().meetings.listView.getDetailField(1, "date_start_date");
		String dateTime1 = todaysDate+" "+startTime;
		
		// Verify Start Date has no background color. Also Date and Time is displayed correctly
		dateTimeField.assertContains(dateTime1, true);
		dateTimeField.assertCssAttribute("color", bgColor, true);
		
		// Go to Meetings record view and change Start Date is past comparing now
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		startDateCtrl.set(yesterdayDate);
		sugar().meetings.recordView.save();
		
		// Go to ListView and verify Start Date has no background color. Also Date and Time is displayed correctly
		sugar().meetings.navToListView();
		String dateTime2 = yesterdayDate+" "+startTime;
		dateTimeField.assertContains(dateTime2, true);
		dateTimeField.assertCssAttribute("color", bgColor, true);
		
		// Go to Meetings record view and change Start Date is beyond 24 hours comparing now
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		startDateCtrl.set(tomorrowDate);
		sugar().meetings.recordView.save();
		
		// Go to ListView and verify Start Date has no background color. Also Date and Time is displayed correctly
		sugar().meetings.navToListView();
		String dateTime3 = tomorrowDate+" "+startTime;
		dateTimeField.assertContains(dateTime3, true);
		dateTimeField.assertCssAttribute("color", bgColor, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}