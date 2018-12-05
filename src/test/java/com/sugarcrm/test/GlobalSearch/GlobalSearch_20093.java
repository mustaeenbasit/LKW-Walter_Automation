package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20093 extends SugarTest {
	FieldSet accountsRecord = new FieldSet();

	public void setup() throws Exception {
		// Need to add some records otherwise nothing will always be returned
		// for any search term
		accountsRecord = testData.get(testName + "_Accounts").get(0);
		
		// Creating Notes Record
		FieldSet fs = new FieldSet();
		sugar().notes.api.create();
		
		// Creating Accounts record
		fs.put("name", accountsRecord.get("name"));
		sugar().accounts.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify that global search displays the correct message when no results are found
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20093_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Global Search for a term that does not exist
		sugar().navbar.setGlobalSearch(accountsRecord.get("searchParam"));
		VoodooUtils.waitForReady();

		// Verify 'No results were found' is displayed for search criteria not matching any record
		sugar().navbar.search.getControl("searchResults").assertContains(accountsRecord.get("searchResult"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
