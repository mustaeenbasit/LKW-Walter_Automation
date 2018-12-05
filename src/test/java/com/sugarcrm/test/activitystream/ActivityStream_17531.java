package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_17531 extends SugarTest {
	AccountRecord myAccount;
	ContactRecord myContact;

	public void setup() throws Exception {
		myContact = (ContactRecord) sugar().contacts.api.create();
		myAccount = (AccountRecord) sugar().accounts.api.create();

		// Login as admin user
		sugar().login();
	}

	/**
	 * comments of pinned with the record should displayed in pinned record's detail view
	 * 
	 * @author Eric Yang
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17531_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet activityStreamCommentData = testData.get(testName).get(0);

		// Open activity stream of Home Page
		sugar().navbar.selectMenuItem(sugar().home, "activityStream"); 

		// Use # sign to pin an account record in comment and post it
		ActivityStream stream = new ActivityStream();
		String commentText = testName + activityStreamCommentData.get("comment") + myAccount.getRecordIdentifier();
		stream.getControl("streamInput").set(commentText);
		// TODO: VOOD-711
		VoodooControl selectSearchRecordCtrl = new VoodooControl("div", "css", ".label.label-module-mini.label-Accounts.pull-left");
		selectSearchRecordCtrl.waitForVisible();
		selectSearchRecordCtrl.click();
		stream.clickSubmit();

		// Open the pinned account record's detail view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Go to activity stream
		sugar().accounts.recordView.showActivityStream();

		// Verify that the activity stream should show the comment posted
		String commentString = testName + activityStreamCommentData.get("comment").substring(0, (activityStreamCommentData.get("comment").length() - 1)) + myAccount.getRecordIdentifier(); 
		stream.assertCommentContains(commentString, 1, true);

		// Open activity stream of Contacts record view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.showActivityStream();

		// Use # sign to pin an account record in comment and post it
		sugar().contacts.recordView.activityStream.getControl("streamInput").set(commentText);
		selectSearchRecordCtrl.waitForVisible();
		selectSearchRecordCtrl.click();
		sugar().contacts.recordView.activityStream.clickSubmit();

		// Open the pinned account record's detail view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Go to activity stream
		sugar().accounts.recordView.showActivityStream();

		// Verify that the activity stream should show the comment posted
		sugar().contacts.listView.activityStream.assertCommentContains(commentString + activityStreamCommentData.get("on") + myContact.get("fullName"), 1, true);

		// Open activity stream of Contacts list view
		sugar().contacts.navToListView();
		sugar().contacts.listView.showActivityStream();

		// Use # sign to pin an account record in comment and post it
		sugar().contacts.listView.activityStream.getControl("streamInput").set(commentText);
		selectSearchRecordCtrl.waitForVisible();
		selectSearchRecordCtrl.click();
		sugar().contacts.listView.activityStream.clickSubmit();

		// Open the pinned account record's detail view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Go to activity stream
		sugar().accounts.recordView.showActivityStream();

		// Verify that the activity stream should show the comment posted
		sugar().contacts.listView.activityStream.assertCommentContains(commentString + activityStreamCommentData.get("on") + sugar().contacts.moduleNameSingular, 1, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}