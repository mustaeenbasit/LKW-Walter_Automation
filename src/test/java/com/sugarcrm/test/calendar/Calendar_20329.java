package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooDate;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar_20329 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Log Call [form shortcut bar] (from calendar Grid) Verify that warning message is 
	 * (NOT) displayed to indicate that the duration is 0h 00m.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20329_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		SimpleDateFormat sdFmt = new SimpleDateFormat("MM/dd/yyyy");

		// TODO: VOOD-863 need support for Calendar module.
		// Click Day button
		sugar.navbar.navToModule("Calendar");
		VoodooUtils.focusFrame("bwc-frame");		
		new VoodooControl("input", "id", "day-tab").click();
		
		// Access 08:00 time slot and click Schedule Call from warning 
		new VoodooControl("div", "css", ".week div[time='"+ customData.get("callTime") +"']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.getWarning().clickLink(1);
		
		// Enter Call Subject data, end time and save.
		sugar.calls.createDrawer.getEditField("name").set(testName);
		new VoodooDate("input", "css", ".fld_date_end.edit input[data-type='time']").set(customData.get("callTime"));
		sugar.calls.createDrawer.getControl("saveButton").click();	
		sugar.alerts.getSuccess().waitForVisible();
		
		// Assert that a Call with 0 min duration is successfully created
		sugar.alerts.getSuccess().assertEquals(customData.get("alertMessage") + " " + testName 
				+" for "+  sdFmt.format(new Date()) +" "+ customData.get("callTime")+".",true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}