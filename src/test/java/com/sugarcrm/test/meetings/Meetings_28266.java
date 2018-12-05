package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_28266 extends SugarTest {

	public void setup() throws Exception {
		sugar().meetings.api.create();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that the time format is correct in meeting scheduling 
	 * @throws Exception
	 */
	@Test
	public void Meetings_28266_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		// TODO: VOOD-1350 Need Lib Support for meeting/call scheduling for meeting/call create form
		VoodooControl scheduleCtrl = new VoodooControl("div", "css", ".row.header .cell.times");
		String startTime = sugar().meetings.createDrawer.getEditField("date_start_time").getText();
		int time = Integer.parseInt(startTime.substring(0, 2));

		// Subtract 4 hours from start date 
		int schedulingTime1;
		schedulingTime1 = Integer.parseInt(startTime.substring(0, 2))-4;
		if (time>=0 && time<= 4)
			schedulingTime1 = Integer.parseInt(startTime.substring(0, 2))+20;

		// Verify scheduling time and there are 4hours from Start Time
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 ), true);
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 + 1), true );
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 + 2), true );

		// Cancel creating record
		sugar().meetings.createDrawer.cancel();

		// Verify Scheduling in existing meetings record
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		String startTime1 = sugar().meetings.createDrawer.getEditField("date_start_time").getText();
		time = Integer.parseInt(startTime1.substring(0, 2));

		// Subtract 4 hours from start date 
		schedulingTime1 = Integer.parseInt(startTime1.substring(0, 2))-4;
		if (time>=0 && time<= 4)
			schedulingTime1 = Integer.parseInt(startTime1.substring(0, 2))+20;

		// Verify scheduling time and there are 4hours from Start Time
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 ), true);
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 + 1), true );
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 + 2), true );

		// Cancel updating record
		sugar().meetings.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}