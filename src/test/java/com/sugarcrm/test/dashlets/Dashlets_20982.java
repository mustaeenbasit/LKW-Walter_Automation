package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20982 extends SugarTest {
	MeetingRecord myMeetingRecord;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		FieldSet startAndEndDate = new FieldSet();
		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		startAndEndDate.put("date_start_date", todaysDate);
		startAndEndDate.put("date_end_date", todaysDate);
		myMeetingRecord = (MeetingRecord) sugar().meetings.api.create(startAndEndDate);
		startAndEndDate.clear();

		// Login as a valid user
		sugar().login();

		// Existing activity(Meetings) record related to the account needed.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel meetingsSubpanel =  sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.linkExistingRecord(myMeetingRecord);		
	}

	/**
	 * Verify that duplicating an activity record is canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20982_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet dashboardData = testData.get(testName).get(0);

		// Choose "My Dashboard"
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboard");;
		if(!dashboardTitle.queryContains(dashboardData.get("myDashboard"), true)) {
			sugar().accounts.dashboard.chooseDashboard(dashboardData.get("myDashboard"));
		}

		// Define Controls for Dashlets
		// TODO: VOOD-976 - need lib support of RHS on record view
		VoodooControl plaannedActivitiesDashletCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='planned-activities']");
		VoodooControl plannedMeetingsDashletTabCtrl = new VoodooControl("a", "css", plaannedActivitiesDashletCtrl.getHookString() + " .dashlet-tab.active a");
		VoodooControl meetingsRecordCountCtrl = new VoodooControl("span", "css", plaannedActivitiesDashletCtrl.getHookString() + " .dashlet-tab span");
		VoodooControl firstMeetingRecordCtrl = new VoodooControl("a", "css", plaannedActivitiesDashletCtrl.getHookString() + " .tab-content li a:nth-child(2)");
		VoodooControl secondMeetingRecordCtrl = new VoodooControl("li", "css", plaannedActivitiesDashletCtrl.getHookString() + " .tab-content li:nth-child(2)");

		// Verifying that linked Meetings record is visible in the dashlet
		plannedMeetingsDashletTabCtrl.assertContains(sugar().meetings.moduleNameSingular, true);
		meetingsRecordCountCtrl.assertContains(dashboardData.get("count"), true);
		firstMeetingRecordCtrl.assertContains(myMeetingRecord.getRecordIdentifier(), true);
		secondMeetingRecordCtrl.assertExists(false);

		// Select an activity(Meeting) record on 'Planned Activities' RHS dashlet 
		firstMeetingRecordCtrl.click();
		VoodooUtils.waitForReady();

		// Click 'Copy' button on the detail view
		sugar().meetings.recordView.copy();

		// Click "Cancel button on the edit view
		sugar().meetings.createDrawer.cancel();

		// Go back to the account record "detail view"
		sugar().meetings.recordView.getDetailField("relatedToParentName").click();
		VoodooUtils.waitForReady();

		// Verify that No matching duplicate activity record is displayed in the Planned Activities Dashlet list view
		plannedMeetingsDashletTabCtrl.assertContains(sugar().meetings.moduleNameSingular, true);
		meetingsRecordCountCtrl.assertContains(dashboardData.get("count"), true);
		firstMeetingRecordCtrl.assertContains(myMeetingRecord.getRecordIdentifier(), true);
		secondMeetingRecordCtrl.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}