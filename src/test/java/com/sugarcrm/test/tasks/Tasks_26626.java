package com.sugarcrm.test.tasks;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_26626 extends SugarTest {	
	@Override
	public void setup() throws Exception {	
		sugar().login();
	}

	/**
	 * Verify [Object Object] is not shown if you reselect/delete data in a datetime fields
	 * @throws Exception
	 */	
	@Test
	public void Tasks_26626_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

		int intMonth = localCalendar.get(Calendar.MONTH) + 1;
		String currentDay = String.valueOf(localCalendar.get(Calendar.DATE));
		String currentMonth = String.valueOf(intMonth);
		Date dTime = new Date(localCalendar.getTimeInMillis());
		Format formatter = new SimpleDateFormat("MMMM");
		String currentMonthName = formatter.format(dTime);
		String currentYear = String.valueOf(localCalendar.get(Calendar.YEAR));

		sugar().navbar.navToModule("Tasks");
		sugar().tasks.listView.create();

		// TODO: VOOD-910 - Need to have a lib to support for the date picker widget in BWC and sidecar modules
		new VoodooControl("i", "css", ".fld_date_start.edit i.fa-calendar").click();
		new VoodooControl("th", "css", ".datepicker.dropdown-menu table.table-condensed tr:first-child th:nth-child(2)").assertContains(currentYear, true);
		new VoodooControl("th", "css", ".datepicker.dropdown-menu table.table-condensed tr:first-child th:nth-child(2)").assertContains(currentMonthName, true);
		new VoodooControl("div", "css", ".fld_date_start.edit .datepicker").click();
		new VoodooControl("td", "css", ".day.active").click();

		// Verify that today is selected
		new VoodooControl("input", "css", ".fld_date_start.edit .input-append.date input").assertContains(currentYear, true);
		new VoodooControl("input", "css", ".fld_date_start.edit div input.datepicker").assertContains(currentMonth, true);
		new VoodooControl("input", "css", ".fld_date_start.edit div input.datepicker").assertContains(currentDay, true);

		// Verify the different time is selected and [object Object] is not shown
		String firstTime = "12:00am";
		String objectStr = "[object Object]";
		new VoodooControl("i", "css", "span[data-voodoo-name='date_start']  i.fa-clock-o").click();

		// TODO: VOOD-907 - Need to add the lib to able to delete or empty the field
		sugar().tasks.createDrawer.getEditField("date_start_date").set("");
		new VoodooControl("li", "css", ".ui-timepicker-list li:first-child").assertContains(firstTime, true);
		new VoodooControl("input", "css", ".fld_date_start.edit div input.datepicker").assertContains(objectStr, false);
		new VoodooControl("li", "css", ".ui-timepicker-list li:first-child").assertContains(objectStr, false);
		new VoodooControl("li", "css", ".ui-timepicker-selected").click();	

		// Just Cancel the create process
		sugar().tasks.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	@Override
	public void cleanup() throws Exception {}
}
