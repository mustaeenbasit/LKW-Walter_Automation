package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20319 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Display calendar_Verify that Year Calendar panel is displayed when clicking "Year" button on "Calendar" 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20319_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet yearData = testData.get(testName).get(0);

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Click "Year" button on "Calendar" page.
		// TODO: VOOD-863
		new VoodooControl("input", "id", "year-tab").click();

		// verify Year calendar page by check wording: Previous/Next Year
		VoodooControl monthHeaderCtrl = new VoodooControl("div", "css", ".monthHeader");
		monthHeaderCtrl.assertContains(yearData.get("previousYear"), true);
		monthHeaderCtrl.assertContains(yearData.get("nextYear"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}