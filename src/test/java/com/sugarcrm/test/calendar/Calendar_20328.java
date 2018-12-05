package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20328 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Schedule call_Verify that call is scheduled at a special time 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20328_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-863 need support for Calendar module.
		// Click Day button
		sugar.navbar.navToModule("Calendar");
		VoodooUtils.focusFrame("bwc-frame");		
		new VoodooControl("input", "id", "day-tab").click();

		// Access 08:00 time slot and click Schedule Call from warning 
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.getWarning().clickLink(1);

		// Call Subject data and save.
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.save();

		// Click view icon to open call record in detail view.
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".week div[time='08:00am'] .head .adicon").click();
		new VoodooControl("a", "css", ".ui-dialog-title a[title='View']").click();

		// Verify that call is displayed.
		sugar.calls.recordView.getDetailField("name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}