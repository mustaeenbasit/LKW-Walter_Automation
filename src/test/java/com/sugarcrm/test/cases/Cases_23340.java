package com.sugarcrm.test.cases;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23340 extends SugarTest {

	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Test Case 23340: Create Contact_Verify that contact for case can be created in "Contacts" sub-panel.
	 */
	@Test
	public void Cases_23340_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(sugar().cases.moduleNamePlural);
		sugar().cases.listView.clickRecord(1);

		StandardSubpanel contactSubpanel = sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.scrollIntoView();
		// Click "Create" button in "Contacts" sub-panel.
		contactSubpanel.addRecord();

		// Create contact for the selected case.
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Verify contact created for the selected case is displayed in "Contacts" sub-panel.
		contactSubpanel.expandSubpanel();
		contactSubpanel.getDetailField(1, "fullName").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
