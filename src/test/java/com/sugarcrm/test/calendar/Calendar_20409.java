package com.sugarcrm.test.calendar;

import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vishal Kumar <vkumar@sugarcrm.com>
 */
public class Calendar_20409 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify the Calendars Goto Date - Month view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20409_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		FieldSet myData = new FieldSet();
		myData = testData.get(testName).get(0);
		
		// Navigate to calendar module
		sugar.calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Getting current date
		DateTime date = DateTime.now();

		// click on month tab
		// TODO: VOOD-863
		new VoodooControl("input", "id", "month-tab").click();

		// Click on Calendar icon to view and select the dates
		new VoodooControl("span", "css", ".dateTime").click();

		// Verifying current date is highlighted
		new VoodooControl("td", "css", ".today.selected.selectable")
				.assertContains(Integer.toString(date.getDayOfMonth()), true);

		// For selecting different month clicking on arrow
		new VoodooControl("a", "css", ".calnavright").click();

		// Increment date with one month
		date = date.plusMonths(1);

		// Verify that the "Select Date" calendar is updated with the selected month and year
		new VoodooControl("a", "css", ".calnav").assertContains(
				date.toString("MMMM") + " " + date.toString("YYYY"), true);

		// Click on the date from different month
		new VoodooControl("td", "css", ".d" + myData.get("dayOfMonth") + ".selectable").click();

		// Verifying calendar is in month view and display correct selected month and year
		VoodooControl monthHeader = new VoodooControl("div", "css", ".monthHeader");
		monthHeader.assertContains("Previous Month", true);
		monthHeader.assertContains("Next Month", true);
		monthHeader.assertContains(
				date.toString("MMMM") + " " + date.toString("YYYY"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}