package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;

public class ListView_16985 extends SugarTest {
	FieldSet accountRecord;
	AccountRecord myAccount;

	public void setup() throws Exception {
		accountRecord = testData.get(testName).get(0);
		myAccount = (AccountRecord) sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Verify cancel of inline edits on Accounts.
	 */
	@Test
	public void ListView_16985_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Account list view and select inline edit button.
		sugar.accounts.navToListView();
		sugar.accounts.listView.editRecord(1);

		// In-line Edit
		sugar.accounts.listView.getEditField(1, "name").set(accountRecord.get("name"));
		sugar.accounts.listView.getEditField(1, "billingAddressCity").set(accountRecord.get("billingAddressCity"));
		sugar.accounts.listView.getEditField(1, "billingAddressCountry").set(accountRecord.get("billingAddressCountry"));
		sugar.accounts.listView.getEditField(1, "workPhone").set(accountRecord.get("workPhone"));

		// TODO: VOOD-662
		//sugar.accounts.listView.getEditField(1, "emailAddress").set(accountRecord.get("emailAddress"));

		// Click "Cancel".
		sugar.accounts.listView.cancelRecord(1);

		// Verify all edited fields show previous values.
		sugar.accounts.listView.verifyField(1, "name", myAccount.get("name"));
		sugar.accounts.listView.verifyField(1, "billingAddressCity", myAccount.get("billingAddressCity"));
		sugar.accounts.listView.verifyField(1, "billingAddressCountry", myAccount.get("billingAddressCountry"));
		sugar.accounts.listView.verifyField(1, "workPhone", myAccount.get("workPhone"));

		// TODO: VOOD-1111
		//sugar.accounts.listView.verifyField(1, "emailAddress", myAccount.get("emailAddress"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}