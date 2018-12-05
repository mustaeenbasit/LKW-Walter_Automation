package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class DashboardTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyDashboard() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyDashboard()...");

		// Create Dashboard from Home screen
		sugar().home.dashboard.clickCreate();
		sugar().home.dashboard.getControl("title").set("New Dashboard");
		sugar().home.dashboard.setColumnsPerRow(3);
		sugar().home.dashboard.addDashlet(1, 3);
		// Cancel Dashlet add via new VoodooControl
		// TODO: Once VOOD-591 is completed adding dashlets can be accomplished or canceled
		new VoodooControl("a","css","#drawers a[name='cancel_button']").click();
		sugar().home.dashboard.save();

		// Delete Dashboard from Home screen
		sugar().home.dashboard.edit();
		sugar().home.dashboard.delete();
		sugar().alerts.getAlert().confirmAlert();

		// Create Dashboard from Accounts Module ListView
		sugar().accounts.navToListView();
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set("My new Accounts");
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.save();

		// Verify title of new dashboard
		sugar().accounts.dashboard.getControl("dashboardTitle").assertElementContains("My new Accounts", true);

		// Switch between dashboards - switch to default Accounts dashboard
		sugar().accounts.dashboard.chooseDashboard(2);

		// Verify switch
		sugar().accounts.dashboard.getControl("dashboardTitle").assertElementContains("My Dashboard", true);

		// Switch back to new created dashboard
		sugar().accounts.dashboard.chooseDashboard("My new Accounts");
		sugar().accounts.dashboard.getControl("dashboardTitle").assertElementContains("My new Accounts", true);

		// Delete newly created dashboard
		sugar().accounts.dashboard.edit();
		sugar().accounts.dashboard.delete();
		sugar().alerts.getAlert().confirmAlert();

		// Verify dashboard is deleted
		sugar().accounts.dashboard.getControl("dashboardTitle").assertElementContains("My new Accounts", false);

		VoodooUtils.voodoo.log.info("verifyDashboard() complete.");
	}

	public void cleanup() throws Exception {}
}