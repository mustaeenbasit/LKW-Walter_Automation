package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_16932 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();

		// Create 41 accounts (test data)
		DataSource toCreate = new DataSource();
		FieldSet toAdd;
		for(int i = 1; i < 42; i++)
		{
			toAdd = new FieldSet();
			toAdd.put("name", "Account " + i);
			toCreate.add(toAdd);
		}
		
		sugar.accounts.api.create(toCreate);
	}

	@Test
	public void ListView_16932_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.accounts.navToListView();
		sugar.accounts.listView.toggleSelectAll();
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertElementContains("You have selected all 20 records in this view.", true);
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertElementContains("Select all records", true);
		sugar.accounts.listView.getControl("selectedRecordsAlert").assertElementContains("in the result set.", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}