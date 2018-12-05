package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_16984 extends SugarTest {
	FieldSet accountRecord;

	public void setup() throws Exception {
		accountRecord = testData.get(testName).get(0);
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Verify user able to perform inline edit for Account module
	 */
	@Test
	public void ListView_16984_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Account list view and select inline edit button.
		sugar.accounts.navToListView();

		// TODO: VOOD-662
		sugar.accounts.listView.toggleSidebar(); // Remove this code once VOOD-662 is fixed
		sugar.accounts.listView.editRecord(1);

		// list view.
		sugar.accounts.listView.getEditField(1, "name").set(accountRecord.get("name"));
		sugar.accounts.listView.getEditField(1, "billingAddressCity").set(accountRecord.get("billingAddressCity"));
		sugar.accounts.listView.getEditField(1, "billingAddressCountry").set(accountRecord.get("billingAddressCountry"));
		sugar.accounts.listView.getEditField(1, "workPhone").set(accountRecord.get("workPhone"));
		sugar.accounts.listView.getEditField(1, "emailAddress").set(accountRecord.get("emailAddress"));

		// Click "Save".
		sugar.accounts.listView.saveRecord(1);

		// Verify all edited fields show update values.
		sugar.accounts.listView.verifyField(1, "name",
				accountRecord.get("name"));
		sugar.accounts.listView.verifyField(1, "billingAddressCity",
				accountRecord.get("billingAddressCity"));
		sugar.accounts.listView.verifyField(1, "billingAddressCountry",
				accountRecord.get("billingAddressCountry"));
		sugar.accounts.listView.verifyField(1, "workPhone",
				accountRecord.get("workPhone"));
		sugar.accounts.listView.verifyField(1, "emailAddress",
				accountRecord.get("emailAddress"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}