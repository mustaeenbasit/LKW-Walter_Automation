package com.sugarcrm.test.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20322 extends SugarTest {
	String todaysDate = new String();

	public void setup() throws Exception {
		FieldSet startDate = new FieldSet();
		todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		startDate.put("date_start_date",todaysDate);
		startDate.put("date_start_time","08:00am");
		sugar().calls.api.create(startDate);
		sugar().login();
	}

	/**
	 * Verify that call detail view page is displayed when clicking the call subject link in Shared Calendar panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20322_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab.
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Open Shared Calendar
		// TODO: VOOD-863: Lib support for Calendar module
		new VoodooControl("input", "id", "shared-tab").click();
		sugar().alerts.waitForLoadingExpiration();	

		// Click Call subject link in the Calendar view.
		VoodooControl callCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + todaysDate + " 08:00am']");
		callCtrl.assertContains(sugar().calls.getDefaultData().get("name"), true);
		callCtrl.click();

		// Assert Calls detail view is visible
		sugar().calls.recordView.assertVisible(true);
		sugar().calls.recordView.getDetailField("name").assertEquals(sugar().calls.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}