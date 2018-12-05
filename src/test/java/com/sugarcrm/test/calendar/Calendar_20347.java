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
public class Calendar_20347 extends SugarTest {
	String previousWeekDate = "pd", currentWeekDate = "cd", nextWeekDate = "nd";
	DataSource myData = new DataSource();

	public void setup() throws Exception {
		FieldSet callsData = new FieldSet();
		myData = testData.get(testName);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();

		// Get the Previous Week Date
		cal.add(Calendar.DATE, -7);
		previousWeekDate = sdf.format(cal.getTime());

		// Get the Current Week Date
		cal.add(Calendar.DATE, 7);
		currentWeekDate = sdf.format(cal.getTime());

		// Get the Next Week Date
		cal.add(Calendar.DATE, 7);
		nextWeekDate = sdf.format(cal.getTime());

		// Create three Call record for Current, Previous and Next week
		callsData.put("date_start_date", previousWeekDate);
		callsData.put("name", myData.get(0).get("callName"));
		sugar.calls.api.create(callsData);
		callsData.clear();
		callsData.put("date_start_date", currentWeekDate);
		callsData.put("name", myData.get(1).get("callName"));
		sugar.calls.api.create(callsData);
		callsData.clear();
		callsData.put("date_start_date", nextWeekDate);
		callsData.put("name", myData.get(2).get("callName"));
		sugar.calls.api.create(callsData);
		callsData.clear();
		sugar.login();
	}

	/**
	 * Display calendar_Verify that call/meeting are displayed in Week Calendar panel.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20347_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Call for current week
		// TODO: VOOD-863
		new VoodooControl("div", "css", "div[datetime='" + currentWeekDate
				+ " " + sugar.calls.getDefaultData().get("date_start_time")
				+ "']").assertContains(myData.get(1).get("callName"), true);

		// TODO: VOOD-863
		// Click on Previous Week to find the Call
		new VoodooControl("a", "css", ".monthHeader div:nth-child(1) a").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", "div[datetime='" + previousWeekDate
				+ " " + sugar.calls.getDefaultData().get("date_start_time")
				+ "']").assertContains(myData.get(0).get("callName"), true);

		// TODO: VOOD-863
		// Click on Next Week to find the Call
		VoodooControl nextWeekCtrl = new VoodooControl("a", "css",
				".monthHeader div:nth-child(3) a");
		nextWeekCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		nextWeekCtrl.click();

		// Needed bwc focus again
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", "div[datetime='" + nextWeekDate
				+ " " + sugar.calls.getDefaultData().get("date_start_time")
				+ "']").assertContains(myData.get(2).get("callName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
