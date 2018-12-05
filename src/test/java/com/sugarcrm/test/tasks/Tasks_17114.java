package com.sugarcrm.test.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_17114 extends SugarTest {
	FieldSet customData; 
	String currentDay, currentMonth, currentYear; 

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();	
	}

	/**
	 * Verify default values on date fields when only time is set.
	 * @throws Exception
	 */	
	@Test
	public void Tasks_17114_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Get current data and format it properly
		DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		String formattedDate1 = df1.format(new Date());
		
		// Open task record
		sugar.navbar.navToModule(sugar.tasks.moduleNamePlural);
		sugar.tasks.listView.create();
		sugar.alerts.waitForLoadingExpiration();

		// Open time picker and select time
		// TODO: VOOD-863
		new VoodooControl("i", "css", ".fld_date_start.edit .fa-clock-o").click();
		new VoodooControl("li", "css", ".ui-timepicker-wrapper li:nth-child(3)").click();

		// Verify that 12:30am is selected
		sugar.tasks.recordView.getEditField("date_start_time").assertContains(customData.get("time"), true);

		// Verify date field is selected automatically
		sugar.tasks.recordView.getEditField("date_start_date").assertContains(formattedDate1, true);

		// dismiss create drawer
		sugar.tasks.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}