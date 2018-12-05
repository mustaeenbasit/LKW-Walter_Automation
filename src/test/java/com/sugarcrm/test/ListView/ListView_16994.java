package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_16994 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		sugar.accounts.api.create();
		customData = testData.get(testName).get(0);
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * Edit inline record - Special characters in inline edit fields
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_16994_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Accounts module to display list view.
		sugar.accounts.navToListView();
		// Click edit button of a record.
		sugar.accounts.listView.editRecord(1);
		// Enter special characters 
		sugar.accounts.listView.getEditField(1, "name").set(customData.get("data"));
		sugar.accounts.listView.getEditField(1, "billingAddressCountry").set(customData.get("data"));
		sugar.accounts.listView.getEditField(1, "billingAddressCity").set(customData.get("data"));
		sugar.accounts.listView.getEditField(1, "workPhone").set(customData.get("data"));
		// Save
		sugar.accounts.listView.saveRecord(1);
		// Verify if characters are valid
		sugar.accounts.listView.getDetailField(1, "name").assertContains(customData.get("data"), true);
		sugar.accounts.listView.getDetailField(1, "billingAddressCountry").assertContains(customData.get("data"), true);
		sugar.accounts.listView.getDetailField(1, "billingAddressCity").assertContains(customData.get("data"), true);
		sugar.accounts.listView.getDetailField(1, "workPhone").assertContains(customData.get("data"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}