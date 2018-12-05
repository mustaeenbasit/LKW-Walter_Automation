package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_17555 extends SugarTest{
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar.login();
		myAccount = (AccountRecord)sugar.accounts.api.create();
	}
	
	/**
	 * Verify the posts show which record they were made on
	 * @author Eric Yang
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17555_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myAccount.createComment("post_testcase17555");
		// The below two lines are verifying the post contains " on (account name)." where "(account name)" is a link the account being posted on.
		myAccount.assertCommentContains("on "+ myAccount.getRecordIdentifier(), 1, true);
		new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type(1) div .tagged a").assertContains(myAccount.getRecordIdentifier(), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}