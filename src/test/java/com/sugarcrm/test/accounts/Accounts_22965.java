package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22965 extends SugarTest {
	StandardSubpanel memberOrganizations;

	public void setup() throws Exception {
		// Create two Account records with different name
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create(fs);
		sugar().accounts.api.create();
		sugar().login();

		// Go to an account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Existing account member record related to this account needed. 
		memberOrganizations = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		memberOrganizations.scrollIntoViewIfNeeded(false);
		memberOrganizations.linkExistingRecord(myAccount);
	}

	/**
	 * Verify that only the relationship between the account member and the account is removed by clicking "rem" icon.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22965_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Already in Account > recordView
		// Click "Unlink" icon on the right edge of a account member record on "MEMBER ORGANIZATIONS" sub-panel.
		memberOrganizations.unlinkRecord(1);

		// TODO: VOOD-1424
		// Verify that this related account member record is removed from "MEMBER ORGANIZATIONS" sub-panel. 
		new VoodooControl("tr", "css", "[data-subpanel-link='members'] tbody tr:nth-of-type(1)").assertExists(false);
		Assert.assertTrue("The subpanel is empty", memberOrganizations.isEmpty());

		// Go to Accounts listView
		sugar().accounts.navToListView();

		// Search the recently un-linked account member record in the list view of Account Module.
		sugar().accounts.listView.setSearchString(testName);

		// Verify that the account member record is still displayed on the account list view under account module.
		sugar().accounts.listView.verifyField(1, "name", testName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}