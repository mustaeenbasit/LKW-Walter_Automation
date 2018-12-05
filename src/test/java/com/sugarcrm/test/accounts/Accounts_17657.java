package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17657 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that google news dashlet can be successfully edit on record view
	 * @throws Exception 
	 */
	@Test
	public void Accounts_17657_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create new Dashboard
		FieldSet dashletData = testData.get(testName).get(0);
		String dashBoardName = dashletData.get("dashboardTitle");
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(dashBoardName);
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1,1);

		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("input", "css", ".span4.search").set(dashletData.get("dashletName"));
		new VoodooControl("a", "css", ".dataTable tr:nth-child(2) a").click();
		sugar().accounts.createDrawer.save();
		sugar().accounts.dashboard.save();

		// Verify title of new dashboard
		sugar().accounts.dashboard.getControl("dashboardTitle").assertElementContains(dashBoardName, true);

		// To check if dashlet is added to the dashboard. 
		sugar().accounts.dashboard.getControl("firstDashlet").assertVisible(true);

		// Assert dashlet title 
		new VoodooControl("h4", "css", ".dashlet-title").assertEquals("News", true);

		// TODO: VOOD-670 - More Dashlet Support
		new VoodooControl("div", "css", "div.dashlet-content div").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}