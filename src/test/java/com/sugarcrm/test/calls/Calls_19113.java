package com.sugarcrm.test.calls;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Calls_19113 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();

		// Create call & save
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.recordView.getEditField("name").set(testName);
		sugar().calls.recordView.getEditField("date_start_date").set(customData.get("date_start_date"));
		sugar().calls.recordView.getEditField("repeatType").set(customData.get("repeatType"));
		sugar().calls.recordView.getEditField("repeatOccurType").set(customData.get("repeatOccurType"));
		sugar().calls.recordView.getEditField("repeatUntil").set(customData.get("date_end"));
		sugar().calls.createDrawer.save();
	}

	/**
	 * Verify that "Edit All Recurrence" in Call record.
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_19113_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");


		CallRecord myCall = new CallRecord(customData);
		// View call and edit record
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);

		// Click on action drop down, select "Edit All Recurrences".
		myCall.editAllReocurrenses();
		// Change 'Repeat Interval' to 10 and 'Repeat Until' to 4 weeks date from now
		sugar().calls.recordView.getEditField("repeatInterval").set(customData.get("count2"));
		sugar().calls.recordView.getEditField("repeatUntil").set(customData.get("date_end"));
		sugar().calls.recordView.save();

		// Verify that records are updated in listview (assert date fields)
		sugar().calls.navToListView();
		sugar().calls.listView.sortBy("headerDatestart", false);
		for (int i = 1; i <= 4; i++) {
			sugar().calls.listView.getDetailField(i, "date_start_date").assertContains(customData.get("date_" + i), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}