package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27720 extends SugarTest {
	
	public void setup() throws Exception {
		// Initialize Test Data
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that Start Date should be red if Start Date is past and is not marked as Held status in Meetings list view
	 * @throws Exception
	 */
	@Test
	public void Meetings_27720_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Set meetings start date as current date and start time as past time from current time
		String currentDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		DataSource meetingDataDS = testData.get(testName);
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("date_start_date").set(currentDate);
		sugar().meetings.recordView.getEditField("date_start_time").set(meetingDataDS.get(0).get("start_time"));
		sugar().meetings.recordView.save();
		
		// Navigate to Meetings list view and verify Start Date is red and also Date Time appears.
		sugar().meetings.navToListView();
		String startDateTime = currentDate + " " + meetingDataDS.get(0).get("start_time");
		sugar().meetings.listView.getDetailField(1, "date_start_date").assertContains(startDateTime, true);
		
		// TODO: VOOD-1882
		VoodooControl listViewStartDateTime = new VoodooControl("span", "css", ".list.fld_date_start");
		listViewStartDateTime.assertCssAttribute("background-color", meetingDataDS.get(0).get("bg_color"), true);
	
		// Edit existing record and set meeting status as Canceled
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("status").set(meetingDataDS.get(1).get("status"));
		sugar().meetings.recordView.save();
		
		// Navigate to meetings list view and verify once meeting status is cancelled then Start Date is red and also Date Time appears.
		sugar().meetings.navToListView();
		sugar().meetings.listView.getDetailField(1, "date_start_date").assertContains(startDateTime, true);
		listViewStartDateTime.assertCssAttribute("background-color", meetingDataDS.get(1).get("bg_color"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}