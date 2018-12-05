package com.sugarcrm.test.calls;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_27351 extends SugarTest {
	String todaysDate = "";

	public void setup() throws Exception {
		// Date of three days after today.
		todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		FieldSet fs = new FieldSet();
		fs.put("date_start_date", todaysDate);
		sugar().calls.api.create(fs);

		// Login as a valid user
		sugar().login();
	}

	/**
	 * Verify that duration is longer than 1 day can be saved in Call.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_27351_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet callsFieldSet = testData.get(testName).get(0);

		// Create/Edit a weekly occurring Call record 
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();
		String dateAfter3Days = DateTime.now().plusDays(3).toString("MM/dd/yyyy");
		sugar().calls.recordView.getEditField("date_end_date").set(dateAfter3Days);
		sugar().calls.recordView.getEditField("repeatType").set(callsFieldSet.get("repeat_type"));
		sugar().calls.recordView.getEditField("repeatOccurType").set(callsFieldSet.get("until"));
		sugar().calls.recordView.getEditField("repeatUntil").set(DateTime.now().plusDays(10).plusMonths(3).toString("MM/dd/yyyy"));
		// TODO: VOOD-1582 - Need lib support to getText() of some controls (Special cases).
		String startTime = sugar().calls.recordView.getEditField("date_start_time").getText();
		String endTime = sugar().calls.recordView.getEditField("date_end_time").getText();
		sugar().calls.recordView.save();

		// Verify the duration of the call is displayed in the "Start & End Date" field
		sugar().calls.recordView.getDetailField("date_start_date").assertContains(todaysDate + " " + startTime + " - " + dateAfter3Days + " " + endTime, true);

		// Go to listview of the Calls. Select a child call and open the record view.
		// Using XPath to select calls by unique date.
		sugar().calls.navToListView();
		String startDateForNextCall = DateTime.now().plusDays(7).toString("MM/dd/yyyy");
		VoodooControl childCall = new VoodooControl("a", "xpath", "//*[@id='content']/div/div/div[1]/span/div/div[2]/div[2]/div[2]/div[3]/div[1]/table/tbody/tr[contains(.,'" + startDateForNextCall + "')]/td[2]/span/div/a");
		childCall.scrollIntoView();
		childCall.click();

		// Verify the duration of the call is displayed in the "Start & End Date" field
		sugar().calls.recordView.getDetailField("date_start_date").assertContains(startDateForNextCall + " " + startTime + " - " + DateTime.now().plusDays(10).toString("MM/dd/yyyy") + " " + endTime, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}