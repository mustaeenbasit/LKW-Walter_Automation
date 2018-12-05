package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28054 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that the Search results should get disappeared
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28054_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter value in global search bar and click enter
		sugar().navbar.getControl("globalSearch").set(myAccount.getRecordIdentifier() + '\uE007');
		VoodooUtils.waitForReady();

		// Clicking on account record in Global Search window 
		sugar().globalSearch.getRow(1).assertVisible(true);
		sugar().globalSearch.clickRecord(1);
		
		// Verify that the search results should be disappear and account record is appears.
		sugar().globalSearch.getRow(1).assertVisible(false);
		sugar().accounts.recordView.assertVisible(true);
		sugar().accounts.recordView.getDetailField("name").assertEquals(myAccount.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}