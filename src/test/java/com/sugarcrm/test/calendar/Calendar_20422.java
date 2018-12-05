package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20422 extends SugarTest {
	DataSource calendarData = new DataSource();
	VoodooControl firstDayOfWeekCtrl;

	public void setup() throws Exception {
		calendarData = testData.get(testName);
		sugar().login();
	}

	/**
	 * Verify that the calendar starting day of the week can be set - month view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20422_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		for(int i = 0; i < calendarData.size(); i++) {
			// Go to profile > Advanced
			// Navigate to User Profile Detail View 
			sugar().navbar.navToProfile();

			// Click Edit to open EditView for User Profile.
			sugar().users.detailView.edit();
			VoodooUtils.focusFrame("bwc-frame");
			sugar().users.userPref.getControl("tab4").click();
			VoodooUtils.waitForReady();

			// TODO: VOOD-563
			// "Calendar options" set "First day of week:" Sunday
			firstDayOfWeekCtrl = new VoodooControl("select", "css", "#calendar_options [name='fdow']");
			firstDayOfWeekCtrl.click();
			new VoodooControl("option", "css", "#calendar_options [label='" + calendarData.get(i).get("firstDayOfWeek") + "']").click();
			VoodooUtils.focusDefault();
			sugar().users.editView.save();

			// Go to Calendar module
			sugar().calendar.navToListView();
			VoodooUtils.focusFrame("bwc-frame");

			// TODO: VOOD-863
			// Define controls for Calendar
			VoodooControl firstDayOfWeekCtrl = new VoodooControl("a", "css", ".week div:nth-child(1) div a");
			VoodooControl monthBtnCtrl = new VoodooControl("input", "id", "month-tab");
			VoodooControl firstDayInMonthViewCtrl = new VoodooControl("a", "css", "#cal-grid div:nth-child(1) div .week div:nth-child(1) div a");
			VoodooControl prevButtonCtrl = new VoodooControl("img", "css", ".monthHeader div:nth-child(1) a img");
			VoodooControl nextButtonCtrl = new VoodooControl("img", "css", ".monthHeader div:nth-child(3) a img");

			// Verify that 'Day Selected' in user profile is displayed as the first day of the week 
			firstDayOfWeekCtrl.assertContains(calendarData.get(i).get("firstDayOfWeekDisplayValue"), true);

			// Select Month button to display month view
			monthBtnCtrl.click();
			VoodooUtils.waitForReady();

			// Verify that 'Day Selected' in user profile is displayed as the first day of the week
			firstDayInMonthViewCtrl.assertContains(calendarData.get(i).get("firstDayOfWeekDisplayValue"), true);

			// Click "Next Month" 
			nextButtonCtrl.click();
			VoodooUtils.focusFrame("bwc-frame");

			// Verify that 'Day Selected' in user profile is displayed as the first day of the week
			firstDayInMonthViewCtrl.assertContains(calendarData.get(i).get("firstDayOfWeekDisplayValue"), true);

			// Click "Previous Month"
			prevButtonCtrl.click();
			VoodooUtils.focusFrame("bwc-frame");

			// Verify that 'Day Selected' in user profile is displayed as the first day of the week
			firstDayInMonthViewCtrl.assertContains(calendarData.get(i).get("firstDayOfWeekDisplayValue"), true);
			VoodooUtils.focusDefault();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}