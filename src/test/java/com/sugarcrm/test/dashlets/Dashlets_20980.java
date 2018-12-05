package com.sugarcrm.test.dashlets;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20980 extends SugarTest {
	CallRecord myCallRecord;
	StandardSubpanel callsSubpanel;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		FieldSet startAndEndDate = new FieldSet();
		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		startAndEndDate.put("date_start_date", todaysDate);
		startAndEndDate.put("date_end_date", todaysDate);
		myCallRecord = (CallRecord) sugar().calls.api.create(startAndEndDate);
		startAndEndDate.clear();

		// Login as a valid user
		sugar().login();

		// Existing call related an existing account record needed.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		callsSubpanel =  sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.linkExistingRecord(myCallRecord);		
	}

	/**
	 * Verify that "Schedule Call" is  removed from "Planned ACTIVITIES" Dashlet.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20980_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet dashboardData = testData.get(testName).get(0);

		// Choose "My Dashboard"
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboard");;
		if(!dashboardTitle.queryContains(dashboardData.get("myDashboard"), true)) {
			sugar().accounts.dashboard.chooseDashboard(dashboardData.get("myDashboard"));
		}

		// Select a call record on 'Planned Activities' RHS dashlet
		// TODO: VOOD-960 - Dashlet selection
		VoodooControl plaannedActivitiesDashletCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='planned-activities']");
		new VoodooControl("div", "css", plaannedActivitiesDashletCtrl.getHookString() + " .dashlet-tab:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// Click "Unlink" button
		// TODO: VOOD-976 - need lib support of RHS on record view
		new VoodooControl("i", "css", plaannedActivitiesDashletCtrl.getHookString() + " .tab-content li .pull-right .fa-chain-broken").click();

		// Verify that Pop-up window "Are you sure you want to unlink the call recordName?"
		sugar().alerts.getWarning().assertContains(dashboardData.get("warningMessage") + myCallRecord.getRecordIdentifier(), true);

		// Click "Confirm" button on the pop-up window. 
		sugar().alerts.getWarning().confirmAlert();

		// Verify that "No data available" is displayed on 'Planned Activities' RHS dashlet
		// TODO: VOOD-976 - need lib support of RHS on record view
		new VoodooControl("div", "css", plaannedActivitiesDashletCtrl.getHookString() + " .tab-content .block-footer").assertContains(dashboardData.get("noData"), true);

		// Go to the account detail view(For refreshing the page)
		VoodooUtils.refresh();

		// Verify that the record is also removed from built-in Calls sub-panel
		Assert.assertTrue("The subpanel is not empty", callsSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}