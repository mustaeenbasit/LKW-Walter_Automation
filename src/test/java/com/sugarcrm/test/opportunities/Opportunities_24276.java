package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24276 extends SugarTest {
	public void setup() throws Exception {
		for (int i = 0; i < 3; i++) {
			FieldSet fs = new FieldSet();
			fs.put("name", testName+"_"+i);
			sugar().opportunities.api.create(fs);
		}
		sugar().login();
	}

	/**
	 * Search Opportunities_Verify that opportunity can be searched using advanced search function
	 *  
	 */
	@Test
	public void Opportunities_24276_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities listview
		sugar().opportunities.navToListView();
		
		// Set first record as favorite
		sugar().opportunities.listView.getControl("favoriteStar01").click();
		
		// Select "My Favorites" filter
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterMyFavorites();
		VoodooUtils.waitForReady();
		
		// Verify that 1 favorite record is shown
		Assert.assertTrue("Row count is not equal to 1 in listview", sugar().opportunities.listView.countRows() == 1);
		sugar().opportunities.listView.getControl("favoriteStar01").assertAttribute("class", "active");
		
		// Select "Recently Created" filter
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterRecentlyCreated();
		VoodooUtils.waitForReady();
		
		// Verify that 3 records are shown
		Assert.assertTrue("Row count is not equal to 3 in listview", sugar().opportunities.listView.countRows() == 3);
		for (int i = 0; i < 3; i++) {
			sugar().opportunities.listView.getDetailField((i+1), "name").assertContains(testName+"_"+(2-i), true);
		}
		
		// Select "Recently Viewed" filter
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterRecentlyViewed();
		VoodooUtils.waitForReady();
		
		// Verify that no records are shown in listview
		Assert.assertTrue("Row count is not equal to 0 in listview", sugar().opportunities.listView.countRows() == 0);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}