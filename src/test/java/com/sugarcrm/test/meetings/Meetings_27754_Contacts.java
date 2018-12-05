package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Meetings_27754_Contacts extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that repeatable meeting is created from sub panel 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27754_Contacts_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		FieldSet customFS = testData.get(testName).get(0);

		// Verify that repeatable meeting is created from sub panel for contacts
		// Go to Contacts module and open one contacts record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		StandardSubpanel meetingSubpanel = sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);

		// Create a new meeting from Subpanel
		meetingSubpanel.addRecord();

		// Select Daily, 3 Repeat Occurrence
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("repeatType").set(customFS.get("repeat_type"));
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(customFS.get("repeat_occurrence"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(customFS.get("repeat_count"));
		sugar().meetings.createDrawer.save();

		// Verify that 3 meetings are created and listed in the sub panel
		meetingSubpanel.scrollIntoView();
		int count= Integer.parseInt(customFS.get("repeat_count"));
		for (int i = 1; i <= count; i++) {
			meetingSubpanel.getDetailField(i, "name").assertEquals(testName, true);
		}
		String contactName = sugar().contacts.getDefaultData().get("firstName") + " " + sugar().contacts.getDefaultData().get("lastName");
		sugar().meetings.navToListView();
		for (int i = 1; i <= count; i++) {
			sugar().meetings.listView.getDetailField(i, "relatedToParentName").assertEquals(contactName, true);
		}
		sugar().meetings.listView.clickRecord(1);

		// Verify Contact's Account is in Related To field in the meeting record
		String contactFullName = sugar().contacts.getDefaultData().get("fullName");
		sugar().meetings.recordView.getDetailField("relatedToParentName").assertContains(contactFullName, true);

		// Verify that contact is in Guests field.
		FieldSet fs = new FieldSet();
		fs.put("fullName", contactFullName);
		sugar().meetings.recordView.verifyInvitee(2, fs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}