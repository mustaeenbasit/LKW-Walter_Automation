package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24280 extends SugarTest {
	public void setup() throws Exception {
		// Create 20 Opportunities record 
		for (int i = 0; i < 20; i++) {
			sugar().opportunities.api.create();
		}
		sugar().login();
		
		// Set max listview items per page to 10
		FieldSet fs = new FieldSet();
		fs.put("maxEntriesPerPage", "10");
		sugar().admin.setSystemSettings(fs);
	}

	/**
	 * Pagination_Verify that the "Opportunities" list view can be paginated
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24280_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities listview and verify that 10 records displays in listview
		sugar().opportunities.navToListView();
		Assert.assertTrue("Records in listview are not equal to 10", sugar().opportunities.listView.countRows() == 10);
		
		// Click on "More Opportunities.." link
		sugar().opportunities.listView.showMore();
		VoodooUtils.waitForReady();
		
		// Verify that after clicking "More Opportunities..", next 10 records displays in listview
		Assert.assertTrue("Records in listview are not equal to 20", sugar().opportunities.listView.countRows() == 20);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}