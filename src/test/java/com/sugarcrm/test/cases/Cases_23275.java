package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Cases_23275 extends SugarTest {
	public void setup() throws Exception {
		FieldSet customData = testData.get(testName).get(0);
		sugar().cases.api.create();
		sugar().login();

		// Go to Cases Record view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Add the Active task dashboard to RHS pane of case detail view
		sugar().cases.dashboard.clickCreate();
		sugar().cases.dashboard.getControl("title").set(customData.get("dashboard_name"));
		sugar().cases.dashboard.addRow();
		sugar().cases.dashboard.addDashlet(1,1);

		// TODO: VOOD-960 - Dashlet selection 
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("input", "css", ".span4.search").set(customData.get("custom_dashboard"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#drawers .fld_save_button a:not(.hide)").click();
		VoodooUtils.waitForReady();
		sugar().cases.dashboard.save();
	}

	/**
	 * Create Task_Verify that a task for case can be created in Active Tasks dashlet.
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23275_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Active tasks dashlet
		// TODO: VOOD-960 - Dashlet selection 
		new VoodooControl("span", "css", ".row-fluid > li div span a span").click();
		new VoodooControl("a", "css", "[data-dashletaction='createRecord']").click();
		sugar().tasks.createDrawer.getEditField("subject").set(testName);
		sugar().tasks.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		// refresh the Dashlet
		new VoodooControl("a", "css", ".dashboard-pane  ul > li > ul > li .btn-group [title='Configure']").click();
		new VoodooControl("a", "css", "li > div li div [data-dashletaction='refreshClicked']").click();
		VoodooUtils.waitForReady();

		// Verify task is in Active-Task Dashlet (i.e in TODO tab, if we have no related date for that record)
		new VoodooControl("div", "css", "div[data-voodoo-name='active-tasks'] .dashlet-unordered-list .dashlet-tabs-row div.dashlet-tab:nth-of-type(3)").click();
		new VoodooControl("div", "css", "div.tab-pane.active p a:nth-of-type(2)").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}