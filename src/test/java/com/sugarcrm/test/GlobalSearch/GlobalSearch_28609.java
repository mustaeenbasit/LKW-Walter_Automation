package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28609 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		customDS = testData.get(testName);

		// Create Four contact records
		sugar().contacts.api.create(customDS);

		// Login as Admin
		sugar().login();
	}

	/**
	 * Verify that in search results record name of a searched record is displayed
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28609_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Search keyword only "black"
		sugar().navbar.setGlobalSearch(customDS.get(0).get("lastName").substring(0, 5)); 

		// Verify contact with black in lastname should display
		VoodooControl searchResultCtrl = sugar().navbar.search.getControl("searchResults");
		for(int i = 0; i < customDS.size()-1; i++) {
			searchResultCtrl.assertContains(customDS.get(i).get("lastName"), true);
		}

		// Verify that Make sure "unknown" is not displayed for contact name.
		searchResultCtrl.assertContains(customDS.get(3).get("lastName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}