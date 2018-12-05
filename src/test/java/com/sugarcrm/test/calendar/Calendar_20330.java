package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20330 extends SugarTest {
	public void setup() throws Exception {

		// Create Call and Meeting record
		FieldSet startDate = new FieldSet();
		String todayDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		startDate.put("date_start_date",todayDate);
		sugar.calls.api.create(startDate);
		sugar.meetings.api.create(startDate);
		sugar.login();
	}

	/**
	 * Verify that a call/meeting can be deleted from calendar view.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20330_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete Meetings Record
		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Click any Meetings subject in the Calendar view.
		// TODO: VOOD-863
		VoodooControl meetingCtrl = new VoodooControl("div", "css", ".week div[time='11:00am'] div div.head");
		meetingCtrl.assertContains(sugar.meetings.getDefaultData().get("name"), true);
		meetingCtrl.click();

		// Click "Delete" button in detail view.
		sugar.meetings.recordView.delete();
		sugar.alerts.confirmAllWarning();
		sugar.alerts.waitForLoadingExpiration();

		// Delete Calls Record
		// Click "Calendar" navigation tab
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Click any Calls subject in the Calendar view.
		// TODO: VOOD-863
		VoodooControl callCtrl = new VoodooControl("div", "css", ".week div[time='10:30pm'] div div.head");
		callCtrl.assertContains(sugar.calls.getDefaultData().get("name"), true);
		callCtrl.click();

		// Click "Delete" button in detail view.
		sugar.calls.recordView.delete();
		sugar.alerts.confirmAllWarning();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that the deleted Call and Meeting is not displayed in the calendar view.
		// Click "Calendar" navigation tab
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("div", "css", ".week div[time='11:00am']").assertContains(sugar.meetings.getDefaultData().get("name"), false);
		new VoodooControl("div", "css", ".week div[time='10:30pm']").assertContains(sugar.calls.getDefaultData().get("name"),false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}