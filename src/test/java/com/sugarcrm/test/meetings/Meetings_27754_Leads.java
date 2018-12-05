package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Meetings_27754_Leads extends SugarTest {

	public void setup() throws Exception {
		// Create test lead record
		sugar().leads.api.create();
		
		// Login as admin
		sugar().login();
	}

	/**
	 * Verify that repeatable meeting is created from sub panel 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27754_Leads_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Initializing test data
		FieldSet customData = testData.get(testName).get(0);
		
		// Go to Lead module and open above created lead record
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		StandardSubpanel meetingSubpanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);

		// Create a new meeting
		meetingSubpanel.addRecord();

		// Select Daily, 3 Repeat Occurrence
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("repeatType").set(customData.get("repeat_type"));
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(customData.get("repeat_occurrence"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(customData.get("repeat_count"));
		sugar().meetings.createDrawer.save();
		
		int count= Integer.parseInt(customData.get("repeat_count"));
		
		// Verify that 3 meetings are created and listed in the sub panel
		for (int i = 1; i <= count; i++) {
			meetingSubpanel.getDetailField(i, "name").assertEquals(testName, true);
		}

		// Navigate to Meetings module
		sugar().meetings.navToListView();
		
		String leadName = sugar().leads.getDefaultData().get("firstName") + " " + sugar().leads.getDefaultData().get("lastName");
		
		// Verify that Lead name is displayed in the related to column in Meeting records
		for (int i = 1; i <= count; i++) {
			sugar().meetings.listView.getDetailField(i, "relatedToParentName").assertEquals(leadName, true);
		}
		
		// Navigate to meeting detail view
		sugar().meetings.listView.clickRecord(1);

		// Verify leadname is in Related To field in the meeting record
		String leadFullName = sugar().leads.getDefaultData().get("fullName");
		sugar().meetings.recordView.getDetailField("relatedToParentName").assertContains(leadFullName, true);

		// Verify that contact is in Guests field.
		FieldSet fs = new FieldSet();
		fs.put("fullName", leadFullName);
		sugar().meetings.recordView.verifyInvitee(2, fs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}