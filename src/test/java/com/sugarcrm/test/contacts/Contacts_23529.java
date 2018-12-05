package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Contacts_23529 extends SugarTest {
	StandardSubpanel meetingsSubpanel;
	
	public void setup() throws Exception {
		ContactRecord myContact = (ContactRecord) sugar().contacts.api.create();	
		MeetingRecord myMeeting = (MeetingRecord) sugar().meetings.api.create();	
		sugar().login();
		
		myContact.navToRecord();
		meetingsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().meetings
				.moduleNamePlural);
		
		// Linking meeting with the contact
		meetingsSubpanel.linkExistingRecord(myMeeting);
	}

	/**
	 * Edit meeting_Verify that a related meeting can be edited from contact detail view.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23529_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		String name = customData.get("newName");
		String status = customData.get("newStatus");
		
		FieldSet meetingData = new FieldSet();
		meetingData.put("name", name);
		meetingData.put("status", status);
		
		// Editing the meeting in subpanel
		meetingsSubpanel.editRecord(1,meetingData);
		
		// Verifying that meeting record is displayed as edited in Sub panel
		// TODO: VOOD-1424
		meetingsSubpanel.getDetailField(1, "name").assertEquals(name, true);
		meetingsSubpanel.getDetailField(1, "status").assertEquals(status, true);
		
		// Clicking meeting name to navigate to the meeting record view
		meetingsSubpanel.clickRecord(1);
		
		// Verifying that meeting name is displayed as edited on the record view too
		sugar().meetings.recordView.getDetailField("name").assertEquals(name, true);
		
		// Verifying that meeting status is displayed as edited on the record view too
		sugar().meetings.recordView.getDetailField("status").assertEquals(status, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
