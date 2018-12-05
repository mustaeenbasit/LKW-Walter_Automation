package com.sugarcrm.test.activitystream;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class ActivityStream_17352 extends SugarTest {
	DataSource accountData;

	public void setup() throws Exception {
		sugar.login();
		accountData = testData.get("ActivityStream_17352_Accounts");
		sugar.accounts.api.create(accountData.get(0));
	}

	/**
	 * should be able to preview a record from an activity stream message(from
	 * module list view)
	 * 
	 * @author Eric Yang
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17352_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.accounts.navToListView();

		// Verify the created account post is existing in activity stream list
		sugar.accounts.listView.showActivityStream();
		sugar.accounts.listView.activityStream.assertCommentContains(accountData.get(0).get("name"), 1, true);

		// Verify the preview
		// TODO The JIRA story VOOD-454 which deals with Lib Support for the
		// preview pane
		sugar.accounts.listView.activityStream.togglePreviewButton(1);
		new VoodooControl("span", "css", ".preview-pane.active .fld_name.detail")
				.assertContains(accountData.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
