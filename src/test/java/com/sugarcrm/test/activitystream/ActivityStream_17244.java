package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class ActivityStream_17244 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Add post to activity stream from home page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17244_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		ActivityStream stream = new ActivityStream();
		for(int i=0;i<ds.size();i++) {
			stream.createComment(ds.get(i).get("post"));
			stream.assertCommentContains(ds.get(i).get("post"), 1, true);
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}