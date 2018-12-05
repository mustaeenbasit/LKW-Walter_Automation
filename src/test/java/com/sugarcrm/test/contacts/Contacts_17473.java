package com.sugarcrm.test.contacts;
import org.junit.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Contacts_17473 extends SugarTest {
	CallRecord myCall;
	MeetingRecord myMeeting;
	StandardSubpanel callSubpanel, meetingSubpanel;

	public void setup() throws Exception {
		sugar().contacts.api.create();
		myCall = (CallRecord) sugar().calls.api.create();
		myMeeting = (MeetingRecord) sugar().meetings.api.create();
		sugar().login();

		// Relate a call and meeting to a contact
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		callSubpanel = sugar().contacts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callSubpanel.linkExistingRecord(myCall);
		meetingSubpanel = sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingSubpanel.linkExistingRecord(myMeeting);
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.closeAllSuccess();
	}

	/**
	 * Verify user can search records in Contacts sub panel
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_17473_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that search record listed in the Calls subpanel but not in Meeting subpanel (i.e call record only)
		sugar().contacts.recordView.setSearchString(myCall.get("name"));
		VoodooUtils.waitForReady();
		FieldSet fs = new FieldSet();
		fs.put("name", myCall.get("name"));
		callSubpanel.verify(1, fs, true);
		Assert.assertTrue("Number of rows did not equal zero in meetings subpanel.", meetingSubpanel.isEmpty());
		fs.clear();

		// Verify that search record listed in the Meetings subpanel but not in Calls subpanel (i.e meeting record only)
		sugar().contacts.recordView.setSearchString(myMeeting.get("name"));
		VoodooUtils.waitForReady();
		fs.put("name", myMeeting.get("name"));
		meetingSubpanel.verify(1, fs, true);
		Assert.assertTrue("Call subpanel is not empty.", callSubpanel.isEmpty());

		// Verify that search record listed in the Calls as well as in Meeting subpanel (i.e initial search string is same in both records)
		sugar().contacts.recordView.setSearchString("A");
		VoodooUtils.waitForReady();
		int row = callSubpanel.countRows();
		Assert.assertTrue("Number of rows not equals one for Calls subpanel.", row == 1);
		row = meetingSubpanel.countRows();
		Assert.assertTrue("Number of rows not equals one for Meetings subpanel.", row == 1);

		// Verify that search record not listed in calls & meetings subpanels (i.e No record display)
		sugar().contacts.recordView.setSearchString(testName);
		VoodooUtils.waitForReady();
		Assert.assertTrue("Call subpanel is not empty.", callSubpanel.isEmpty());
		Assert.assertTrue("Meeting subpanel is not empty.", meetingSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
