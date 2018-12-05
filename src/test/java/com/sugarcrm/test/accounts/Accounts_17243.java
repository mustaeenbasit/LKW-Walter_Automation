package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17243 extends SugarTest {
	AccountRecord account1;
	
	public void setup() throws Exception {
		sugar().login();
		account1 = (AccountRecord)sugar().accounts.api.create();
		account1.navToRecord();
	}

	/**
	 * Add comment to activity stream from record view
	 * @throws Exception
	 */
	@Test
	public void Accounts_17243_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.recordView.showActivityStream();
		DataSource ds = testData.get(testName);
		
		sugar().accounts.recordView.activityStream.createComment(ds.get(0).get("post"));
		sugar().accounts.recordView.activityStream.createReply((ds.get(0).get("comment")), 1);
		sugar().accounts.recordView.activityStream.assertCommentContains(ds.get(0).get("post"), 1, true);
		sugar().accounts.recordView.activityStream.assertReplyContains(ds.get(0).get("comment"), 1, 1, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
