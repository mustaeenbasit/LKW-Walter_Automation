package com.sugarcrm.test.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20406 extends SugarTest {
	FieldSet customData;
	String todaysDate;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		// Create Call     
		todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		FieldSet fs = new FieldSet();
		fs.put("date_start_date", todaysDate);
		fs.put("date_start_time", customData.get("date_start_time1"));
		sugar.calls.api.create(fs);

		// Create Meeting
		fs.clear();
		fs.put("date_start_date", todaysDate);
		fs.put("date_start_time", customData.get("date_start_time2"));
		sugar.meetings.api.create(fs);

		// Create task
		fs.clear();
		fs.put("date_due_date", todaysDate);
		fs.put("date_due_time", customData.get("date_due_time3"));
		sugar.tasks.api.create(fs);
		sugar.login();
	}

	/**
	 * Verify 15 minute time increments on the calendar Day view with activities
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20406_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		// Day view
		// TODO: VOOD-863
		new VoodooControl("input", "id", "day-tab").click();

		// Verify that the calendar is displayed with 15 minute time increments.
		String slot1Time = new VoodooControl("div", "css", "div.week div div:nth-child(5)").getAttribute("time");
		String slot2Time = new VoodooControl("div", "css", "div.week div div:nth-child(6)").getAttribute("time");
		SimpleDateFormat format = new SimpleDateFormat("HH:mma");
		Date date1 = format.parse(slot1Time);
		Date date2 = format.parse(slot2Time);
		long incrementTime = date2.getTime() - date1.getTime(); 
		long incrementTimeInMins = incrementTime/60000;
		Assert.assertTrue("Increment time is 15mins", incrementTimeInMins == Integer.parseInt(customData.get("incrementTime")));

		// Verify Call is displayed accurately
		new VoodooControl("div", "css", "div.week div div:nth-child(58)").assertAttribute("datetime", todaysDate+" "+customData.get("date_start_time1"));

		// Verify Meeting is displayed accurately
		new VoodooControl("div", "css", "div.week div div:nth-child(59)").assertAttribute("datetime", todaysDate+" "+customData.get("date_start_time2"));

		// Verify task is displayed accurately
		new VoodooControl("div", "css", "div.week div div:nth-child(60)").assertAttribute("datetime", todaysDate+" "+customData.get("date_due_time3"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}