package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_27745 extends SugarTest {
	FieldSet fs;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify that no word "Warning" in the yellow message bar when clicking on a cell in Calendar
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_27745_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.getWarning().waitForVisible();
		// verify that a yellow message bar appears with proper message
		sugar.alerts.getWarning().assertContains(fs.get("message"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}