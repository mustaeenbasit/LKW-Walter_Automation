package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20403 extends SugarTest {
	FieldSet customData;
	VoodooControl frstDayFieldCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify that the calendar starting day of the week can be set - Shared view
	 * @throws Exception
	 */
	@Test
	public void Calendar_20403_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to User Profile Detail View
		sugar.navbar.navToProfile();
		// Select User profile -> Advanced tab and click edit. 
		sugar.users.detailView.edit();	
		VoodooUtils.focusFrame("bwc-frame");
		sugar.users.userPref.getControl("tab4").click();
		// TODO: VOOD-563 need lib support for user profile edit page
		// Set "First day of week:" to Monday 
		frstDayFieldCtrl = new VoodooControl("select", "css", "#calendar_options [name='fdow']");
		frstDayFieldCtrl.click();
		new VoodooControl("option", "css", "#calendar_options [label='Monday']").click();
		VoodooUtils.focusDefault();
		sugar.users.editView.save();

		// Navigate to Calendar module
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		//  Verify that Monday is displayed as the first day of the week 
		// TODO: VOOD-863
		VoodooControl frstDayOfWeekCtrl = new VoodooControl("a", "css", ".week div:nth-child(1) div a");
		frstDayOfWeekCtrl.assertContains(customData.get("frstDayOfWeek"), true);

		// Verify Sunday is the last day of the week.
		VoodooControl lastDayOfWeekCtrl = new VoodooControl("a", "css", ".week div:nth-child(7) div a");
		lastDayOfWeekCtrl.assertContains(customData.get("lastDayOfWeek"), true);

		// In week view,Click Previous Week
		VoodooControl prevButtonCtrl = new VoodooControl("a", "css", ".monthHeader div:nth-child(1) a");
		prevButtonCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		//  Verify that Monday is displayed as the first day of the week 
		frstDayOfWeekCtrl.assertContains(customData.get("frstDayOfWeek"), true);

		// Verify Sunday is the last day of the week.
		lastDayOfWeekCtrl.assertContains(customData.get("lastDayOfWeek"), true);

		// Click 0n Next Week.
		VoodooControl nextButtonCtrl = new VoodooControl("a", "css", ".monthHeader div:nth-child(3) a");
		nextButtonCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		//  Verify that Monday is displayed as the first day of the week 
		frstDayOfWeekCtrl.assertContains(customData.get("frstDayOfWeek"), true);

		// Verify Sunday is the last day of the week.
		lastDayOfWeekCtrl.assertContains(customData.get("lastDayOfWeek"), true);

		// Select Month button,
		new VoodooControl("input", "id", "month-tab").click();

		//  Verify that Monday is displayed as the first day of the week 
		VoodooControl frstDayInMonthViewCtrl = new VoodooControl("a", "css", "#cal-grid div:nth-child(1) div .week div:nth-child(1) div a"); 
		frstDayInMonthViewCtrl.assertContains(customData.get("frstDayOfWeek"), true);

		// Verify Sunday is the last day of the week.
		VoodooControl lastDayInMonthViewCtrl =  new VoodooControl("a", "css", "#cal-grid div:nth-child(1) div .week div:nth-child(7) div a"); 
		lastDayInMonthViewCtrl.assertContains(customData.get("lastDayOfWeek"), true);

		// In month view, click "Previous Month"
		prevButtonCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		//  Verify that Monday is displayed as the first day of the week 
		frstDayInMonthViewCtrl.assertContains(customData.get("frstDayOfWeek"), true);

		// Verify Sunday is the last day of the week.
		lastDayInMonthViewCtrl.assertContains(customData.get("lastDayOfWeek"), true);

		// In month view, click "Next Month"
		nextButtonCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		//  Verify that Monday is displayed as the first day of the week 
		frstDayInMonthViewCtrl.assertContains(customData.get("frstDayOfWeek"), true);

		// Verify Sunday is the last day of the week.
		lastDayInMonthViewCtrl.assertContains(customData.get("lastDayOfWeek"), true);

		// Select Shared button.
		new VoodooControl("input", "id", "shared-tab").click();	

		//  Verify that Monday is displayed as the first day of the week 		
		frstDayOfWeekCtrl.assertContains(customData.get("frstDayOfWeek"), true);

		// Verify Sunday is the last day of the week.
		lastDayOfWeekCtrl.assertContains(customData.get("lastDayOfWeek"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}