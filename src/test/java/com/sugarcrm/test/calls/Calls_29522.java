package com.sugarcrm.test.calls;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_29522 extends SugarTest {
	LeadRecord leadRecord;

	public void setup() throws Exception {
		leadRecord = (LeadRecord)sugar().leads.api.create();
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that correct number of repeat call is created when create from subpanel
	 * @throws Exception
	 */
	@Test
	public void Calls_29522_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet callData = testData.get(testName).get(0);
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		StandardSubpanel callSub = sugar().contacts.recordView.subpanels.get(sugar().calls.moduleNamePlural);

		// Create Daily Call Record having 3 occurrences
		callSub.create(callData);
		callSub.clickRecord(1);
		sugar().calls.recordView.edit();
		sugar().calls.recordView.showMore();

		// Add one Lead in Guests field.
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(leadRecord);
		sugar().calls.recordView.save();

		// Navigate to Contact Record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		callSub.scrollIntoView();

		// Verify 3 Calls are available in the Subpanel
		int callRecords = Integer.parseInt(callData.get("repeatOccur"));
		String callName = callData.get("name");
		Assert.assertTrue("Total no. of rows not equal to 3", callRecords == callSub.countRows());

		// Verify 3 calls with same name are available in Call Subpanel
		for(int i = 1; i <= callRecords; i++) {
			callSub.getDetailField(i, "name").assertEquals(callName, true);
		}

		// Verify 3 Calls are available in ListView of Calls
		sugar().calls.navToListView();
		Assert.assertEquals(callRecords, sugar().calls.listView.countRows());

		// Verify 3 calls with same name are available in ListView
		for(int i = 1; i <= callRecords; i++) {
			sugar().calls.listView.getDetailField(i, "name").assertEquals(callName, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}