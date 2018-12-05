package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Calendar_20323 extends SugarTest {
	String tomorrowsDate;
	
	public void setup() throws Exception {
		
		// Get tomorrows Date
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		date = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		tomorrowsDate = sdf.format(date);
		
		// Create a Meeting for tomorrow, default time
		FieldSet startDate = new FieldSet();
		startDate.put("date_start_date",tomorrowsDate);
		sugar.meetings.api.create(startDate);
		sugar.login();
	}

	/**
	 * Verify that meeting detail view page is displayed when 
	 *  clicking the meeting subject link in Shared Calendar panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20323_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open Meeting in Shared Calendar 
		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Open Shared Calendar
		// TODO: VOOD-863: Lib support for Calendar module
		VoodooControl sharedBtn = new VoodooControl("input", "id", "shared-tab");
		sharedBtn.click();
		
		// Click an Meetings subject in the Calendar view.
		// TODO: VOOD-863
		VoodooControl meetingCtrl = new VoodooControl("div", "css", "div[datetime='" 
		     + tomorrowsDate + " " + sugar.meetings.getDefaultData().get("date_start_time") + "']");
		meetingCtrl.assertContains(sugar.meetings.getDefaultData().get("name"), true);
		meetingCtrl.click();

		// Assert Meetings detail view is visible
		sugar.meetings.recordView.assertVisible(true);
		sugar.meetings.recordView.getDetailField("name")
			.assertEquals(sugar.meetings.getDefaultData().get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}