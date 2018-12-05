package com.sugarcrm.test.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Calendar_20410 extends SugarTest {
	String dayOfMonth, monthString, monthStringComplete, yearString, weekString;
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Getting today's date
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
		SimpleDateFormat monthFormatComplete = new SimpleDateFormat("MMMMM");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

		// Creating a future date
		dayOfMonth = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.YEAR, 1);
		cal.set(Calendar.DAY_OF_MONTH, 10);
		
		monthString = String.valueOf(monthFormat.format(cal.getTime()));
		monthStringComplete = String.valueOf(monthFormatComplete.format(cal.getTime()));
		yearString = String.valueOf(yearFormat.format(cal.getTime()));

		// Getting the first and the last days of the week of the future date
		Calendar first = (Calendar) cal.clone();
		first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

		// and add six days to the first date to get last date
		Calendar last = (Calendar) first.clone();
		last.add(Calendar.DAY_OF_YEAR, 6);

		// Getting the weekString in the required format i.e. "2015 August 2 - 2015 August 8"
		SimpleDateFormat df = new SimpleDateFormat("MMMMM d yyyy");
		weekString = df.format(first.getTime()) + " - " + df.format(last.getTime());
		sugar().login();
	}

	/**
	 * Verify the Calendars Goto Date - Shared view.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20410_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);

		// TODO: VOOD-863: Need Library support for Calendar module
		VoodooControl sharedButton = new VoodooControl("button", "id", "shared-tab");
		VoodooControl gotoDateButton = new VoodooControl("button", "id", "goto_date_trigger");
		VoodooControl gotoDateContainer = new VoodooControl("div", "id", "container_goto_date_trigger");
		VoodooControl todaySelectedDate = new VoodooControl("a", "css", "#goto_date_trigger_div td.today.selectable a");
		VoodooControl calMonthYearLink = new VoodooControl("a", "class", "calnav");
		VoodooControl monthHeaderOnPopUp = new VoodooControl("label", "css", "[for='goto_date_trigger_div_nav_month']");
		VoodooControl monthDropDown = new VoodooControl("select", "id", "goto_date_trigger_div_nav_month");
		VoodooControl yearField = new VoodooControl("input", "id", "goto_date_trigger_div_nav_year");
		VoodooControl okButton = new VoodooControl("button", "id", "goto_date_trigger_div_nav_submit");

		// Taking 10 as the default futureDate for any month 
		VoodooControl futureDate = new VoodooControl("a", "css", "#goto_date_trigger_div_t .d10 a");   
		VoodooControl weekPageHeader = new VoodooControl("h3", "css", ".monthHeader div:nth-child(2) h3");

		// Click on the "Shared" button and the "Goto Date" button.
		VoodooUtils.focusFrame("bwc-frame");
		sharedButton.click();
		gotoDateButton.click();

		// Verify that a small calendar pop-up is displayed and the highlighted day is the current date.
		gotoDateContainer.assertVisible(true);
		todaySelectedDate.assertEquals(dayOfMonth, true);

		// Clicking the "Month Year" hyperlink on "Select Date" Calendar
		calMonthYearLink.click();

		// Verifying that a pop-up that allows you to enter a month and year appears
		monthHeaderOnPopUp.assertEquals(customData.get("calendarText"), true);

		// Enter a Month and Year then click OK.
		monthDropDown.set(monthString);
		yearField.set(yearString);
		okButton.click();

		// Verify that the "Select Date" calendar is updated with the selected month and year.
		VoodooUtils.waitForReady();
		calMonthYearLink.assertContains(monthStringComplete + " " + yearString, true);

		// Clicking on a day to add a calendar entry 
		futureDate.click();
		VoodooUtils.waitForReady();

		// Verify that the Calendar is still in "Shared" view mode 
		Assert.assertEquals(sharedButton.getAttribute("value"), customData.get("value"));

		// Verify that the Calendar displays the week you have selected.
		weekPageHeader.assertEquals(weekString, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
