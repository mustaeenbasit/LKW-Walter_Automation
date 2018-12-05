package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_29046 extends SugarTest {

	public void setup() throws Exception {
		sugar().calls.api.create();
		sugar().login();
	}

	/**
	 * Verify that user should be able to edit the fields through Inline Editor for Calls Module
	 * @throws Exception
	 */
	@Test
	public void Calls_29046_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet callStatus = testData.get(testName).get(0);
		VoodooControl statusValue = sugar().calls.recordView.getDetailField("status");

		// Navigate to the call record view
		sugar().navbar.navToModule(sugar().calls.moduleNamePlural);
		sugar().calls.listView.clickRecord(1);

		// Assert that, call status is Scheduled before edit
		statusValue.assertEquals(sugar().calls.getDefaultData().get("status"), true);

		// Inline edit the status field
		// TODO: VOOD-854 - Need lib support for inline edit on Record view
		new VoodooControl ("span", "css", "span[data-voodoo-name='status']").hover();
		new VoodooControl ("i", "css", "span[data-name='status'] .fa-pencil").click();

		// Changing the status to "Canceled"
		// TODO: VOOD-1891 - getEditField().set() method does not work when inline edit the select2 fields(i.e in the HeaderPane) in the recordView 
		new VoodooControl("li", "css", ".select2-drop-active .select2-results li:nth-child(3)").click();

		// Save and Assert that the status has been changed.
		sugar().calls.recordView.save();
		statusValue.assertEquals(callStatus.get("statusCanceled"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}