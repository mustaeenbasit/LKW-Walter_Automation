package com.sugarcrm.test.meetings;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Meetings_29522 extends SugarTest {
	LeadRecord leadRecord;

	public void setup() throws Exception {
		leadRecord = (LeadRecord)sugar().leads.api.create();
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that correct number of repeat meeting is created when create from subpanel
	 * @throws Exception
	 */
	@Test
	public void Meetings_29522_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet meetingData = testData.get(testName).get(0);
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		StandardSubpanel meetingSub = sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);

		// Create Daily Meeting Record having 3 occurrences
		meetingSub.create(meetingData);
		meetingSub.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.showMore();

		// Add one Lead in Guests field.
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(leadRecord);
		sugar().meetings.recordView.save();

		// Navigate to Contact Record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		meetingSub.scrollIntoView();

		// Verify 3 Meetings are available in the Subpanel
		int meetingRecords = Integer.parseInt(meetingData.get("repeatOccur"));
		String meetingName = meetingData.get("name");
		Assert.assertTrue("Total no. of rows not equal to 3", meetingRecords == meetingSub.countRows());

		// Verify 3 Meetings with same name are available in Meetings Subpanel
		for(int i = 1; i <= meetingRecords; i++) {
			meetingSub.getDetailField(i, "name").assertEquals(meetingName, true);
		}

		// Verify 3 Meetings are available in ListView of Meetings
		sugar().meetings.navToListView();
		Assert.assertEquals(meetingRecords, sugar().meetings.listView.countRows());

		// Verify 3 meetings with same name are available in ListView
		for(int i = 1; i <= meetingRecords; i++) {
			sugar().meetings.listView.getDetailField(i, "name").assertEquals(meetingName, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}