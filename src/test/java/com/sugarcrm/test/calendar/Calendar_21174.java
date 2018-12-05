package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_21174 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}
	/**
	 * Create daily recurrence call for three occurrences via Calendar.
	 * @throws Exception
	 */
	@Test
	public void Calendar_21174_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Access 08:00 time slot and click on it
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();

		// click on Schedule a call link from warning
		sugar.alerts.getWarning().clickLink(1);

		// Enter call subject, repeat type and repeat occurrences
		FieldSet callData = testData.get(testName).get(0);
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.getEditField("repeatType").set(callData.get("repeatType"));
		sugar.calls.createDrawer.getEditField("repeatOccur").set(callData.get("repeatOccur"));
		sugar.calls.createDrawer.save();

		// Navigate to calls list view
		sugar.calls.navToListView();
		int count = Integer.parseInt(callData.get("repeatOccur"));

		// Verify the recurring call is saved with 3 calls records
		for (int i = 1; i <= count; i++){
			sugar.calls.listView.getDetailField(i, "name").assertEquals(testName, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}