package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_16938 extends SugarTest {
	DataSource accounts;

	@Override
	public void setup() throws Exception {
		accounts = testData.get("ListView_16938");
		sugar.login();
		sugar.accounts.api.create(accounts);
	}

	/**
	 * Verify unchecking "select all"
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_16938_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts list view and click "select all" checkbox.
		sugar.accounts.navToListView();
		sugar.accounts.listView.toggleSelectAll();

		// Verify the message
		// "You have selected all 20 records in this view. Select all records in the result set." appears.
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertContains("You have selected all 20 records in this view.", true);
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertContains("Select all records", true);
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertContains("in the result set.", true);

		sugar.accounts.listView.clickSelectAllRecordsLink();
		// Verify all records are selected on list view and have check mark next
		// to them.
		// TODO: JIRA story VOOD-436 about the ability to assert the
		// checked/unchecked state of a checkbox.

		// Click on checkbox to unselect all records.
		sugar.accounts.listView.toggleSelectAll();
		// Verify all the records no longer selected.
		// TODO: JIRA story VOOD-436 about the ability to assert the
		// checked/unchecked state of a checkbox.

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}