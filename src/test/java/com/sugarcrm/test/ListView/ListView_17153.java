package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class ListView_17153 extends SugarTest {
	AccountRecord associatedAccount;

	public void setup() throws Exception {
		associatedAccount = (AccountRecord)sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Related-to widget select value from "search for more" on create page
	 * @throws Exception
	 */
	@Test
	public void ListView_17153_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet searchSelect = testData.get(testName).get(0);
		sugar.navbar.navToModule(sugar.contacts.moduleNamePlural);
		sugar.contacts.listView.create();
		VoodooSelect relatedAccount = (VoodooSelect)sugar.contacts.createDrawer.getEditField("relAccountName");
		sugar.contacts.createDrawer.getEditField("relAccountName").click();

		// Assert that a "search" field appears and the "Search for more..." link appears below search field
		relatedAccount.selectWidget.getControl("searchBox").assertVisible(true);

		// Click the "search for more..." link
		relatedAccount.selectWidget.getControl("searchForMoreLink").click();
		VoodooUtils.waitForReady();

		// Assert "Search and select Accounts" page appears displaying all the available "Accounts" with radio buttons before each record.
		sugar.accounts.searchSelect.assertVisible(true);

		// Assert Module Title
		sugar.accounts.searchSelect.getControl("moduleTitle").assertContains(searchSelect.get("accountSearchAndSelectTitle"), true);
		sugar.accounts.searchSelect.search(associatedAccount.getRecordIdentifier());

		// TODO: VOOD-1487 - Need lib support for verification of sugar-fields on SSV
		new VoodooControl("input", "css", ".fld_Accounts_select input[type='radio']").assertVisible(true);
		new VoodooControl("span", "css", ".layout_Accounts .single:nth-child(1) [data-voodoo-name='name']").assertContains(associatedAccount.getRecordIdentifier(), true);

		// Click the radio button on one of the listed accounts
		sugar.accounts.searchSelect.selectRecord(associatedAccount);
		VoodooUtils.waitForReady();
		sugar.alerts.getWarning().cancelAlert();

		// Assert that the selected account is displayed on "Account Name" field while still in edit mode
		relatedAccount.assertEquals(associatedAccount.getRecordIdentifier(), true);
		sugar.contacts.createDrawer.getControl("saveButton").assertVisible(true);

		// Create a Contact
		sugar.contacts.createDrawer.getEditField("lastName").set(sugar.contacts.getDefaultData().get("lastName"));
		sugar.contacts.createDrawer.save();

		// Verify the Contact full name and related Account name
		sugar.contacts.listView.verifyField(1, "fullName", sugar.contacts.getDefaultData().get("lastName"));
		sugar.contacts.listView.verifyField(1, "relAccountName", associatedAccount.getRecordIdentifier());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}