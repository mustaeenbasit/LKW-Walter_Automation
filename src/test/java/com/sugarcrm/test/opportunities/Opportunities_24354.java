package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24354 extends SugarTest {
	DataSource customDS = new DataSource();
	VoodooControl customFilterCtrl, selectedFilterCtrl;

	public void setup() throws Exception {
		customDS = testData.get(testName);
		sugar().opportunities.api.create(customDS);
		sugar().login();

		// A saved view for an advanced search is saved in Opportunities module.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterCreateNew();
		sugar().opportunities.listView.filterCreate.setFilterFields("name", "Name", "starts with", customDS.get(0).get("name").substring(0, 4), 1);
		sugar().opportunities.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().opportunities.listView.filterCreate.save();
	}

	/**
	 * Verify that the saved view's layout setting can be updated.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24354_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Results are shown as per the saved "Filter" in setup.
		sugar().opportunities.listView.verifyField(1, "name", customDS.get(1).get("name"));
		sugar().opportunities.listView.verifyField(2, "name", customDS.get(0).get("name"));

		// TODO: VOOD-1580
		// Click Filter option and select saved filter
		sugar().opportunities.listView.openFilterDropdown();
		customFilterCtrl = new VoodooControl("li", "css", ".search-filter-dropdown .select2-results li:nth-child(2)");
		customFilterCtrl.click();
		selectedFilterCtrl = new VoodooControl("span", "css", "[data-voodoo-name='Opportunities'] .choice-filter-label");
		selectedFilterCtrl.click();
		VoodooUtils.waitForReady();
		
		// Update the "Display Columns" setting and Ordering of Columns
		sugar().opportunities.listView.filterCreate.setFilterFields("name", "Name", "exactly matches", customDS.get(2).get("name"), 1);
		sugar().opportunities.listView.filterCreate.save();
		
		// Verify that the layout for the saved search is changed.
		sugar().opportunities.listView.verifyField(1, "name", customDS.get(2).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}