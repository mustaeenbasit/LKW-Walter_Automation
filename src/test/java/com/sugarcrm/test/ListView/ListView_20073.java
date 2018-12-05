package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_20073 extends SugarTest {
	DataSource bugsData = new DataSource();

	public void setup() throws Exception {
		bugsData = testData.get(testName);
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().bugs.api.create(bugsData);
	}
	/**
	 * Search out open items in Bugs module (using 'is not any of') filter for  Closed and Rejected
	 */
	@Test
	public void ListView_20073_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Bugs list view
		sugar().bugs.navToListView();
		sugar().bugs.listView.openFilterDropdown();
		sugar().bugs.listView.selectFilterCreateNew();

		// Setting-Up filter in list view of Bugs module
		// TODO: VOOD-1478
		FieldSet filterData = testData.get(testName+"_1").get(0);
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("filterField"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(filterData.get("filterOperator"));
		new VoodooSelect("li", "css", ".select2-search-field").set(filterData.get("filterValueClosed"));
		new VoodooSelect("li", "css", ".select2-search-field").set(filterData.get("filterValueRejected"));
		VoodooUtils.waitForReady();

		// Verifying Bugs appearing in list view are coming as per above filter	
		int listCount = sugar().bugs.listView.countRows();
		for (int i = 1; i <= listCount ; i++) {
			sugar().bugs.listView.getDetailField(i, "status").assertEquals(bugsData.get(2).get("status"), false);
			sugar().bugs.listView.getDetailField(i, "status").assertEquals(bugsData.get(3).get("status"), false);
		}

		// Close the filter in list view of Bugs module
		sugar().bugs.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}