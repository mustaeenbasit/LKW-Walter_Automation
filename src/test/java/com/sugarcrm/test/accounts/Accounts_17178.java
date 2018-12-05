package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17178 extends SugarTest {
	AccountRecord myAccount;
	FieldSet customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Add post to activity stream on record view
	 * @throws Exception
	 */
	@Test
	public void Accounts_17178_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myAccount.navToRecord();
		sugar().accounts.recordView.showActivityStream();
		sugar().accounts.recordView.activityStream.createComment(customData.get("post"));
		sugar().accounts.recordView.activityStream.waitForVisible(8000);
		
		// Verify that the post should show up in the activity stream
		sugar().accounts.recordView.activityStream.assertCommentContains(customData.get("post"), 1, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}