package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_17556 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar.login();
		myAccount = (AccountRecord) sugar.accounts.api.create();
	}

	/**
	 * Verify refresh the activity stream on the record changes
	 * 
	 * @author Eric Yang
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17556_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();
		sugar.accounts.recordView.showActivityStream();

		// The two below lines are verifying the post contains
		// " on (account name)." where "(account name)" is a link the account
		// being posted on.
		myAccount.assertCommentContains(
				"Created " + myAccount.getRecordIdentifier() + " Account.", 1,
				true);
		new VoodooControl("span", "css",
				".activitystream-list.results li:nth-of-type(1) div .tagged a")
				.assertContains(myAccount.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}