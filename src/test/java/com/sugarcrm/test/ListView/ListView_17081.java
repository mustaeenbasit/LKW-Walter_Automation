package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;

public class ListView_17081 extends SugarTest {
	FieldSet accountRecord;

	@Override
	public void setup() throws Exception {
		accountRecord = testData.get("ListView_17081").get(0);
		sugar.login();
	}

	/**
	 * Verify dropdown field widget in create view.
	 */
	@Test
	public void ListView_17081_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Account list view.
		sugar.accounts.navToListView();

		// Click "Create" button.
		sugar.accounts.listView.create();

		// Enter required fields for Accounts.
		sugar.accounts.createDrawer.getEditField("name").set(accountRecord.get("name"));
		sugar.accounts.createDrawer.getEditField("industry").set(accountRecord.get("industry"));
		sugar.accounts.createDrawer.getEditField("type").set(accountRecord.get("type"));
		sugar.accounts.createDrawer.save();

		// Verify Type and Industry selections are saved properly.
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.getDetailField("industry").assertElementContains(accountRecord.get("industry"), true);
		sugar.accounts.recordView.getDetailField("type").assertElementContains(accountRecord.get("type"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}