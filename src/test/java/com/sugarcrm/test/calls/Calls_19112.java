package com.sugarcrm.test.calls;

import com.sugarcrm.sugar.records.CallRecord;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_19112 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();

		// Call record with recurrence
		sugar().navbar.selectMenuItem(sugar().calls, "create" + sugar().calls.moduleNameSingular);
		sugar().alerts.waitForLoadingExpiration();
		sugar().calls.createDrawer.getEditField("name").set(customData.get("name"));
		sugar().calls.recordView.getEditField("repeatType").set(customData.get("repeat_type"));
		sugar().calls.recordView.getEditField("repeatOccurType").set(customData.get("repeatOccurType"));
		sugar().calls.recordView.getEditField("repeatOccur").set(customData.get("repeat_count"));
		sugar().calls.createDrawer.save();
	}

	/**
	 * Verify that "Delete all recurrence" in Call record.
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_19112_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		CallRecord myCall = new CallRecord(customData);
		sugar().calls.listView.clickRecord(1);
		myCall.deleteAllReocurrences();
		sugar().alerts.getWarning().confirmAlert();

		// verify that all record deleted
		sugar().calls.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}