package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24355 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		customDS = testData.get(testName+"_record");
		sugar().opportunities.api.create(customDS);
		sugar().login();

		// A saved view for filter search is saved.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterCreateNew();
		sugar().opportunities.listView.filterCreate.setFilterFields("name", "Name", "starts with", customDS.get(0).get("name").substring(0, 4), 1);
		sugar().opportunities.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().opportunities.listView.filterCreate.save();
	}

	/**
	 * Filter_Verify that the saved filter can be deleted.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24355_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// VOOD-1828 Records created via api call are displayed in random order.
		sugar().opportunities.listView.sortBy("headerName", false);
				
		// Results are shown as per the saved "Filter" in setup.
		sugar().opportunities.listView.verifyField(1, "name", customDS.get(0).get("name"));
		sugar().opportunities.listView.verifyField(2, "name", customDS.get(1).get("name"));

		// TODO: VOOD-1580
		// Select the saved search "Filter" from the drop down list and Click on Filter and its edit view will be open
		sugar().opportunities.listView.openFilterDropdown();
		VoodooControl customFilterCtrl = new VoodooControl("li", "css", ".search-filter-dropdown .select2-results li:nth-child(2)");
		customFilterCtrl.click();
		new VoodooControl("span", "css", "[data-voodoo-name='Opportunities'] .choice-filter-label").click();
		VoodooUtils.waitForReady();
		
		// Click the delete button.
		sugar().opportunities.listView.filterCreate.delete();
		sugar().opportunities.listView.confirmDelete();

		// Verify that the saved search disappears from the  "Filter" drop down list
		sugar().opportunities.listView.openFilterDropdown();
		customFilterCtrl.assertEquals(testName, false);
		// Close the "Filter" drop down as it overlaps the element in next step  - by selecting "All Opportunities"
		customFilterCtrl.click();
		
		// Verify all the opportunities are displayed in list view
		for(int i = 0; i < customDS.size(); i++)
			sugar().opportunities.listView.verifyField(i+1, "name", customDS.get(i).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}