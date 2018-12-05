package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20360 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.quotes.api.create();
		sugar.login();
	}

	/**
	 * Verify that call record is displayed in "Activities" sub-panel of "Quote" detail view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20360_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.quotes.navToListView();
		// Open a Quote detail view
		sugar.quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// In the "Activities" sub panel, log a call for the current day.
		// TODO: VOOD-826
		new VoodooControl("span", "css", "#list_subpanel_activities tr td:nth-child(1) ul li span").click();
		new VoodooControl("a", "id", "Activities_logcall_button_create_").click();
		VoodooUtils.focusDefault();
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.getEditField("date_start_time").set(customData.get("date_start_time"));
		sugar.calls.createDrawer.save();

		VoodooUtils.focusFrame("bwc-frame");
		// Verify The scheduled call record is displayed in "Activities" sub-panel.
		// TODO: VOOD-826
		new VoodooControl("div", "css", "#list_subpanel_activities").assertContains(testName, true);
		VoodooUtils.focusDefault();

		// Navigate to Calendar module.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		// Verify the call is displayed on the current day of Calendar
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".slot[datetime='"+todaysDate+" "+ customData.get("date_start_time")+"']").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}