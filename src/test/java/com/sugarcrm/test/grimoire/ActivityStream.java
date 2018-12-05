package com.sugarcrm.test.grimoire;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.UserRecord;

public class ActivityStream extends SugarTest {
	AccountRecord myAccount;
	UserRecord qauser;

	public void setup() throws Exception {
		qauser = new UserRecord(sugar().users.getQAUser());
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	@Test
	public void ActivityStream_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		sugar().accounts.navToListView();

		// Create an Activity Stream in Accounts ListView
		sugar().accounts.listView.showActivityStream();
		sugar().accounts.listView.activityStream.createComment("This is the First Entry on the ListView");
		sugar().accounts.listView.activityStream.createComment("This is the Second Entry on the ListView");

		// Verify Activity Stream Comment content
		sugar().accounts.listView.activityStream.assertCommentContains("This is the First Entry on the ListView", 2, true); // Assert String does exist in comment
		sugar().accounts.listView.activityStream.assertCommentContains("This is the Second Entry on the ListView", 1, true); // Assert String does exist in comment
		sugar().accounts.listView.activityStream.assertCommentContains("This is the First Entry on the ListView", 1, false); // Assert String does not exist in comment
		sugar().accounts.listView.activityStream.assertCommentContains("This is the Second Entry on the ListView", 2, false); // Assert String does not exist in comment

		// Create 2 replies to the Activity Stream Comment
		sugar().accounts.listView.activityStream.createReply("This is my First Reply to my Second Entry on the ListView", 1);
		sugar().accounts.listView.activityStream.createReply("This is my First Reply to my First Entry on the ListView", 2);

		// Verify Each Reply's content
		sugar().accounts.listView.activityStream.assertReplyContains("This is my First Reply to my First Entry on the ListView", 1, 1, true); // Should contain string
		sugar().accounts.listView.activityStream.assertReplyContains("This is my First Reply to my Second Entry on the ListView", 1, 1, false); // Should not contain string

		sugar().accounts.listView.activityStream.assertReplyContains("This is my First Reply to my First Entry on the ListView", 2, 1, false); // Should not contain string
		sugar().accounts.listView.activityStream.assertReplyContains("This is my First Reply to my Second Entry on the ListView", 2, 1, true); // Should contain string

		sugar().accounts.listView.showListView();

		// Activity Stream from RecordView
		// Create 2 comments on the Record
		myAccount.createComment("I think I just made a comment from the Record view");
		myAccount.createComment("Another comment from the Record view");

		// Assert Comments
		myAccount.assertCommentContains("I think I just", 2, true);
		myAccount.assertCommentContains("Another comment", 1, true);

		// Create a reply for each comment
		myAccount.createReply("This is the reply I am making to my First comment from the Record view", 1);
		myAccount.createReply("Another reply I made", 2);

		// Assert each reply on each comment
		myAccount.assertReplyContains("This is the reply", 2, 1, true);
		myAccount.assertReplyContains("This is the reply", 1, 1, false);
		myAccount.assertReplyContains("Another reply I made", 1, 1, true);
		myAccount.assertReplyContains("Another reply I made", 2, 1, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
