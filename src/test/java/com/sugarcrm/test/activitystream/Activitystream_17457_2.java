package com.sugarcrm.test.activitystream;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Activitystream_17457_2 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Show tip message in the comments input box
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void Activitystream_17457_2_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		String tip = ds.get(0).get("tip");
		VoodooUtils.voodoo.log.info("Check tip message on module list");
		sugar.accounts.navToListView();
		sugar.accounts.listView.showActivityStream();
		sugar.alerts.waitForLoadingExpiration();
		// The following checks the tool tip string in the Activity Stream input
		// field is correct.
		sugar.accounts.listView.activityStream.getControl("streamInput").assertAttribute("data-placeholder", tip);
		sugar.accounts.listView.activityStream.createComment(ds.get(0).get("post"));
		sugar.alerts.waitForLoadingExpiration();
		// The use of sugar.accounts. is because we do not have a
		// HomeModule.java file for support
		// When HomeModule.java is available then the following lines should be
		// updated to use it.
		sugar.accounts.listView.activityStream.assertCommentContains(ds.get(0).get("post"), 1, true);

		// Restore 
		new VoodooControl("button", "css", "li[data-module='Home'] button[data-toggle='dropdown']").click();
		new VoodooControl("a", "xpath", "//li[@data-module='Home']/span/div/ul/li[contains(.,'My Dashboard')]/a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}