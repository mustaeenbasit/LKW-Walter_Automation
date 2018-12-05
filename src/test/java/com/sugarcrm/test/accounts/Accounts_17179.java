package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Accounts_17179 extends SugarTest {
	AccountRecord myAccount;
	DataSource comments;
	
	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}

	/**
	 * Add comment to activity stream from record detail view
	 * @throws Exception
	 */
	@Test
	public void Accounts_17179_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		comments = testData.get("Accounts_17179");

		myAccount.navToRecord();
		sugar().accounts.recordView.showActivityStream();
		
		// ABE-587 - double quote is not working in comment/activity stream yet.
		for(FieldSet comment : comments) {	
			// Enter an activity stream
			myAccount.createComment(comment.get("activityStream"));			

			// Verify the last posted activity stream
			myAccount.assertCommentContains(comment.get("activityStream"), 1, true);
			
			// Enter a comment/reply
			myAccount.createReply(comment.get("replyComment"), 1);

			// Verify the replied comment
			myAccount.assertReplyContains(comment.get("replyComment"), 1, 1, true);
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}