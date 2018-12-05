package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Calls_30138 extends SugarTest {
	CallRecord myCall;

	public void setup() throws Exception {
		myCall = (CallRecord) sugar().calls.api.create();
		sugar().login();
	}

	/**
	 * Verify Data should appear in 'Date Created' and 'Date Modified' fields after creating calls record.
	 * @throws Exception
	 */
	@Test
	public void Calls_30138_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Getting today's date
		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");

		// Navigating to the call created above
		myCall.navToRecord();

		// Clicking Show More link 
		sugar().calls.recordView.showMore();

		// Verifying Date Created is displayed with today's date 
		// TODO: VOOD-597: (Need lib support for date created and date modified fields)
		new VoodooControl("div", "css", ".fld_date_entered_by div[data-placement='bottom']")
			.assertContains(todaysDate, true);

		// Verifying Date Modified is displayed with today's date
		new VoodooControl("div", "css", ".fld_date_modified_by div[data-placement='bottom']")
			.assertContains(todaysDate, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}