package com.sugarcrm.test.meetings;

import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_29917 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that User does not find 'Repeat Until' date as blank in Meetings
	 * @throws Exception
	 */
	@Test
	public void Meetings_29917_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calculating the values for Current date & Repeat Until date
		DateTime date = DateTime.now();
		FieldSet meetingData = testData.get(testName).get(0);
		String currentDate = date.toString("MM/dd/yyyy");
		String repeatUntilDate = date.plusDays(3).toString("MM/dd/yyyy");

		// Create a custom meeting record
		FieldSet fs = new FieldSet();
		fs.put("date_start_date", currentDate);
		fs.put("date_end_date", currentDate);
		fs.put("repeatType", meetingData.get("repeatType"));
		fs.put("repeatInterval", meetingData.get("repeatInterval"));
		fs.put("repeatUntil",repeatUntilDate);
		sugar().meetings.create(fs);

		// Sorting the meetings with Start Date
		// TODO: TR-10791 - Recursive calls/meeting records are not coming in correct reverse chronological
		// order in list view (Hinders Automation)
		sugar().meetings.listView.sortBy("headerDatestart", true);

		// Calculating the no. of rows in Meetings list view
		int rowCount = sugar().meetings.listView.countRows();
		String meetingName = sugar().meetings.getDefaultData().get("name");

		// Verify meetings with same name  and recursive start date created on Meetings list view
		for (int i = 1; i <= rowCount; i++) {
			sugar().meetings.listView.verifyField(i, "name", meetingName );
			sugar().meetings.listView.verifyField(i, "date_start_date", date.plusDays(i-1).toString("MM/dd/yyyy"));
		}

		// Clicking on last recursive meeting record
		sugar().meetings.listView.clickRecord(rowCount);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.save();

		// Asserting that 'Repeat Until' date doesn't go blank in recursive meeting record on Edit & Save
		sugar().meetings.recordView.getDetailField("repeatUntil").assertEquals(repeatUntilDate, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}