package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17082 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord) sugar().accounts.api.create();
	}

	/**
	 * Verify user can toggle the activity stream and subpanels on the record view
	 * @throws Exception
	 */
	@Test
	public void Accounts_17082_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();

		// Since default view is data view verify the "Related" drop down is present 
		// TODO VOOD-815 Need Filter functionality for modules in Record View
		VoodooControl related_dropdown = new VoodooControl("span", "css", "span[data-voodoo-name='filter-module-dropdown']");
		related_dropdown.assertContains("Related", true);

		// Verify first subpanel
		new VoodooControl("div", "css", "div.filtered.layout_Accounts").assertContains("Member Organizations", true);

		sugar().accounts.recordView.showActivityStream();
		// Verify the word "Module" in filter
		related_dropdown.assertContains("Module", true);

		// Verify Submit button is visible
		sugar().accounts.recordView.activityStream.getControl("submit").assertVisible(true);

		// Make sure you can click data view again.
		sugar().accounts.recordView.showDataView();
		related_dropdown.assertContains("Related", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}