package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_26704 extends SugarTest {
	ContactRecord myContact;
	CallRecord myCall;
	MeetingRecord myMeeting;
	TaskRecord myTask;
	NoteRecord myNotes;

	public void setup() throws Exception {
		FieldSet statusData = testData.get(testName).get(0);

		sugar().accounts.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();

		// Create Call and Meeting record with status 'Held', Task with 'Completed' and a Notes record so that they are appears on Historical Summary page
		myNotes = (NoteRecord) sugar().notes.api.create();
		FieldSet statusFS = new FieldSet();
		statusFS.put("status", statusData.get("callAndMeetingStatus"));
		myCall = (CallRecord) sugar().calls.api.create(statusFS);
		myMeeting = (MeetingRecord) sugar().meetings.api.create(statusFS);
		statusFS.clear();
		statusFS.put("status", statusData.get("taskStatus"));
		myTask = (TaskRecord) sugar().tasks.api.create(statusFS);

		// Login to Sugar
		sugar().login();

		// Account record with some calls, meetings, tasks and notes related to the account
		String calls = sugar().calls.moduleNamePlural;
		String meetings = sugar().meetings.moduleNamePlural;
		String tasks = sugar().tasks.moduleNamePlural;
		String notes = sugar().notes.moduleNamePlural;
		String contacts = sugar().contacts.moduleNamePlural;

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(calls).linkExistingRecord(myCall);
		sugar().accounts.recordView.subpanels.get(meetings).linkExistingRecord(myMeeting);

		// Need to scroll so that link the notes records
		sugar().accounts.recordView.subpanels.get(contacts).scrollIntoViewIfNeeded(false); 
		sugar().accounts.recordView.subpanels.get( tasks).linkExistingRecord(myTask);
		sugar().accounts.recordView.subpanels.get(notes).linkExistingRecord(myNotes);

		// Contact is related to some or all of those records related to the account
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.subpanels.get(calls).linkExistingRecord(myCall);
		sugar().contacts.recordView.subpanels.get(meetings).linkExistingRecord(myMeeting);

		// Need to scroll so that link the notes records
		sugar().contacts.recordView.subpanels.get(contacts).scrollIntoViewIfNeeded(false); 
		sugar().contacts.recordView.subpanels.get(tasks).linkExistingRecord(myTask);
		sugar().contacts.recordView.subpanels.get(notes).linkExistingRecord(myNotes);
	}

	/** Verify that related contact is a link to corresponding contact record view in the Historical Summary page
	 * @throws Exception
	 */
	@Test
	public void Contacts_26704_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open the account record created in the setup in the record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Open Historical Summary page by click Historical Summary menu item under Actions dropdown in the account record view
		sugar().accounts.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-965
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();

		//  Observe the 'Related Contact' column
		// Verify "Related Contact" column contains the contact Name which is a link.
		String contactName = sugar().contacts.getDefaultData().get("fullName");
		VoodooControl noteRelatedRecord = new VoodooControl("a", "css", ".layout_Accounts.active .single td:nth-child(7) a");
		for(int i = 1; i < 5; i++) {
			new VoodooControl("a", "css", ".layout_Accounts.active .single:nth-child("+i+") td:nth-child(7) a").assertEquals(contactName, true);
		}

		// Click on the contact's name
		// TODO: VOOD-1946
		new VoodooControl("i", "css", ".layout_Accounts.active .btn.btn-invisible.sidebar-toggle i").click();
		noteRelatedRecord.click();
		VoodooUtils.waitForReady();

		// Verify that the corresponding record view of the contact is displayed
		sugar().contacts.recordView.assertVisible(true);
		sugar().contacts.recordView.getDetailField("fullName").assertContains(contactName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}