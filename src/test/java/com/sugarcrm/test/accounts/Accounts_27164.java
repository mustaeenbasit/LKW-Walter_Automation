package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class Accounts_27164 extends SugarTest {
	DataSource recordsData;
	ArrayList<Record> contactsRecord;

	public void setup() throws Exception {
		recordsData = testData.get(testName);
		FieldSet values = new FieldSet();

		// Create two accounts
		for(int i = 0; i < recordsData.size(); i++){
			values.put("name", testName + "_" + i);
			sugar().accounts.api.create(values);
			values.clear();
		}

		// Create a Contact without any associated account.
		sugar().contacts.api.create();

		// Create two Contacts 'test Call_1', 'test Call_2'.
		contactsRecord = new ArrayList<Record>();
		for (int i = 0; i < recordsData.size(); i++) {
			contactsRecord.add(sugar().contacts.api.create(recordsData.get(i)));
			values.clear();
		}

		// Login to sugar
		sugar().login();
	}

	/**
	 * Verify the pop-up list only show related records.
	 * @throws Exception
	 */
	@Test
	public void Accounts_27164_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link above created Contacts 'test Call_1', 'test Call_2' to a Account(i.e. Accounts_27164_0)
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecords(contactsRecord);

		// Edit "test Call_1" and check "Reports to" field and see which contacts show there
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(2);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("reportsTo").scrollIntoView();
		sugar().contacts.recordView.getEditField("reportsTo").click();

		// Select link labeled 'Search and Select'
		// TODO: VOOD-795
		new VoodooControl("div", "xpath", "//div[@id='select2-drop']//div[text()='Search and Select...']").click();
		VoodooUtils.waitForReady();

		// Verify that all contacts should be displayed. (Should not be filtered as per the Account name.)
		sugar().contacts.searchSelect.assertContains(recordsData.get(0).get("firstName") + " " + recordsData.get(0).get("lastName"), true);
		sugar().contacts.searchSelect.assertContains(recordsData.get(1).get("firstName") + " " + recordsData.get(1).get("lastName"), true);
		sugar().contacts.searchSelect.assertContains(sugar().contacts.getDefaultData().get("firstName") + " " + sugar().contacts.getDefaultData().get("lastName"), true);

		// Cancel the search and Select view
		sugar().contacts.searchSelect.cancel();

		// Cancel the record from the edit view
		sugar().contacts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}