package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_16939 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();

		// Create 41 accounts (test data)
		DataSource toCreate = new DataSource();
		FieldSet toAdd;
		for(int i = 1; i < 42; i++) {
			toAdd = new FieldSet();
			toAdd.put("name", "Account " + i);
			toCreate.add(toAdd);
		}
		sugar.accounts.api.create(toCreate);
	}

	/**
	 * Verify link to "Clear selections"
	 * @throws Exception
	 */
	@Test
	public void ListView_16939_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to list view and click "select all" checkbox.
		sugar.accounts.navToListView();
		sugar.accounts.listView.toggleSelectAll();
		
		VoodooControl message = sugar.accounts.listView.getControl("selectedRecordsAlert");
		
		// Verify the message "You have selected all 20 records in this view. Select all records in the result set." appears.
		message.assertContains("You have selected all 20 records in this view.", true);
		message.assertContains("Select all records", true);
		message.assertContains("in the result set.", true);
		
		// Click on Select all records link and verify Clear selection link exist.
		sugar.accounts.listView.clickSelectAllRecordsLink();
		VoodooUtils.pause(5000);
		message.assertContains("You have selected all 41 records in the result set.", true);
		message.assertContains("Clear selections", true);
		
		// Verify all records are selected on list view and have check mark next to them.
		// TODO: JIRA story VOOD-436 about the ability to assert the checked/unchecked state of a checkbox.
		
		// Click on Clear selections link.
		sugar.accounts.listView.clickClearSelectionsLink();
		VoodooUtils.pause(5000);
		
	    // Verify all the records no longer selected.
		// TODO: JIRA story VOOD-436 about the ability to assert the checked/unchecked state of a checkbox.
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}