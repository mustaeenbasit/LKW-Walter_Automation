package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class ActivityStream_17817 extends SugarTest {
	AccountRecord myAcc;
	
	public void setup() throws Exception {
		sugar.login();
		myAcc = (AccountRecord) sugar.accounts.api.create();
	}

	/**
	 * Verify symbol like apostrophe is encoded correctly in comment
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17817_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		myAcc.navToRecord();
		sugar.accounts.recordView.showActivityStream();
		sugar.accounts.recordView.activityStream.createComment(ds.get(0).get("post"));
		sugar.accounts.recordView.activityStream.createReply(ds.get(0).get("comment"), 1);
		
		// TODO VOOD-969
		new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type(1) .comments li:nth-of-type(1) .tagged").waitForVisible();
		sugar.accounts.recordView.activityStream.assertReplyContains(ds.get(0).get("comment"), 1, 1, true);	
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}