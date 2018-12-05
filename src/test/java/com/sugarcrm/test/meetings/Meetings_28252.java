package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_28252 extends SugarTest {
	FieldSet meetingData;
	LeadRecord myLead;

	public void setup() throws Exception {
		sugar().login();
		meetingData = testData.get(testName).get(0);
		myLead = (LeadRecord) sugar().leads.api.create();

		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that "Related To" Lead is removed in the Guest field for all children and parent record
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_28252_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		sugar().meetings.createDrawer.getEditField("name").set(meetingData.get("name"));

		sugar().meetings.createDrawer.getEditField("repeatType").set(meetingData.get("repeatType"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(meetingData.get("repeatOccur"));

		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(myLead.getRecordIdentifier());

		// Click on "-" for the Lead in the Guest field.
		// TODO: VOOD-1350
		new VoodooControl("button", "css", "div[data-module='Leads'] > div.cell.buttons button[data-action='removeRow']").click();
		// Verify that Lead is removed from the Guest field. 
		new VoodooControl("div", "css", ".participants-schedule [data-module='Leads'] ").assertVisible(false);
		sugar().meetings.createDrawer.save();

		sugar().meetings.listView.previewRecord(1);

		// Verify in GuestField qaUser can see only qaUser
		sugar().previewPane.getPreviewPaneField("assignedTo").assertEquals(sugar().users.getQAUser().get("lastName"), true);

		// Verify in the "Related to" field, still see the Lead
		sugar().previewPane.getPreviewPaneField("relatedToParentName").assertEquals(sugar().leads.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}