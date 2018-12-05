package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_16975 extends SugarTest {
	DataSource accounts;

	@Override
	public void setup() throws Exception {
		accounts = testData.get("ListView_16975");
		sugar.login();
		sugar.accounts.api.create(accounts);
	}

	/**
	 * Verify "Select all records" message/link does not appear when all records
	 * are already selected.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_16975_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.accounts.navToListView();
		sugar.accounts.listView.toggleSelectAll();

		// Verify message "You have selected all 'n' records in this view. Select all records in the result set." does NOT appear.
		String messageText = testData.get("ListView_16975_message").get(0).get("message");
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertContains(messageText, false);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}