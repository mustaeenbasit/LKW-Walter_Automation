package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class ActivityRecordTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void inviteRecordTests() throws Exception {
		VoodooUtils.voodoo.log.info("Running inviteRecordTests()...");

		ContactRecord myContact = (ContactRecord)sugar().contacts.api.create();
		CallRecord myCall = (CallRecord)sugar().calls.api.create();

		// Verify addInvitee with Person type as record
		myCall.addInvitee(myContact);
		VoodooControl inviteesCtrl = sugar().calls.recordView.getControl("invitees");
		inviteesCtrl.assertContains(myContact.getRecordIdentifier(), true);

		// Verify removeInvitee with Person type as record
		myCall.removeInvitee(myContact);
		inviteesCtrl.assertContains(myContact.getRecordIdentifier(), false);

		// Verify addInvitee with Person type as search string
		myCall.addInvitee(myContact.getRecordIdentifier());
		inviteesCtrl.assertContains(myContact.getRecordIdentifier(), true);

		// Verify removeInvitee with record by index
		myCall.removeInvitee(3);
		inviteesCtrl.assertContains(myContact.getRecordIdentifier(), false);

		VoodooUtils.voodoo.log.info("inviteRecordTests() complete.");
	}

	@Test
	public void deleteAllReocurrences() throws Exception {
		VoodooUtils.voodoo.log.info("Running deleteAllReocurrences()...");

		FieldSet repeatData = new FieldSet();
		repeatData.put("repeatType", "Daily");
		repeatData.put("repeatOccurType", "Occurrences");
		repeatData.put("repeatOccur", "5");
		repeatData.put("date_start_date", null);
		repeatData.put("date_end_date", null);
		repeatData.put("date_start_time", null);
		repeatData.put("date_end_time", null);

		MeetingRecord myMeeting = new MeetingRecord(repeatData);
		sugar().meetings.create(repeatData);
		sugar().meetings.listView.clickRecord(2);
		myMeeting.deleteAllReocurrences();
		sugar().alerts.getWarning().confirmAlert();

		// Verify no records in listview
		sugar().meetings.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info("deleteAllReocurrences() complete.");
	}

	@Test
	public void editAllReocurrences() throws Exception {
		VoodooUtils.voodoo.log.info("Running editAllReocurrences()...");

		FieldSet repeatData = new FieldSet();
		repeatData.put("repeatType", "Daily");
		repeatData.put("repeatOccurType", "Occurrences");
		repeatData.put("repeatOccur", "3");
		repeatData.put("date_start_date", null);
		repeatData.put("date_end_date", null);
		repeatData.put("date_start_time", null);
		repeatData.put("date_end_time", null);
		MeetingRecord myMeeting = new MeetingRecord(repeatData);
		sugar().meetings.create(repeatData);
		sugar().meetings.listView.clickRecord(2);
		myMeeting.editAllReocurrenses();
		sugar().alerts.waitForLoadingExpiration();

		// Verify we are on edit
		sugar().meetings.recordView.getEditField("name").set(testName);
		sugar().meetings.recordView.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify parent record is updated
		sugar().meetings.recordView.getDetailField("name").assertEquals(testName, true);

		// Verify child records are also updated
		sugar().meetings.navToListView();
		sugar().meetings.listView.verifyField(1, "name", testName);
		sugar().meetings.listView.verifyField(2, "name", testName);
		sugar().meetings.listView.verifyField(3, "name", testName);

		VoodooUtils.voodoo.log.info("editAllReocurrences() complete.");
	}

	public void cleanup() throws Exception {}
}