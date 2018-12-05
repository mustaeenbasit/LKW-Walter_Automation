package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22679 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * In-Line Create Call_Verify that call can be in-line created from Activities sub-panel for a lead.
	 * @throws Exception
	 */
	@Test
	public void Leads_22679_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the Leads module and navigate to the existing lead record
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);

		// Click the "+" icon in the Calls subpanel on the Lead record view
		StandardSubpanel callSubpanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callSubpanel.addRecord();
		String callName = sugar().calls.getDefaultData().get("name");
		FieldSet callStatus = testData.get(testName).get(0);
		String statusHeld = callStatus.get("statusHeld");

		// Enter the name and the status in the call create form and click save
		sugar().calls.createDrawer.getEditField("name").set(callName);
		sugar().calls.createDrawer.getEditField("status").set(statusHeld);
		sugar().calls.createDrawer.save();

		// Assert that the call is created in the subpanel
		callSubpanel.getDetailField(1, "name").assertEquals(callName, true);

		// Click subject column for the call record
		callSubpanel.clickRecord(1);

		// Assert the call record view is displayed with the data entered in create page
		sugar().calls.recordView.getDetailField("name").assertEquals(callName, true);
		sugar().calls.recordView.getDetailField("status").assertEquals(statusHeld, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}