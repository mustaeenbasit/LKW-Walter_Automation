package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22990 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Account Detail - Verify that account list view is displayed after clicking "Accounts" link in top main menu
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22990_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Go to account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click "Accounts" link on the top of main menu
		sugar().accounts.navToListView();

		// Verify that the account list view is displayed
		// TODO: VOOD-1887
		sugar().accounts.listView.assertVisible(true);

		// Remove this block of code after VOOD-1887 is resolved
		// Verifying 'Module Title', 'Create Button' and a record on the list view to ensure user is redirected to the list view
		sugar().accounts.listView.getControl("moduleTitle").assertEquals(sugar().accounts.moduleNamePlural, true);
		sugar().accounts.listView.getControl("createButton").assertEquals(customFS.get("createButton"), true);
		Assert.assertTrue("User is now on Accounts recordview and 1 record is exist", sugar().accounts.listView.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}