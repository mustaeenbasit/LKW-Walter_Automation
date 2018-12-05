package com.sugarcrm.test.contacts;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23514 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * View contact_Verify that "Contact Report" page is displayed by clicking "Contact Reports" link in Navigation shortcuts.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23514_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contacts module.Click "View Contact Reports" link in Navigation shortcuts.
		sugar().navbar.selectMenuItem(sugar().contacts, "viewContactReports");
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		VoodooControl contactsCtrl = new VoodooControl("option", "css", "#Reportsadvanced_searchSearchForm  select [value='Contacts']");

		// Verify that the Contacts option is selected/ highlighted in module filter
		assertTrue("The Contacts option is not selected when it should.", contactsCtrl.waitForElement().getCssValue("background-color").contentEquals("rgba(200, 200, 200, 1)"));

		// TODO: VOOD-822
		VoodooControl reportRowCtrl = new VoodooControl("tr", "css", ".list.view .oddListRowS1");
		reportRowCtrl.scrollIntoView();

		// Verify that the Contacts report is listed in Report module
		reportRowCtrl.assertContains(sugar().contacts.moduleNamePlural, true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
