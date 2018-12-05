package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Cases_23285 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * TC 23285: Scheduled Meeting_Verify that a related meeting can be scheduled for case in Planned Activity dashlet
	 * @throws Exception
	 */
	@Test
	public void Cases_23285_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource meetDS = testData.get(testName);
		// Open case record view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Choosing My Dashboard 
		sugar().dashboard.chooseDashboard("My Dashboard");

		// Click "Schedule Meeting" in a Planned Activities tab
		// TODO: VOOD-1305 - Dashlet: Planned Activities - Need lib support for RHS Planned Activities Dashlets
		new VoodooControl("a", "css", "ul.dashlet-cell.rows.row-fluid.layout_Home div:nth-child(1) span a span:nth-child(1)").click();
		new VoodooControl("a", "css", "span li:nth-child(1) [data-dashletaction='createRecord']").click();
		sugar().alerts.waitForLoadingExpiration();

		sugar().meetings.createDrawer.getEditField("name").set(meetDS.get(0).get("name"));
		sugar().meetings.createDrawer.getEditField("status").set(meetDS.get(0).get("status"));
		sugar().meetings.createDrawer.getEditField("date_start_date").set(meetDS.get(0).get("date_start_date"));
		sugar().meetings.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// TODO: VOOD-1305
		// Click Future button in Planned Activities dashlet
		VoodooControl todayCtrl = new VoodooControl("button", "css", "div.dashlet-options button[value='today']");
		VoodooControl futureCtrl = new VoodooControl("button", "css", "div.dashlet-options button[value='future']");
		futureCtrl.waitForVisible();

		futureCtrl.click();
		sugar().alerts.waitForLoadingExpiration();

		// Hack
		// On First click of futureCtrl, page appears to wait indefinitely, hence this hack.
		// Now after first click, we wait per waitForLoadingExpiration(), then click todayCtrl, wait for
		// tab to populate, then click futureCtrl again. This time futureCtrl tab gets populated.
		todayCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
		futureCtrl.click();

		// Verify the created meeting is displayed
		VoodooControl meetingCtrl = new VoodooControl("p", "css",".tab-pane.active [data-action='pagination-body'] p");
		meetingCtrl.assertContains(meetDS.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
//