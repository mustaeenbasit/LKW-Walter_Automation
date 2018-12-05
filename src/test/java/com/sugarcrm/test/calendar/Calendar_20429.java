package com.sugarcrm.test.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Calendar_20429 extends SugarTest {
	FieldSet meetingData;

	public void setup() throws Exception {
		meetingData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify "Daily" meeting recurrence with an end date and interval > 1
	 * @throws Exception
	 */
	@Test
	public void Calendar_20429_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); 
		
		// Getting the Meeting start day i.e which is also the first day of the current week
		cal.add(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() - cal.get(Calendar.DAY_OF_WEEK));
		String meetingDate = sdf.format(cal.getTime());
		
		// Getting the repeat until day of the meetings by adding 29 days to the start date
		cal.add(Calendar.DATE, 29);
		String repeatUntilDate = sdf.format(cal.getTime());
		
		// Turning the date back to the start date of the meetings
		cal.add(Calendar.DATE, -29);
		
		// Click "Calendar" navigation tab.
		sugar.calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Clicking the calendar cell 
		// TODO: VOOD-863
		new VoodooControl("div", "css", "div[datetime='" + meetingDate + " 08:00am']").click();
		
		// Creating repetitive meetings
		VoodooUtils.focusDefault();
		sugar.meetings.createDrawer.getEditField("name").set(meetingData.get("name"));
		sugar.meetings.createDrawer.getEditField("repeatType").set(meetingData.get("repeatType"));
		sugar.meetings.createDrawer.getEditField("repeatInterval").set(meetingData.get("repeatInterval"));
		sugar.meetings.createDrawer.getEditField("repeatUntil").set(repeatUntilDate);
		sugar.meetings.createDrawer.save();
	
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-863
		VoodooControl nextWeekBtn = new VoodooControl("a", "css", ".monthHeader div:nth-child(3) a");
		VoodooControl meetingCtrl = new VoodooControl("div", "css", "div[datetime='" 
				+ meetingDate + " 08:00am'] .head");

		int verifyMeeting = 15;
		
		// Verifying 15 meetings on 5 Calendar pages
		while(verifyMeeting > 1) {
			meetingCtrl = new VoodooControl("div", "css", "div[datetime='" + meetingDate + " 08:00am'] .head");
			meetingCtrl.assertEquals("Important Meeting", true);
			cal.add(Calendar.DATE, 2);
			meetingDate = sdf.format(cal.getTime());
			verifyMeeting--;

			if (verifyMeeting == 11 || verifyMeeting == 8 || verifyMeeting == 4 || verifyMeeting == 1){
				nextWeekBtn.click(); 
				VoodooUtils.waitForReady();
				VoodooUtils.focusFrame("bwc-frame");
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}