package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.test.SugarTest;

public class Calls_29530 extends SugarTest {
	FieldSet callData = new FieldSet();

	public void setup() throws Exception {
		callData = testData.get(testName).get(0);
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		fs.put("status", callData.get("status"));
		sugar().calls.api.create(fs);
		sugar().calls.api.create();
		sugar().login();
	}

	/**
	 * Verify that a call can be closed from the list view
	 * @throws Exception
	 */
	@Test
	public void Calls_29530_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Call's list view
		sugar().calls.navToListView();

		// Asserting the first calls status
		VoodooControl firstCallStatus = sugar().calls.listView.getDetailField(1, "status");
		firstCallStatus.assertEquals(sugar().calls.getDefaultData().get("status"), true);

		// Opening the Row Action Dropdown for first record and clicking on close option
		// TODO: VOOD-742
		sugar().calls.listView.openRowActionDropdown(1);
		VoodooControl closeCtrl = new VoodooControl("a", "css", ".fld_record-close.list [class='rowaction']");
		closeCtrl.click();

		// Verify the success pop-up message "Success Call marked as held."
		String successMessage = callData.get("successMessage");
		String changedStatus = callData.get("changedStatus");
		VoodooControl alertCtrl = sugar().alerts.getSuccess();
		alertCtrl.assertContains(successMessage, true);
		((Alert) alertCtrl).closeAlert();

		// Verify that Status of first Call record change to Held in Call's list view
		firstCallStatus.assertEquals(changedStatus, true);

		// Asserting the second calls status
		VoodooControl secondCallStatus = sugar().calls.listView.getDetailField(2, "status");
		secondCallStatus.assertEquals(callData.get("status"), true);

		// Opening the Row Action Dropdown for second record and clicking on close option
		sugar().calls.listView.openRowActionDropdown(2);
		closeCtrl.click();

		// Verify the success pop-up message "Success Call marked as held."
		alertCtrl.assertContains(successMessage, true);

		// Verify that Status of second Call record change to Held in Call's list view
		secondCallStatus.assertEquals(changedStatus, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}