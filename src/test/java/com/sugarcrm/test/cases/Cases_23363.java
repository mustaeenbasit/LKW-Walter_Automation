package com.sugarcrm.test.cases;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23363 extends SugarTest {
	MeetingRecord myMeeting;
	FieldSet myData;

	public void setup() throws Exception {
		// Get Today's Date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = sdf.format(date);
		FieldSet startDate = new FieldSet();
		startDate.put("date_start_date", todaysDate);

		myData = testData.get(testName).get(0);
		myMeeting = (MeetingRecord) sugar().meetings.api.create(startDate);
		sugar().cases.api.create();
		sugar().login();

		// Schedule Meeting record for a case needed
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		StandardSubpanel meetingsSubpanel = sugar().cases.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.linkExistingRecord(myMeeting);
	}

	/**
	 * Case Scheduled Meeting_Verify that the meeting detail view is displayed after clicking the meeting
	 * subject in Planned Activities dashlet.
	 *
	 */
	@Test
	public void Cases_23363_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Add Planned Activities dashlet to RHS of an Cases record
		String dashboardTitle = sugar().cases.dashboard.getControl("dashboard").getText().trim();
		if(!dashboardTitle.equals("My Dashboard")) {
			sugar().cases.dashboard.chooseDashboard("My Dashboard");
		}

		// Click the subject of an existing meeting in Planned Activities dashlet
		// TODO: VOOD-960
		new VoodooControl("a", "css", ".dashlet-content li:nth-child(1) a:nth-child(2)").click();

		VoodooUtils.waitForReady();

		// Verify that Meeting detail view is displayed
		sugar().meetings.recordView.getControl("moduleIDLabel").hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(sugar().meetings.moduleNameSingular, true);
		sugar().meetings.recordView.getDetailField("name").assertEquals(sugar().meetings.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
