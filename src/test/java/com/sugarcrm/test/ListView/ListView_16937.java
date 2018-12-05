package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_16937 extends SugarTest {
	DataSource accounts;
	
	@Override
	public void setup() throws Exception {
		accounts = testData.get("ListView_16937");
		
		sugar.login();
		sugar.accounts.api.create(accounts);
	}

	/**
	 *  Verify link to "Select all records"
	 * @throws Exception
	 */
	@Test
	public void ListView_16937_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts list view and click "select all" checkbox.
		sugar.accounts.navToListView();
		sugar.accounts.listView.toggleSelectAll();
		
		// Verify the message "You have selected all 20 records in this view. Select all records in the result set." appears.
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertContains("You have selected all 20 records in this view.", true);
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertContains("Select all records", true);
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertContains("in the result set.", true);
		
		sugar.accounts.listView.clickSelectAllRecordsLink();
		// Verify all records are selected on list view and have check mark next
		// to them.
		// TODO: JIRA story VOOD-436 about the ability to assert the
		// checked/unchecked state of a checkbox.
		
		// Click on "More Accounts" to display more records.
		// TODO: JIRA story VOOD-605 about the ability to click on "More accounts.." link.
		new VoodooControl("button", "css", "button[data-action='show-more']").click();
		// Verify all the records are selected.
		// TODO: JIRA story VOOD-436 about the ability to assert the
		// checked/unchecked state of a checkbox.

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}