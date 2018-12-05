package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27000 extends SugarTest {
	FieldSet customData = new FieldSet();
	String date1 = "", date2 = "";

	public void setup() throws Exception {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		date1 = sdf.format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 3);
		date = cal.getTime();
		date2 = sdf.format(date);

		int hour = cal.get(Calendar.HOUR); // Get current hours
		int min = cal.get(Calendar.MINUTE); // Get current minutes
		int booleanAmPm = cal.get(Calendar.AM_PM); // Get current 0/1 : am/pm
		String amPm = (booleanAmPm == 0) ? "am" : "pm";
		String time = "";
		if (hour <= 0) hour = 12;

		// Set hours and minutes as per requirement
		if (min >= 0 && min < 15) {
			time = hour + ":15" + amPm;
		} else if (min >= 15 && min < 30)
			time = hour + ":30" + amPm;
		else if (min >= 30 && min < 45)
			time = hour + ":45" + amPm;
		else if (min >= 45 && min < 59) {
			if (hour != 12) hour = (hour + 1);
			time = hour + ":00pm";
		}

		customData = testData.get(testName).get(0);
		// Create three meetings with different name and start date
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		fs.put("date_start_date", date1);
		fs.put("date_start_time", time);
		sugar().meetings.api.create(fs);
		fs.clear();
		fs.put("date_start_date", date2);
		fs.put("date_start_time", time);
		sugar().meetings.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify that create filter works correctly in list view
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27000_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create filter
		sugar().meetings.navToListView();
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterCreateNew();

		// Set filter Fields
		sugar().meetings.listView.filterCreate.setFilterFields("date_start_date", customData.get("type"), customData.get("operator"), sugar().meetings.getDefaultData().get("date_start_date"), 1);
		// Verify no record with invalid date search
		sugar().meetings.listView.assertIsEmpty();

		sugar().meetings.listView.filterCreate.setFilterFields("date_start_date", customData.get("type"), customData.get("operator"), date2, 1);
		// / Verify record with valid date search
		sugar().meetings.listView.verifyField(1, "name", sugar().meetings.getDefaultData().get("name"));

		sugar().meetings.listView.filterCreate.setFilterFields("date_start_date", customData.get("type"), customData.get("operator"), date1, 1);
		// Verify different record with valid date search
		sugar().meetings.listView.verifyField(1, "name", testName);

		// Save the filter
		sugar().meetings.listView.filterCreate.getControl("filterName").set(customData.get("filterName"));
		VoodooUtils.waitForReady();
		sugar().meetings.listView.filterCreate.getControl("saveButton").click();

		// Verify success alert and record after save filter
		sugar().alerts.getSuccess().assertVisible(true);
		sugar().alerts.getSuccess().assertContains(customData.get("successMsg"), true);
		sugar().meetings.listView.verifyField(1, "name", testName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}