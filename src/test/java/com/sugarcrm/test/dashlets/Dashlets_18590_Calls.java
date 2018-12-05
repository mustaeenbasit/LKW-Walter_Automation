package com.sugarcrm.test.dashlets;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Dashlets_18590_Calls extends SugarTest {
	public void setup() throws Exception {
		// bugs data record via API
		sugar().bugs.api.create();
		sugar().login();

		// enable Bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
	}

	/**
	 * Verify history and activities dashlet is displaying calls correctly according to its status
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_18590_Calls_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource callsDS = testData.get(testName);

		// Bugs record view and create calls with status = Held, Scheduled, Canceled 
		sugar().bugs.navToListView();
		sugar().bugs.listView.clickRecord(1);
		StandardSubpanel callSubpanel = sugar().bugs.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		VoodooControl name = sugar().calls.createDrawer.getEditField("name");
		VoodooSelect status = (VoodooSelect)sugar().calls.createDrawer.getEditField("status");
		for (int i = 0; i < callsDS.size(); i++) {
			callSubpanel.addRecord();
			name.set(callsDS.get(i).get("name"));
			status.set(callsDS.get(i).get("status"));
			sugar().calls.createDrawer.save();
			sugar().alerts.getSuccess().closeAlert();
		}

		// TODO: VOOD-963
		VoodooControl dashboardTitle = sugar().bugs.dashboard.getControl("dashboard");			
		FieldSet customData = testData.get(testName+"_custom").get(0);

		// Select My Dashboard, if Help dashboard 
		if(!dashboardTitle.queryContains(customData.get("my_dashboard"), true))
			sugar().dashboard.chooseDashboard(customData.get("my_dashboard"));

		// TODO: VOOD-1305
		// Verify only Scheduled call is in Planned Activity dashlet
		new VoodooControl("div", "css", ".dashlet-tabs.tab2 [class=dashlet-tab]").click();
		new VoodooControl("a", "css", ".dashlets li.row-fluid:nth-of-type(1) .tab-pane.active a:nth-of-type(2)").assertEquals(callsDS.get(0).get("name"), true);
		Assert.assertTrue("Calls in Planned Activity dashlet not equal one", new VoodooControl("li", "css", ".dashlets li.row-fluid:nth-of-type(1) .tab-pane.active li").count() == 1);

		// TODO: VOOD-814
		// No suitable control found on page, index position is the only way to assert values
		// Verify Held and Cancelled calls in History dashlet
		new VoodooControl("div", "css", "div[data-voodoo-name=history] .dashlet-tabs .dashlet-tab:nth-of-type(3)").click();
		VoodooControl historyDashlet = new VoodooControl("a", "css", "div[data-voodoo-name=history] .tab-pane.active ul");
		historyDashlet.assertContains(callsDS.get(1).get("name"), true);
		historyDashlet.assertContains(callsDS.get(2).get("name"), true);
		Assert.assertTrue("Calls in history dashlet not equal two", new VoodooControl("a", "css", "div[data-voodoo-name=history] .tab-pane.active ul li").count() == 2);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}