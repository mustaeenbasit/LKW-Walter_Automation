package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_28266 extends SugarTest {
	int schedulingTime1;

	public void setup() throws Exception {
		sugar.calls.api.create();

		// Login as QAUser
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * Verify that the time format is correct in call scheduling 
	 * @throws Exception
	 */
	@Test
	public void Calls_28266_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify Scheduling in Call create form
		sugar.calls.navToListView();
		// Click on "create" to open Call create form
		sugar.calls.listView.create();

		// TODO: VOOD-1350 Need Lib Support for meeting/call scheduling for meeting/call create form
		VoodooControl scheduleCtrl = new VoodooControl("div", "css", ".row.header .cell.times");
		String startTime = sugar.calls.createDrawer.getEditField("date_start_time").getText();
		int time = Integer.parseInt(startTime.substring(0, 2));

		if (time>=0 && time<= 4){
			schedulingTime1 = Integer.parseInt(startTime.substring(0, 2))+20;
		}
		else
			schedulingTime1 = Integer.parseInt(startTime.substring(0, 2))-4;

		// Verify scheduling time and there are 4hours from Start Time
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 ), true);
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 + 1), true );
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 + 2), true );

		sugar.calls.createDrawer.cancel();

		// Verify Scheduling in existing call record
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		String startTime1 = sugar.calls.createDrawer.getEditField("date_start_time").getText();
		time = Integer.parseInt(startTime1.substring(0, 2));

		if (time>=0 && time<= 4){
			schedulingTime1 = Integer.parseInt(startTime1.substring(0, 2))+20;
		}
		else
			schedulingTime1 = Integer.parseInt(startTime1.substring(0, 2))-4;

		// Verify scheduling time and there are 4hours from Start Time
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 ), true);
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 + 1), true );
		scheduleCtrl.assertContains(String.valueOf(schedulingTime1 + 2), true );

		sugar.calls.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}