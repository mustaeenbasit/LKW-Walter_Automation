package com.sugarcrm.test.meetings;

import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27252 extends SugarTest {	

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that interval is >=1 for recurring meetings.
	 * @throws Exception
	 */
	@Test
	public void Meetings_27252_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Meetings create drawer
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		FieldSet customData = testData.get(testName).get(0);
		sugar().meetings.createDrawer.getEditField("repeatType").set(customData.get("repeat_type"));

		// TODO: VOOD-1463 - Need a lib support to assert values in dropdown field's on BWC/Sidecar modules
		sugar().meetings.createDrawer.getEditField("repeatInterval").click();
		VoodooControl repeatIntervalDropdown = new VoodooControl("ul", "css", ".select2-drop-active.select2-with-searchbox .select2-results");

		// Verify, in "Repeat Interval", starting number is 1 and increase by 1 until to 99.
		for (int i = 1; i < 100; i++) {
			repeatIntervalDropdown.assertContains(Integer.toString(i), true);
		}

		// Fill Repeat Interval = 2
		new VoodooControl("li", "css", ".select2-drop-active.select2-with-searchbox .select2-results li:nth-child(2)").click();

		// Fill Repeat Occurrence = 3 times.
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(customData.get("repeatOccurType"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(customData.get("repeat_count"));
		sugar().meetings.createDrawer.save();

		// Date of two days and five days after today
		DateTime date = DateTime.now();
		String todayDate = date.toString("MM/dd/yyyy");
		String dateAfter2Days = date.plusDays(2).toString("MM/dd/yyyy");
		String dateAfter5Days = date.plusDays(4).toString("MM/dd/yyyy");

		// Verify, 3 recurring meetings are generated. The first one is from today.
		sugar().meetings.listView.sortBy("headerDatestart", true);
		sugar().meetings.listView.getDetailField(1, "date_start_date").assertContains(todayDate, true);
		sugar().meetings.listView.getDetailField(2, "date_start_date").assertContains(dateAfter2Days, true);
		sugar().meetings.listView.getDetailField(3, "date_start_date").assertContains(dateAfter5Days, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}