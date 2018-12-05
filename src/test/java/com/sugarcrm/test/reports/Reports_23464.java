package com.sugarcrm.test.reports;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_23464 extends SugarTest {
	public void setup() throws Exception {	
		sugar().login();
	}

	/**
	 * Check that datetime filter is adjusted for user timezone in reports for cases module
	 * @throws Exception
	 */
	@Test
	public void Reports_23464_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Calculate TimeZone Offset via library to account for DST shift
		FieldSet customData = testData.get(testName).get(0);
		DateTimeZone zone = DateTimeZone.forID(customData.get("timeZone1").substring(0, 15));
		int currentOffsetMilliseconds = zone.getOffset(Instant.now());
		int currentOffsetHours = currentOffsetMilliseconds / (60 * 60 * 1000);
		String timeZone = customData.get("timeZone1") + currentOffsetHours + ":00)";

		// Go to Admin > Profile and set timezone as America/Chicago GMT - 6:00.
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		// Click Advanced tab
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();
		sugar().users.userPref.getControl("advanced_timeZone").scrollIntoView();
		sugar().users.userPref.getControl("advanced_timeZone").set(timeZone);
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Go to Reports > Create Report
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// Create Rows and Columns > Cases
		// TODO: VOOD-822
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Cases").click();

		// Filter: Date Created is today
		new VoodooControl("tr", "id", "Cases_date_entered").click();
		new VoodooControl("select", "css", "[name='qualify']").set(customData.get("filter"));
		new VoodooControl("input", "css", "#nextBtn").click();

		// Select Display name
		new VoodooControl("tr", "id", "Cases_name").click();
		new VoodooControl("tr", "id", "Cases_date_entered").click();
		new VoodooControl("input", "css", "#nextBtn").click();

		// Check Show Query
		new VoodooControl("input", "id", "show_query").click();

		// Click Preview
		new VoodooControl("input", "id", "previewButton").click();
		VoodooUtils.waitForReady();
		DateTime date = DateTime.now();
		String currDate1 = date.toString("yyyy-MM-dd");
		String currDate2 = date.plusDays(1).toString("yyyy-MM-dd");

		// Verify query would have WHERE clause
		String time1 = " 0"+Math.abs(currentOffsetHours)+":00:00";
		String time2 = " 0"+Math.abs(currentOffsetHours + 1)+":59:59'";
		String query = customData.get("querytext1")+currDate1+time1+customData.get("querytext2")+currDate2+time2;
		new VoodooControl("table","id", "query_table").assertContains(query, true);

		// Cancel
		new VoodooControl("input", "css", "#report_details_div #cancelButton").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}