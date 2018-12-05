package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_20069 extends SugarTest {
	DataSource callsData;

	public void setup() throws Exception {
		callsData = testData.get(testName);
		sugar.calls.api.create(callsData);
		sugar.login();
	}
	/**
	 * Search out open (Scheduled) items in Calls module (using 'not any of') filter for 'Held' and 'Canceled' 
	 */
	@Test
	public void ListView_20069_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to calls list view
		sugar.calls.navToListView();
		sugar.calls.listView.openFilterDropdown();
		sugar.calls.listView.selectFilterCreateNew();

		// Setting-Up filter in list view of calls module
		// TODO: VOOD-1478
		FieldSet filterData = testData.get(testName+"_1").get(0);
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("filterField"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(filterData.get("operator"));
		new VoodooSelect("ul", "css", ".select2-choices").set(filterData.get("filter_value_Held"));
		new VoodooSelect("ul", "css", ".select2-choices").set(filterData.get("filter_value_Canceled"));
		VoodooUtils.waitForReady();

		// Verifying calls appearing in list view are coming as per above filter	
		int listCount = sugar.calls.listView.countRows(); // 
		for (int i = 1; i <= listCount ; i++) 
			sugar.calls.listView.verifyField(i, "status", callsData.get(1).get("status"));

		// Close the filter in list view of calls module 
		// TODO: VOOD-1478
		new VoodooSelect("a", "css", ".filter-close").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}