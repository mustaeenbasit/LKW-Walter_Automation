package com.sugarcrm.test.tasks;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_17112 extends SugarTest {
	FieldSet customData;
	String currentMonth, currentDay, currentYear;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();	
	}

	/**
	 * Verify user can specify date and time for the Tasks module.
	 * @throws Exception
	 */	
	@Test
	public void Tasks_17112_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Get Month, Day, Year
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		
		// Get current data and format it properly
		DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		String formattedDate1 = df1.format(new Date());

		Date dTime = new Date(localCalendar.getTimeInMillis());
		Format formatter = new SimpleDateFormat("MMMM");
		String currentMonthName = formatter.format(dTime);
		currentYear = String.valueOf(localCalendar.get(Calendar.YEAR));

		// Tasks drawer
		sugar.navbar.navToModule(sugar.tasks.moduleNamePlural);
		sugar.tasks.listView.create();
		sugar.alerts.waitForLoadingExpiration();

		// Verify month, year on calendar icon
		new VoodooControl("i", "css", ".fld_date_start.edit i.fa-calendar").click();
		new VoodooControl("th", "css", "div:nth-child(2) div.datepicker-days table tr:nth-child(1) th.switch").assertContains(String.format("%s %s", currentMonthName, currentYear), true);
		new VoodooControl("div", "css", ".fld_date_start.edit .datepicker").click();
		new VoodooControl("td", "css", ".day.active").click();

		// Verify that today is selected
		sugar.tasks.recordView.getEditField("date_start_date").assertContains(formattedDate1, true);

		// Verify that time drop down.  15 minutes apart between 2 time selected.
		new VoodooControl("i", "css", ".fld_date_start.edit .fa-clock-o").click();
		new VoodooControl("li", "css", ".ui-timepicker-list li:first-child").assertContains(customData.get("firstTime"), true);
		new VoodooControl("li", "css", ".ui-timepicker-list li:nth-child(2)").assertContains(customData.get("secondTime"), true);

		// Selected/Highlighted time 
		VoodooControl timeSelectCtrl = new VoodooControl("li", "css", ".ui-timepicker-selected");
		timeSelectCtrl.click();	

		// Verify time selected 
		sugar.tasks.recordView.getEditField("date_start_time").assertContains(timeSelectCtrl.getText(), true);

		// dismiss create drawer
		sugar.tasks.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}