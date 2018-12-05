package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17242 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.navToListView();
	}

	/**
	 * Add post to activity stream from module list view
	 * @throws Exception
	 */
	@Test
	public void Accounts_17242_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.listView.showActivityStream();
		DataSource ds = testData.get(testName);
		
		sugar().accounts.listView.activityStream.createComment(ds.get(0).get("post"));
		sugar().accounts.recordView.activityStream.assertCommentContains(ds.get(0).get("post"), 1, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
