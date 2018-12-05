package com.sugarcrm.test.calls;

import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_29917 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that User does not find 'Repeat Until' date as blank in Calls
	 * @throws Exception
	 */
	@Test
	public void Calls_29917_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calculating the values for Current date & Repeat Until date
		DateTime date = DateTime.now();
		FieldSet callData = testData.get(testName).get(0);
		String currentDate = date.toString("MM/dd/yyyy");
		String repeatUntilDate = date.plusDays(3).toString("MM/dd/yyyy");

		// Create a custom call record
		FieldSet fs = new FieldSet();
		fs.put("date_start_date", currentDate);
		fs.put("date_end_date", currentDate);
		fs.put("repeatType", callData.get("repeatType"));
		fs.put("repeatInterval", callData.get("repeatInterval"));
		fs.put("repeatUntil",repeatUntilDate);
		sugar().calls.create(fs);

		// Sorting the calls with Start Date
		// TODO: TR-10791 - Recursive calls/meeting records are not coming in correct reverse chronological
		// order in list view (Hinders Automation)
		sugar().calls.listView.sortBy("headerDatestart", true);

		// Calculating the no. of rows in Calls list view
		int rowCount = sugar().calls.listView.countRows();
		String callName = sugar().calls.getDefaultData().get("name");

		// Verify 4 calls with same name  and recursive start date created on Calls list view
		for (int i = 1; i <= rowCount; i++) {
			sugar().calls.listView.verifyField(i, "name", callName );
			sugar().calls.listView.verifyField(i, "date_start_date", date.plusDays(i-1).toString("MM/dd/yyyy"));
		}

		// Clicking on last recursive call record
		sugar().calls.listView.clickRecord(rowCount);
		sugar().calls.recordView.edit();
		sugar().calls.recordView.save();

		// Asserting that 'Repeat Until' date doesn't go blank in recursive call record on Edit & Save
		sugar().calls.recordView.getDetailField("repeatUntil").assertEquals(repeatUntilDate, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}