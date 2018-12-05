package com.sugarcrm.test.accounts;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22911 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().login();

		// TODO: VOOD-444 - Support creating relationships via API
		// related contact to account
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getWarning().cancelAlert();	
		sugar().contacts.recordView.save();

		// Navigate to the Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
	}

	/**
	 * Verify that only the relationship between the contact and the account is removed by clicking "unlink" action.
	 * @throws Exception
	 */
	@Test
	public void Accounts_22911_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.expandSubpanel();
		contactSubpanel.unlinkRecord(1);

		// Verify contact record unlink from accounts
		assertTrue("The contact record has not been unlinked yet", contactSubpanel.isEmpty());

		// To verify this record is unlink only from accounts, not from contact list
		sugar().contacts.navToListView();
		sugar().contacts.listView.setSearchString(sugar().contacts.getDefaultData().get("lastName"));
		sugar().contacts.listView.getDetailField(1, "fullName").assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}