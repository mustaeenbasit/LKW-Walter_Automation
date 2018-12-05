package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29637 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that initial state of "Usefulness for Article" Dashlet should be no data
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29637_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB modules and Create a KB
		// Create a KB record via UI, as we need to check in RHS-dashlet pane
		sugar().knowledgeBase.create();

		// Select the KB and go to record view
		sugar().knowledgeBase.listView.clickRecord(1);

		// Select My Dashboard from RHS pane
		FieldSet dashboardData = testData.get(testName).get(0);
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(dashboardData.get("dashboardTitle"), true))
			sugar().dashboard.chooseDashboard(dashboardData.get("dashboardTitle"));

		// Verify that Initially 'Usefulness for Articles' dashlet shows 'No data available' & others dashlet as well
		// TODO: VOOD-960
		new VoodooControl("div", "css", ".row-fluid.sortable .dashlet-container .block-footer").assertEquals(dashboardData.get("noDataAvailable"), true);
		new VoodooControl("div", "css", ".row-fluid.sortable:nth-of-type(2) .dashlet-container .block-footer").assertEquals(dashboardData.get("noDataAvailable"), true);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}