package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17092 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Expand/collapse a specific subpanel (Updated the test case as per v7.7)
	 * @throws Exception
	 */
	@Test
	public void Accounts_17092_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.clickRecord(1);

		StandardSubpanel callSubpanel = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);

		// TODO: VOOD-1517 - Investigate verification of Header Column Display Names
		VoodooControl callSubjectHeader = new VoodooControl("th", "css", "[data-voodoo-name='Calls'] th[data-fieldname='name']");

		// Open the calls subpanel
		callSubpanel.getControl("toggleSubpanel").click();

		// Assert that the Subject header is visible
		callSubjectHeader.assertVisible(true);

		// Close the subpanel
		callSubpanel.getControl("toggleSubpanel").click();

		// Assert that the Header is not visible
		callSubjectHeader.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}