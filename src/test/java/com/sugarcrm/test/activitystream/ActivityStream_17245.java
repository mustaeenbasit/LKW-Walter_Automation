package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class ActivityStream_17245 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Add comment to activity stream from home page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17245_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		ActivityStream stream = new ActivityStream();
		stream.createComment(ds.get(0).get("post"));
		stream.createReply(ds.get(0).get("comment"), 1);
		stream.assertReplyContains(ds.get(0).get("comment"), 1, 1, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}