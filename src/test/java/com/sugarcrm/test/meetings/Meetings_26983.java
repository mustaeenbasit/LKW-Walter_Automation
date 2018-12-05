package com.sugarcrm.test.meetings;

import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_26983 extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that the meeting is created when fill in all non-required fields
	 * @throws Exception
	 */
	@Test
	public void Meetings_26983_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet meetingData = testData.get(testName).get(0);
		FieldSet meetingDefaultData = sugar().meetings.getDefaultData();
		String meetingName = meetingDefaultData.get("name");
		String qaUser = sugar().users.getQAUser().get("userName");
		String contactName = sugar().contacts.getDefaultData().get("lastName");
		String meetingStatus = meetingDefaultData.get("status");

		// Create a Meeting Record
		sugar().navbar.selectMenuItem(sugar().meetings, "create" + sugar().meetings.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("name").set(meetingName);
		sugar().meetings.createDrawer.getEditField("description").set(meetingDefaultData.get("description"));
		sugar().meetings.createDrawer.getEditField("assignedTo").set(qaUser);
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().contacts.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(contactName);
		sugar().meetings.createDrawer.getEditField("location").set(meetingData.get("location"));
		sugar().meetings.createDrawer.getEditField("type").set(meetingDefaultData.get("type"));
		sugar().meetings.createDrawer.getEditField("status").set(meetingStatus);

		// Set the Until Date
		DateTime date = DateTime.now();
		String repeatUntilDate = date.plusWeeks(2).toString("MM/dd/yyyy");
		sugar().meetings.createDrawer.getEditField("repeatType").set(meetingData.get("repeatType"));
		sugar().meetings.createDrawer.getEditField("repeatUntil").set(repeatUntilDate);
		sugar().meetings.createDrawer.save();

		// Verify subject, assignedTo, relatedTo, status
		// TODO: VOOD-1537 - ListView VerifyField(row,field,value) should use assertEquals not assertContains
		sugar().meetings.listView.getDetailField(1, "name").assertEquals(meetingName, true);
		sugar().meetings.listView.getDetailField(1, "assignedTo").assertEquals(qaUser, true);
		sugar().meetings.listView.getDetailField(1, "relatedToParentName").assertContains(contactName, true);
		sugar().meetings.listView.getDetailField(1, "status").assertEquals(meetingStatus, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}