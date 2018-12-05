package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vishal Kumar <vkumar@sugarcrm.com>
 */
public class Meetings_20445 extends SugarTest {
	String meetingDate = null;
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	Calendar cal = Calendar.getInstance();
	FieldSet myData = new FieldSet();

	public void setup() throws Exception {
		FieldSet fs = new FieldSet();
		meetingDate = sdf.format(cal.getTime());

		// Create Meeting without recurrences with current date
		myData = testData.get(testName).get(0);
		fs.put("name", myData.get("meetingName"));
		fs.put("date_start_date", meetingDate);
		sugar().meetings.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify Edit single meeting - set to yearly recurring
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_20445_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit existing meetings
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("repeatType").set(
				myData.get("repeatType"));
		sugar().meetings.recordView.getEditField("repeatInterval").set(
				myData.get("repeatInterval"));
		sugar().meetings.recordView.getEditField("repeatOccur").set(
				myData.get("repeatOccur"));
		sugar().meetings.recordView.save();
		sugar().meetings.navToListView();

		// verifying meetings list view contains only three meetings
		Assert.assertEquals("should only three records in meetings listview",
				Integer.parseInt(myData.get("repeatOccur")),
				sugar().meetings.listView.countRows());

		// Sort the list view of meetings
		sugar().meetings.listView.sortBy("headerDatestart", true);

		// Verifying meeting name and correct date in list view
		for (int i = 1; i <= sugar().meetings.listView.countRows(); i++) {
			sugar().meetings.listView.getDetailField(i, "date_start_date")
					.assertContains(meetingDate, true);
			sugar().meetings.listView.getDetailField(i, "name").assertContains(
					myData.get("meetingName"), true);

			// Increment date by one year
			cal.add(Calendar.YEAR, 1);
			meetingDate = sdf.format(cal.getTime());
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}