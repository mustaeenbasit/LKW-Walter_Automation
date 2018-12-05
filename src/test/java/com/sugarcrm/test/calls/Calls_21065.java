package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21065 extends SugarTest {
	public void setup() throws Exception {
		// Existing Calls record are needed. Call status should be "Scheduled".
		sugar().calls.api.create();

		// Login
		sugar().login();
	}

	/**
	 * Call status changes to "Held" when close icon is selected for call
	 * @throws Exception
	 */
	@Test
	public void Calls_21065_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Navigate to Calls list view
		sugar().calls.navToListView();

		// In list view Open Row Action drop down for any call record and click on "Close"
		sugar().calls.listView.openRowActionDropdown(1);
		// TODO: VOOD-742
		VoodooControl closeCtrl = new VoodooControl("a", "css", ".fld_record-close.list a");
		closeCtrl.click();

		// Observe the success pop-up message about close the call with message "Success Call marked as held."
		sugar().alerts.getSuccess().assertContains(customFS.get("successMessage"), true);

		// "Close" option is no longer exist for the record
		sugar().calls.listView.openRowActionDropdown(1);
		closeCtrl.assertVisible(false);
		sugar().calls.listView.getControl("dropdown01").click(); // Need to close the row action drop down to remove hover

		// Verify that Status of the Call record change to Held in Call's list view
		sugar().calls.listView.getDetailField(1, "status").assertEquals(customFS.get("status"), true);

		// Navigate to the detail view of the Call record view
		sugar().calls.listView.clickRecord(1);

		// Verify that Status of the Call record change to Held in Call's list view and detail view
		sugar().calls.recordView.getDetailField("status").assertEquals(customFS.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}