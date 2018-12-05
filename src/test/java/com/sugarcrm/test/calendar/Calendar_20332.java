package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20332 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Access 08:00 time slot and click Schedule Call from warning
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.getWarning().clickLink(1);

		// Schedule a Call, enter the "Subject" and save.
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.save();
	}

	/**
	 * Edit call_Verify that call editing is canceled after clicking "Cancel" button in edit view.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20332_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet callName = testData.get(testName).get(0);

		// Verify the Call subject and click call subject in Calendar view.
		// TODO: VOOD-863
		VoodooControl calendarCall = new VoodooControl("div", "css", ".week div[time='08:00am'] div.head div:nth-child(2)");
		VoodooUtils.focusFrame("bwc-frame");
		calendarCall.assertContains(testName, true);
		calendarCall.click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();

		// Update call information (Subject).
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("name").set(callName.get("name"));

		// Click "Cancel" button.
		sugar.calls.recordView.cancel();

		// Verify that the Call information is not updated. The original information (Subject) is displayed in Detail View.
		sugar.calls.recordView.getDetailField("name").assertEquals(testName, true);

		// Verify that the Call information is not updated. The original information (Subject) is displayed in Calendar view.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		calendarCall.assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}