package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21054 extends SugarTest {
	DataSource meetingData;

	public void setup() throws Exception {
		sugar().login();
		meetingData = testData.get(testName);
		sugar().meetings.create(meetingData);
	}

	/**
	 * Sort meeting_Verify that meetings can be sorted by some columns in meeting list view.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21054_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set Name in Ascending Order.
		sugar().meetings.listView.sortBy("headerName", true);

		// Verification of Name in Ascending Order
		sugar().meetings.listView.verifyField(1, "name", (meetingData.get(1).get("name")));
		sugar().meetings.listView.verifyField(2, "name", (meetingData.get(2).get("name")));
		sugar().meetings.listView.verifyField(3, "name", (meetingData.get(0).get("name")));

		// Set Start Date in Ascending Order
		sugar().meetings.listView.sortBy("headerDatestart", true);

		// Verification of Start Date in Ascending Order
		sugar().meetings.listView.verifyField(1, "date_start_date", meetingData.get(2).get("date_start_date") +" "+ meetingData.get(2).get("date_start_time"));
		sugar().meetings.listView.verifyField(2, "date_start_date", meetingData.get(0).get("date_start_date") +" "+ meetingData.get(0).get("date_start_time"));
		sugar().meetings.listView.verifyField(3, "date_start_date", meetingData.get(1).get("date_start_date") +" "+ meetingData.get(1).get("date_start_time"));

		// Set UserName in Ascending Order.
		sugar().meetings.listView.sortBy("headerAssignedusername", true);

		// Verification of UserName in Ascending Order
		sugar().meetings.listView.verifyField(1, "assignedTo", meetingData.get(0).get("assignedTo"));
		sugar().meetings.listView.verifyField(2, "assignedTo", meetingData.get(1).get("assignedTo"));
		sugar().meetings.listView.verifyField(3, "assignedTo", meetingData.get(2).get("assignedTo"));

		// Set Name in Descending Order
		sugar().meetings.listView.sortBy("headerName", false);

		// Verification of Name in Descending Order
		sugar().meetings.listView.verifyField(1, "name", (meetingData.get(0).get("name")));
		sugar().meetings.listView.verifyField(2, "name", (meetingData.get(2).get("name")));
		sugar().meetings.listView.verifyField(3, "name", (meetingData.get(1).get("name")));

		// Set Start Date in Descending Order
		sugar().meetings.listView.sortBy("headerDatestart", false);

		// Verification of Start Date in Descending Order
		sugar().meetings.listView.verifyField(1, "date_start_date", meetingData.get(1).get("date_start_date") +" " +meetingData.get(1).get("date_start_time"));
		sugar().meetings.listView.verifyField(2, "date_start_date", meetingData.get(0).get("date_start_date") +" " +meetingData.get(0).get("date_start_time"));
		sugar().meetings.listView.verifyField(3, "date_start_date", meetingData.get(2).get("date_start_date") +" " +meetingData.get(2).get("date_start_time"));

		// Set User Name in Descending Order
		sugar().meetings.listView.sortBy("headerAssignedusername", false);

		// Verification of User Name in Descending Order
		sugar().meetings.listView.verifyField(1, "assignedTo", meetingData.get(2).get("assignedTo"));
		sugar().meetings.listView.verifyField(2, "assignedTo", meetingData.get(1).get("assignedTo"));
		sugar().meetings.listView.verifyField(3, "assignedTo", meetingData.get(0).get("assignedTo"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}