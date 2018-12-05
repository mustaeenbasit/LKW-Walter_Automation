package com.sugarcrm.test.calls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_27169 extends SugarTest {
	FieldSet customData;
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that a Call is saved and appears when click on a cell of Calendar
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_27169_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		customData = testData.get(testName).get(0);
		
		// Calculate tomorrow's date
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance(); 
		c.add(Calendar.DATE, 1);
		Date dt = c.getTime();
		String tomorrowDate = sdf.format(dt);

		// Navigate to Calender Module.
		sugar.navbar.navToModule("Calendar");
		sugar.alerts.waitForLoadingExpiration();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-863
		// Ensure Weekly tab is selected
		new VoodooControl("input", "id", "week-tab").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Click slot for tomorrow, 11:00 am
		new VoodooControl("div", "css", "div[datetime='"+tomorrowDate+" "+customData.get("startTime")+"']").click();
		VoodooUtils.focusDefault();		

		// New Meeting page appears. Select "Schedule a Call" from Alert
		sugar.alerts.getAlert().getChildElement("a", "css", "a[data-action='schedule-call']").click();
		
		// Call page appears. Input a Call name
		sugar.calls.createDrawer.getEditField("name").set(sugar.calls.getDefaultData().get("name"));

		// Confirm Start date and time are correct
		sugar.calls.createDrawer.getEditField("date_start_date").assertContains(tomorrowDate, true);
		sugar.calls.createDrawer.getEditField("date_start_time").assertContains(customData.get("startTime"), true);
		sugar.calls.createDrawer.save();
		
		// Control returns to Calendar view
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-863
		// Confirm that new Call appears in tomorrow's cell at 11:00am in Calendar
		VoodooControl callCellCtrl = new VoodooControl("div", "css", "div[datetime='"+tomorrowDate+" "+customData.get("startTime")+"'] div div.head div:nth-of-type(2)");
		callCellCtrl.assertExists(true);
		
		// Click on the call to verify call record
		callCellCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify, Start Date is 11am of tomorrow. End Date is 15 minutes after, 11:15am of tomorrow. 
		sugar.calls.recordView.getDetailField("date_start_date").assertContains(tomorrowDate+" "+customData.get("callDuration"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}