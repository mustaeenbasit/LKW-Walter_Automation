package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20358 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that call record is displayed in "Planned Activities" Dashlet of "Leads" Record view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20358_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to leads record view
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Choose dashboard by name
		FieldSet customFS = testData.get(testName).get(0);
		VoodooControl dashboard = sugar().leads.dashboard.getControl("dashboard");
		if(!dashboard.queryContains(customFS.get("myDashboard"), true))
			sugar().leads.dashboard.chooseDashboard(customFS.get("myDashboard"));

		// Archived Email
		// TODO: VOOD-798. Lib support in create/verify Archive Email from History Dashlet
		VoodooControl historyDashletCreateCtrl = new VoodooControl("span", "css", ".dashboard .dashlet-row li .layout_Home .fa.fa-plus");
		historyDashletCreateCtrl.waitForVisible();
		historyDashletCreateCtrl.click();
		new VoodooControl("a", "css", ".ui-draggable .dashlet-header ul.dropdown-menu li:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// Fill in all required fields > click "Save" button.
		sugar().calls.createDrawer.getEditField("name").set(testName);
		String callTime = sugar().calls.getDefaultData().get("date_start_time");
		sugar().calls.createDrawer.getEditField("date_start_time").set(callTime);
		sugar().calls.createDrawer.save();

		// TODO: VOOD-798. Lib support in create/verify Archive Email from History Dashlet
		// Verify that the scheduled call record is displayed in "Planned Activities" Dashlet.
		new VoodooControl("a", "css", "[data-voodoo-name='planned-activities'] .dashlet-tabs-row div:nth-child(2) a").click();
		new VoodooControl("p", "css", "[data-voodoo-name='planned-activities'] .tab-pane.active .unstyled.listed p").assertContains(testName, true);

		// Go to calendar 
		sugar().calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-863
		// Verify call is displayed on Calendar.
		new VoodooControl("div", "css", ".week div:nth-child(3) div[time='" + callTime + "']").assertExists(true);
		VoodooUtils.focusDefault();
		
		// Go to leads record view
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		
		// TODO: VOOD-1424 -Make StandardSubpanel.verify() verify specified value is in correct column.
		// Verify that the call is displayed in the built-in Calls subpanel for lead record.
		StandardSubpanel callSubPanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callSubPanel.expandSubpanel();
		callSubPanel.getDetailField(1, "name").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}