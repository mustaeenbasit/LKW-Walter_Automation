package com.sugarcrm.test.meetings;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class Meetings_21163 extends SugarTest {
	ArrayList<Record> myMeetings;
	DataSource meetingDS, dropdownDS;

	public void setup() throws Exception {
		meetingDS = testData.get(testName);
		dropdownDS = testData.get(testName+"_1");
		myMeetings = sugar().meetings.api.create(meetingDS);
		sugar().login();
	}

	/**
	 * New action dropdown list in meeting detail view page
	 * @throws Exception
	 */
	@Test
	public void Meetings_21163_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		for(Record meeting: myMeetings) {
			meeting.navToRecord();
			sugar().meetings.recordView.openPrimaryButtonDropdown();
			sugar().meetings.recordView.getControl("editButton").assertVisible(true);
			sugar().meetings.recordView.getControl("editButton").assertEquals(dropdownDS.get(0).get("dropdown_menu_action"), true);
			sugar().meetings.recordView.getControl("deleteButton").assertVisible(true);
			sugar().meetings.recordView.getControl("deleteButton").assertEquals(dropdownDS.get(2).get("dropdown_menu_action"), true);
			sugar().meetings.recordView.getControl("copyButton").assertVisible(true);
			sugar().meetings.recordView.getControl("copyButton").assertEquals(dropdownDS.get(1).get("dropdown_menu_action"), true);
			sugar().meetings.recordView.getControl("share").assertVisible(true);
			sugar().meetings.recordView.getControl("share").assertEquals(dropdownDS.get(3).get("dropdown_menu_action"), true);

			// if status is not held, close and close create new button not appears
			if(meeting.get("status").equals(meetingDS.get(0).get("status"))) {
				sugar().meetings.recordView.getControl("closeAndCreateNew").assertVisible(false);
				sugar().meetings.recordView.getControl("close").assertVisible(false);
			}
			else {
				sugar().meetings.recordView.getControl("closeAndCreateNew").assertVisible(true);
				sugar().meetings.recordView.getControl("closeAndCreateNew").assertEquals(dropdownDS.get(5).get("dropdown_menu_action"), true);
				sugar().meetings.recordView.getControl("close").assertVisible(true);
				sugar().meetings.recordView.getControl("close").assertEquals(dropdownDS.get(4).get("dropdown_menu_action"), true);
			}

			// Trigger delete menu action from dropdown
			sugar().meetings.recordView.getControl("deleteButton").click();
			sugar().alerts.getWarning().confirmAlert();
		}	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}