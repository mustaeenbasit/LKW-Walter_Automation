package com.sugarcrm.test.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vishal Kumar <vkumar@sugarcrm.com>
 */
public class Calendar_20456 extends SugarTest {
	DataSource myData = new DataSource();

	public void setup() throws Exception {
		myData = testData.get(testName);
		sugar().login();
	}

	/**
	 * Verify Edit single meeting - set to yearly recurring
	 * @throws Exception
	 */
	@Test
	public void Calendar_20456_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Setting current date for meetings
		String meetingDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		meetingDate = sdf.format(cal.getTime());
		meetingDate = sdf.format(cal.getTime());

		// Navigate to Calendar Module
		sugar().calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on month tab on the Calendar
		// TODO: VOOD-863
		VoodooControl monthTabCtrl = new VoodooControl("input", "id", "month-tab");
		monthTabCtrl.click();

		// Click on cell in month tab
		// TODO: VOOD-863
		VoodooControl meetingCtrl = new VoodooControl("div", "css", ".basic_slot[datetime*='" + meetingDate + "']");
		meetingCtrl.click();

		// Create monthly meeting with 3 recurrence via Calendar cell
		VoodooUtils.focusDefault();
		sugar().meetings.recordView.getEditField("name").set(myData.get(0).get("name"));
		sugar().meetings.recordView.getEditField("repeatType").set(myData.get(0).get("repeatType"));
		sugar().meetings.recordView.getEditField("repeatInterval").set(myData.get(0).get("repeatInterval"));
		sugar().meetings.recordView.getEditField("repeatOccur").set(myData.get(0).get("repeatOccur"));
		sugar().meetings.recordView.save();

		// Navigate to calendar module
		sugar().calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on month tab on the Calendar
		monthTabCtrl.click();

		// Click on next month to find second meeting
		// TODO: VOOD-863
		new VoodooControl("a", "css", ".monthHeader div:nth-child(3) a").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Adding 1 month to find the second meeting
		cal.add(Calendar.MONTH, 1);
		meetingDate = sdf.format(cal.getTime());

		// TODO: VOOD-863
		new VoodooControl("div", "css", ".basic_slot[datetime*='" + meetingDate + "'] div div").click();

		// Deleting second meeting
		VoodooUtils.focusDefault();
		sugar().meetings.recordView.delete();
		sugar().alerts.confirmAllWarning();
		VoodooUtils.waitForReady();
		
		// Verifying total no of meeting count in list view after deletion.
		Assert.assertEquals("should be only 2 meetings after deletion of 2nd meeting",
				Integer.parseInt(myData.get(0).get("repeatOccur")) - 1, sugar().meetings.listView.countRows());

		// Verifying second meeting is not present in list view
		sugar().meetings.listView.getDetailField(1, "date_start_date").assertContains(meetingDate, false);
		sugar().meetings.listView.getDetailField(2, "date_start_date").assertContains(meetingDate, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}