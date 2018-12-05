package com.sugarcrm.test.ListView;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_20066 extends SugarTest {

	public void setup() throws Exception {
		sugar.accounts.api.create();
		DataSource opportunityData= testData.get(testName);
		sugar.login();
		sugar.opportunities.create(opportunityData);
	}

	/**
	 * Search out open items in Opportunity Module
	 */
	@Test
	public void ListView_20066_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create filter 
		sugar.opportunities.listView.openFilterDropdown();
		sugar.opportunities.listView.selectFilterCreateNew();

		// Setting-Up filter in list view of Opportunities module
		// TODO: VOOD-1478
		FieldSet filterData = testData.get(testName+"_filterData").get(0);
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("filterField"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(filterData.get("filterOperator"));
		new VoodooSelect("li", "css", ".select2-search-field").set(filterData.get("filterValueClosedWon"));
		new VoodooSelect("li", "css", ".select2-search-field").set(filterData.get("filterValueClosedLost"));
		VoodooUtils.waitForReady();

		// Verifying Opportunities appearing in list view are neither having status "Closed Lost" nor "Closed Won"	
		int rows = sugar.opportunities.listView.countRows();
		Assert.assertTrue("Row is not equal to 1", rows == 1);
		sugar.opportunities.listView.getDetailField(1,"status").assertEquals(filterData.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}