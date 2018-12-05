package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class RecentlyViewedTests extends SugarTest {
	AccountRecord myAccount1, myAccount2;

	public void setup() throws Exception {
		sugar().login();
		myAccount1 = (AccountRecord)sugar().accounts.create();

		FieldSet secondAccount = new FieldSet();
		secondAccount.put("name", "Second Account");
		myAccount2 = (AccountRecord)sugar().accounts.create(secondAccount);
	}

	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-508 - Once resolved commented code snippet will work
		// Check clickRecentlyViewed using (module, int) works
		//sugar().accounts.navToListView();
		//sugar().navbar.clickRecentlyViewed(sugar().accounts, 1);
		//sugar().accounts.getField("name").detailControl.assertEquals(myAccount2.getRecordIdentifier(), true);

		// Check clickRecentlyViewed using (module, string) works
		sugar().accounts.navToListView();
		sugar().navbar.clickRecentlyViewed(sugar().accounts, myAccount1.getRecordIdentifier());
		sugar().accounts.getField("name").detailControl.assertEquals(myAccount1.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}