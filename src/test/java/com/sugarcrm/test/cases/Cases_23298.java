package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Cases_23298 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Verify that call can be scheduled with special !@# /alt ¡™£¢ characters in Subject/description fields
	 * @throws Exception
	 */
	@Test
	public void Cases_23298_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet callsData = testData.get(testName).get(0);
		String myDashboard = callsData.get("myDashboard");
		String callName = callsData.get("name");

		// Go to Cases Record view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Check default dashboard availability
		// TODO: VOOD-960 - Dashlet selection And VOOD-963 - Some dashboard controls are needed
		VoodooControl dashboardTitle = sugar().cases.dashboard.getControl("dashboardTitle");
		if (!dashboardTitle.queryContains(myDashboard, true)) {
			sugar().dashboard.chooseDashboard(myDashboard);
		}

		// Log Call in a Planned Activities tab
		// TODO: VOOD-1305 - Dashlet: Planned Activities - Need lib support for RHS Planned Activities Dashlets
		new VoodooControl("a", "css", ".dashlet-row .dashlet-container .actions a").click();
		new VoodooControl("a", "css", "span li:nth-of-type(2) [data-dashletaction='createRecord']").click();

		// Enter Special and Alt Characters in Calls' name and description
		sugar().calls.createDrawer.getEditField("name").set(callName);
		sugar().calls.createDrawer.getEditField("description").set(callsData.get("description"));
		sugar().calls.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		// refresh the Dashlet
		new VoodooControl("button", "css", ".dashlet-cell.layout_Home .dashlet-header .btn-toolbar button.dropdown-toggle").click();
		new VoodooControl("a", "css", ".dashlet-cell.layout_Home .dashlet-header .btn-toolbar .btn-group.open [data-dashletaction='refreshClicked']").click();
		VoodooUtils.waitForReady();

		// Verify Special and Alt Characters are visible in Planned Dashlet under Calls tab
		new VoodooControl("div", "css", "div[data-voodoo-name='planned-activities'] .dashlet-unordered-list .dashlet-tabs-row div.dashlet-tab:nth-of-type(2)").click();
		new VoodooControl("div", "css", "div.tab-pane.active p a:nth-of-type(2)").assertEquals(callName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}