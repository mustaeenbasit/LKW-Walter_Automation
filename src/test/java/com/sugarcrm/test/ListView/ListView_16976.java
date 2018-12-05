package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_16976 extends SugarTest {
	DataSource accounts;

	@Override
	public void setup() throws Exception {
		accounts = testData.get("ListView_16976");
		sugar.login();
		sugar.accounts.api.create(accounts);
	}

	/**
	 *  Verify message to "Select all" displays the correct count of selected records.
	 * @throws Exception
	 */
	@Test
	public void ListView_16976_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts list view.
		sugar.accounts.navToListView();
		VoodooUtils.pause(5000);
		
		// Click "Show More" link to display 20 more Account records.
		// TODO: JIRA story VOOD-605 about the ability to click on "More accounts.." link.
		new VoodooControl ("button", "css", "button[data-action='show-more']").click();
		VoodooUtils.pause(5000);
		
		// Click the "Select all" checkbox.
		sugar.accounts.listView.toggleSelectAll();
		
		// Verify the message "You have selected all 40 records in this view. Select all records in the result set." appear.
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertContains("You have selected all 40 records in this view.", true);
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertContains("Select all records", true);
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertContains("in the result set.", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
