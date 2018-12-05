package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20327 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Log call from calendar page.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20327_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Access 08:00 time slot and click Schedule Call from warning
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.getWarning().clickLink(1);

		// Call Subject data and save.
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.save();

		// Verify that the Log Call is displayed in the selected time slot with the same subject entered
		// TODO: VOOD-863
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".week div[time='08:00am'] div.head div:nth-child(2)").assertContains(testName, true);
		new VoodooControl("div", "css", ".week div[time='08:00am'] .head .adicon").click();
		VoodooUtils.waitForReady();

		// Verify the Call subject at the Additional Detail page
		new VoodooControl("div", "css", ".ui-dialog-content").assertContains(testName, true);

		// Click on the view icon on the Additional Detail page
		new VoodooControl("a", "css", ".ui-dialog-title a[title='View']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that the Call detail information is displayed on "Calls Detail View" page.
		sugar.calls.recordView.getDetailField("name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}