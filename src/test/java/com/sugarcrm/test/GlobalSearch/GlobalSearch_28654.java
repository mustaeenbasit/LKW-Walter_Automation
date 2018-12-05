package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28654 extends SugarTest {
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		FieldSet accountName = new FieldSet();
		accountName.put("name", customData.get("AccountName"));
		sugar().accounts.api.create(accountName);
		sugar().login();
	}

	/**
	 * Verify that search results are displayed for the actual characters searched. 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28654_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		VoodooControl globalSearchViewTitle = sugar().globalSearch.getControl("headerpaneTitle");
		
		// Type an account name in the globalSearch field and hit Enter
		// TODO: CB-252
		sugar().navbar.getControl("globalSearch").set(customData.get("searchString") + '\uE007');
		VoodooUtils.waitForReady();
		
		// Verify that the Global Search View Title displays 'Search Results for: "abc's corp"'
		globalSearchViewTitle.assertEquals(customData.get("stringInitials") + "\"" + 
				customData.get("searchString") + "\"", true);
		
		// Verify that Global Search View Title does not display 'Search Results for: "abc%27s%20corp"'
		globalSearchViewTitle.assertContains(customData.get("stringInitials") + "\"" + 
				customData.get("incorrectString") + "\"", false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}