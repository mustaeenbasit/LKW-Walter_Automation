package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17014 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify auto duplicate check on account name while creating a new account
	 * @throws Exception
	 */
	@Test
	public void Accounts_17014_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.createDrawer.save();
		
		// TODO: VOOD-513 -need lib support for duplicate check panel
		VoodooControl duplicatCtrl = sugar().accounts.createDrawer.getControl("duplicateCount");
		VoodooControl dupListViewCtrl = new VoodooControl("div","css","div[data-voodoo-name='dupecheck-list-edit']");
		
		// Verify that the duplicate found panel is shown
		duplicatCtrl.waitForVisible();
		duplicatCtrl.assertVisible(true);
		dupListViewCtrl.waitForVisible();
		dupListViewCtrl.assertVisible(true);

		// Should found all the matched accounts with Name Matches EXACTLY. 
		new VoodooControl("div", "css", "div[data-voodoo-name='dupecheck-list-edit'] .fld_name.list").assertContains(
				myAccount.getRecordIdentifier(), true);
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}