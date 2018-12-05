package com.sugarcrm.test.activitystream;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_29703 extends SugarTest {
	MeetingRecord myMeeting;

	public void setup() throws Exception {
		// Create an Account Record and Contacts record
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		myMeeting = (MeetingRecord) sugar().meetings.api.create();

		// Login as Admin user
		sugar().login();
	}

	/**
	 * Verify that Activity Stream in the 'Record' view of a record should show proper messages.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_29703_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Now go to any Contacts record view, click action drop-down of Meetings -> Link Existing Record -> select few records from the list
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		StandardSubpanel meetingsSubpanelCtrl = sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanelCtrl.linkExistingRecord(myMeeting);

		// Verify that the User would find selected records getting linked to the corresponding sub-panel
		meetingsSubpanelCtrl.getDetailField(1, "name").assertEquals(myMeeting.getRecordIdentifier(), true);

		// Now go to Activity Stream view
		sugar().contacts.recordView.showActivityStream();

		FieldSet activityStreamMessageData = testData.get(testName).get(0);

		// Verify that the User must see linked activities showing up in the Activity stream view
		sugar().contacts.recordView.activityStream.assertCommentContains(activityStreamMessageData.get("linked") + " " + sugar().contacts.getDefaultData().get("fullName") + " " + activityStreamMessageData.get("to") + " " + myMeeting.getRecordIdentifier() + "." , 1, true);
		sugar().contacts.recordView.activityStream.assertCommentContains(activityStreamMessageData.get("created") + " " + sugar().contacts.getDefaultData().get("fullName") + " " + sugar().contacts.moduleNameSingular + "." , 2, true);

		// Now select Messages for Link from the filter drop-down
		// TODO: VOOD-474
		new VoodooControl("a", "css", ".filter-view.search .table-cell:nth-child(2) .search-filter a").click();
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-child(3)").click();
		VoodooUtils.waitForReady();

		// Verify that the user must see linked activities showing up in the Activity stream view
		sugar().contacts.recordView.activityStream.assertCommentContains(activityStreamMessageData.get("linked") + " " + sugar().contacts.getDefaultData().get("fullName") + " " + activityStreamMessageData.get("to") + " " + myMeeting.getRecordIdentifier() + "." , 1, true);
		// TODO: VOOD-474
		VoodooControl activityStreamMessageRowCtrl = new VoodooControl("li", "css", ".activitystream-list.results .activitystream-posts-comments-container");
		Assert.assertTrue("Other then linked activity messages are shoilng up", activityStreamMessageRowCtrl.countWithClass() == 1);

		// Remove Messages for Link filter
		// TODO: VOOD-474
		new VoodooControl("i", "css", ".filter-view.search .table-cell:nth-child(2) .choice-filter .choice-filter-close i").click();
		VoodooUtils.waitForReady();

		// Verify that the user must see all the activities related to the record showing up in the Activity stream view
		sugar().contacts.recordView.activityStream.assertCommentContains(activityStreamMessageData.get("linked") + " " + sugar().contacts.getDefaultData().get("fullName") + " " + activityStreamMessageData.get("to") + " " + myMeeting.getRecordIdentifier() + "." , 1, true);
		sugar().contacts.recordView.activityStream.assertCommentContains(activityStreamMessageData.get("created") + " " + sugar().contacts.getDefaultData().get("fullName") + " " + sugar().contacts.moduleNameSingular + "." , 2, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}