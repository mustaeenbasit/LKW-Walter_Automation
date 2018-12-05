package com.sugarcrm.test.cases;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_29016 extends SugarTest {
	FieldSet dashboardAndMeetingData = new FieldSet();

	public void setup() throws Exception {
		ArrayList<Record> myMeetings = new ArrayList<Record>();
		dashboardAndMeetingData = testData.get(testName).get(0);

		// Create a meeting from api for 'My History' tab
		FieldSet meetingData = new FieldSet();
		meetingData.put("name", testName);
		meetingData.put("status", dashboardAndMeetingData.get("status"));
		myMeetings.add(sugar().meetings.api.create(meetingData));
		meetingData.clear();

		// Create a case record
		sugar().cases.api.create();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Create a meeting from other user for 'Team History' tab
		meetingData.put("status", dashboardAndMeetingData.get("status"));
		myMeetings.add(sugar().meetings.create(meetingData));
		meetingData.clear();

		// Logout from QAUser and Login as admin user
		sugar().logout();
		sugar().login();

		// Go to Cases Module and click on a record at list view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Link meeting record to the case
		StandardSubpanel meetingsSubpanel = sugar().cases.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.linkExistingRecords(myMeetings);
	}

	/**
	 * Verify that Loading.. is not showing at History dashlet of record view of Cases Module
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_29016_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Cases Module and click on a record at list view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// At RHS -> My Dashboard -> History dashlet,
		String myDashboard = dashboardAndMeetingData.get("myDashboard");
		VoodooControl dashboardTitle = sugar().cases.dashboard.getControl("dashboard");
		if(!dashboardTitle.queryContains(myDashboard, true)) {
			sugar().cases.dashboard.chooseDashboard(myDashboard);
		}

		// Toggle between My History & Team History tabs
		// TODO: VOOD-814 - Lib support for handling My Dashboard->History Dashlet in recordview
		VoodooControl dashletRecordCtrl = new VoodooControl("li", "css", "ul.dashlet-row li.row-fluid:nth-child(2)");
		VoodooControl meetingCount = new VoodooControl("sapn", "css", dashletRecordCtrl.getHookString() + " ul.dashlet-cell div.dashlet-tab.active span.count");
		VoodooControl myHistoryCtrl = new VoodooControl("button", "css", dashletRecordCtrl.getHookString() + " .dashlet-options .dashlet-group button");
		VoodooControl teamHistoryCtrl = new VoodooControl("button", "css", dashletRecordCtrl.getHookString() + " .dashlet-options .dashlet-group button:nth-child(2)");
		VoodooControl firstRecord = new VoodooControl("a", "css", dashletRecordCtrl.getHookString() + " li a:nth-child(2)");
		VoodooControl secondRecordCtrl = new VoodooControl("a", "css", dashletRecordCtrl.getHookString() + " li:nth-child(2) a:nth-child(2)");

		// Click on 'My History' tab
		myHistoryCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the Related records should be displayed at Meetings tab
		meetingCount.assertContains(dashboardAndMeetingData.get("myCount"), true);
		firstRecord.assertContains(testName, true);
		secondRecordCtrl.assertExists(false);

		// Click on 'Team History' tab
		teamHistoryCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the Related records should be displayed at Meetings tab
		meetingCount.assertContains(dashboardAndMeetingData.get("teamCount"), true);
		firstRecord.assertContains(testName, true);
		secondRecordCtrl.assertContains(sugar().meetings.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}