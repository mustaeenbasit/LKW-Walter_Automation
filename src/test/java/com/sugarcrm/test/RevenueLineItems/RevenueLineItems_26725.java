package com.sugarcrm.test.RevenueLineItems;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26725 extends SugarTest {
	public void setup() throws Exception {
		sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * Calendar icon should remain and allow to select date when mass update "Expected Closed Date"
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26725_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet massUpdateFS = testData.get(testName).get(0);

		// Find out the todays date, month and year
		Date date = new Date();
		DateFormat dateNew = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = dateNew.format(date);
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(date);
		myCalendar = Calendar.getInstance();    
		String month = myCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		int year = myCalendar.get(Calendar.YEAR);
		int day = myCalendar.get(Calendar.DAY_OF_MONTH);

		// Navigate to RLI list view
		sugar().revLineItems.navToListView();

		// Check select box for one record and select "Mass Update" action
		sugar().revLineItems.listView.checkRecord(1);
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.massUpdate();

		// Select "Expected Closed Date"
		sugar().revLineItems.massUpdate.getControl("massUpdateField02").set(massUpdateFS.get("expectedClosedDate"));

		// TODO: VOOD-910 Need to have a lib to support for the date picker widget in BWC and sidecar modules
		VoodooControl calendarIconCtrl = new VoodooControl("input", "css", ".massupdate.fld_date_closed input");
		VoodooControl activeDateCtrl = new VoodooControl("td", "css", ".dropdown-menu:nth-child(8) .datepicker-days td[class='day active']");
		VoodooControl currentMonthAndYearCtrl = new VoodooControl("th", "css", ".dropdown-menu:nth-child(8) .datepicker-days .table-condensed .switch");

		// Click on the calendar icon try to select a date
		// CSS for the calendar view(todays date, month and year) are available on the page only when click twice on the date type field.
		calendarIconCtrl.click();
		sugar().revLineItems.listView.getControl("moduleTitle").click();
		calendarIconCtrl.click();

		// Verify that the Calendar appears showing the current month and highlighted today
		currentMonthAndYearCtrl.assertContains(month + " " + year, true);
		activeDateCtrl.assertContains(String.valueOf(day), true);

		// Select the current day date and click on Update button
		activeDateCtrl.click();
		sugar().revLineItems.massUpdate.update();

		// Verify that Mass Update can be done
		sugar().revLineItems.listView.getDetailField(1, "date_closed").assertContains(todaysDate, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}