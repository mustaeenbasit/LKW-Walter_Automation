package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_28901 extends SugarTest {

	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Verify Cancel link should be working at Archive Email drawer of Cases detail view -> History Dashlet
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_28901_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to any Case record view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Choose My Dashboard
		VoodooControl dashboard = sugar().accounts.dashboard.getControl("dashboard");
		if(!dashboard.queryContains("My Dashboard", true))
			sugar().cases.dashboard.chooseDashboard("My Dashboard");

		// Archived Email
		// TODO: VOOD-798. Lib support in create/verify Archive Email from History Dashlet
		VoodooControl historyDashletCreateCtrl = new VoodooControl("a", "css", ".dashlet-row li.row-fluid.sortable:nth-child(2) span.btn-group.dashlet-toolbar a");
		historyDashletCreateCtrl.waitForVisible();
		historyDashletCreateCtrl.click();

		// At My Dashboard of RHS, Click on "+" sign to Archive Email action in "History" dashlet.
		new VoodooControl("a", "css", "div.thumbnail.dashlet.ui-draggable a[data-dashletaction='archiveEmail']").click();
		VoodooUtils.waitForReady();

		// At Archive Email drawer, Click on "Cancel" link
		new VoodooControl("a", "css", "div.headerpane div.btn-toolbar.pull-right:nth-child(2) span:nth-child(1) a").click();

		// Verify that Cancel link should be working. After click on cancel link, Archive Email drawer should be closed and should be redirect on Case detail view
		sugar().cases.recordView.assertExists(true);
		sugar().cases.recordView.getDetailField("name").assertEquals(sugar().cases.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
