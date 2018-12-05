package com.sugarcrm.test.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vishal Kumar <vkumar@sugarcrm.com>
 */

public class Calendar_20436 extends SugarTest {
	String meetingDate;
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	Calendar cal = Calendar.getInstance();
	DataSource myData = new DataSource();

	public void setup() throws Exception {
		meetingDate = sdf.format(cal.getTime());

		// Create Meeting for 14 occurrence with monthly repeat by 1 interval 
		FieldSet meetingsData = new FieldSet();
		myData = testData.get(testName);
		meetingsData.put("name", myData.get(0).get("meetingName"));
		meetingsData.put("date_start_time", myData.get(0).get("date_start_time"));
		meetingsData.put("repeatType", myData.get(0).get("repeatType"));
		meetingsData.put("repeatInterval", myData.get(0).get("repeatInterval"));
		meetingsData.put("repeatOccur", myData.get(0).get("repeatOccur"));
		meetingsData.put("repeatOccur", myData.get(0).get("repeatOccur"));
		meetingsData.put("date_start_date", meetingDate);
		sugar.meetings.api.create(meetingsData);
		sugar.login();
	}

	/**
	 * Verify "Monthly" meeting recurrence with n number of occurrences
	 * @throws Exception
	 */
	@Test
	public void Calendar_20436_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab.
		sugar.calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Click "month" button on "Calendar" page.
		// TODO: VOOD-863
		new VoodooControl("input", "id", "month-tab").click();

		// Calendar creates and displays a monthly occurence, of 14 meetings, from correct start date
		// TODO: VOOD-863
		VoodooControl nextMonth = new VoodooControl("a", "css", ".monthHeader div:nth-child(3) a");
		for (int i = 0; i < Integer.parseInt(myData.get(0).get("repeatOccur")); i++) {

			// TODO: VOOD-863
			VoodooControl meetingCtrl = new VoodooControl("div", "css",
					"div[datetime='" + meetingDate + " 08:00am']");
			meetingCtrl.assertEquals(myData.get(0).get("meetingName"), true);

			// Increment Date by one month
			cal.add(Calendar.MONTH, 1);
			meetingDate = sdf.format(cal.getTime());

			// Click the next month tab on the Calendar
			nextMonth.click();
			VoodooUtils.focusFrame("bwc-frame");
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}