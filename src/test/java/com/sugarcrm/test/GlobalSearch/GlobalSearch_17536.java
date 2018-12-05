package com.sugarcrm.test.GlobalSearch;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_17536 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		// Create Five contact records
		customDS = testData.get(testName+"_contactData");
		sugar().contacts.api.create(customDS);
		sugar().login();
	}

	/**
	 * Display up to 5 results in global search results dropdown
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_17536_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Input data in global search box, make sure there are some records match the search criteria
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();
		globalSearchCtrl.set(customDS.get(0).get("lastName").substring(0, 4));
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1849
		// Verify, show up to 5 records in the search result dropdown
		Assert.assertTrue("Not show 5 records in the Search result dropdown", new VoodooControl("li", "xpath", "//*[@id='header']/div/div/div/div[2]/div[3]/div/ul/li[contains(.,'" + customDS.get(0).get("lastName").substring(0, 4) + "')]").count() == customDS.size());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}