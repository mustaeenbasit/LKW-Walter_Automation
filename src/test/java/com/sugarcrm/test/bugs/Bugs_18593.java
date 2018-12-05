package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Bugs_18593 extends SugarTest {
	DataSource bugs;

	public void setup() throws Exception {
		bugs = testData.get("Bugs_18593");
		sugar.login();
		sugar.bugs.api.create(bugs);
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * Detail View_Verify that the pagination control links is displayed at the
	 * top right corner of bug detail view after canceling editing bug.
	 */
	@Test
	public void Bugs_18593_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Bugs record view
		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);
		sugar.bugs.recordView.edit();
		sugar.bugs.recordView.cancel();

		// TODO: JIRA story VOOD-622 about the ability to access previous and
		// next arrow in record view.
		// Verify the The pagination control links are displayed at the top
		// right corner of bug detail view
		new VoodooControl("a", "css",
				".btn.btn-invisible.previous-row.disabled").assertExists(true);
		new VoodooControl("a", "css", ".btn.btn-invisible.next-row")
				.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
