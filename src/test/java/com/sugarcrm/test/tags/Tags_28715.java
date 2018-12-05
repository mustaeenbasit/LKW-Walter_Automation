package com.sugarcrm.test.tags;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28715 extends SugarTest {
	DataSource tagsDS = new DataSource();
	
	public void setup() throws Exception {
		tagsDS = testData.get(testName);
		
		// Create Tags records.
		sugar().tags.api.create(tagsDS);
		sugar().login();
		
		// Go to Tags Module and sort records.
		sugar().tags.navToListView();
		sugar().tags.listView.sortBy("headerName", true);		
		
		// In List View - click the star to favorite a Tag record
		sugar().tags.listView.getControl("favoriteStar01").click();
	}

	/**
	 * Verify ability to filter tags in the Tags module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28715_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Verify, Tag record marked as Favorite.
		sugar().tags.listView.openFilterDropdown();
		sugar().tags.listView.selectFilterMyFavorites();
		Assert.assertTrue("Assert Row Count = " + sugar().tags.listView.countRows() + " FAILED", sugar().tags.listView.countRows() == 1);
		sugar().tags.listView.verifyField(1, "name", tagsDS.get(0).get("name"));
		
		// Clear pre-selected filter and create new filter.
		DataSource filterDataDS = testData.get(testName+"_filterData");
		sugar().tags.listView.openFilterDropdown();
		sugar().tags.listView.selectFilterCreateNew();
		sugar().tags.listView.filterCreate.setFilterFields("name",filterDataDS.get(0).get("filterField"),filterDataDS.get(0).get("operator"),filterDataDS.get(0).get("value"),1);
		sugar().tags.listView.filterCreate.getControl("filterName").set(filterDataDS.get(0).get("filterName"));
		VoodooUtils.waitForReady();
		sugar().tags.listView.filterCreate.save();
		
		// Verify records are fetched based on the selected filter.
		sugar().tags.listView.openFilterDropdown();
		sugar().tags.listView.selectFilterAll();
		sugar().tags.listView.selectFilter(filterDataDS.get(0).get("filterName"));
		Assert.assertTrue("Assert Row Count = " + sugar().tags.listView.countRows() + " FAILED", sugar().tags.listView.countRows() == 1);
		sugar().tags.listView.verifyField(1, "name", tagsDS.get(1).get("name"));
				
		// Clear pre-selected filter and create new filter.
		sugar().tags.listView.openFilterDropdown();
		sugar().tags.listView.selectFilterCreateNew();
		sugar().tags.listView.filterCreate.setFilterFields("name",filterDataDS.get(1).get("filterField"),filterDataDS.get(1).get("operator"),filterDataDS.get(1).get("value"),1);
		sugar().tags.listView.filterCreate.getControl("filterName").set(filterDataDS.get(1).get("filterName"));
		VoodooUtils.waitForReady();
		sugar().tags.listView.filterCreate.save();
		
		// Verify records are fetched based on the selected filter.
		sugar().tags.listView.openFilterDropdown();
		sugar().tags.listView.selectFilterAll();
		sugar().tags.listView.selectFilter(filterDataDS.get(1).get("filterName"));
		Assert.assertTrue("Assert Row Count = " + sugar().tags.listView.countRows() + " FAILED", sugar().tags.listView.countRows() == 1);
		sugar().tags.listView.verifyField(1, "name", tagsDS.get(2).get("name"));

		// Edit existing filter and save.
		// TODO: VOOD-1580
		new VoodooControl("span", "css", ".choice-filter-label").click();
		sugar().tags.listView.filterCreate.setFilterFields("name",filterDataDS.get(2).get("filterField"),filterDataDS.get(2).get("operator"),filterDataDS.get(2).get("value"),1);
		sugar().tags.listView.filterCreate.save();
		
		// Verify records are fetched based on the selected filter which was updated above.
		sugar().tags.listView.openFilterDropdown();
		sugar().tags.listView.selectFilterAll();
		sugar().tags.listView.selectFilter(filterDataDS.get(2).get("filterName"));
		Assert.assertTrue("Assert Row Count = " + sugar().tags.listView.countRows() + " FAILED", sugar().tags.listView.countRows() == 1);
		sugar().tags.listView.verifyField(1, "name", tagsDS.get(3).get("name"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}