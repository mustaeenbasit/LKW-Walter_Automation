package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_20072 extends SugarTest {
	DataSource caseData;

	public void setup() throws Exception {
		caseData = testData.get(testName);
		sugar.cases.api.create(caseData);
		sugar.login();
	}
	/**
	 * Search out open items in Cases module (using 'is not any of') filter for  Closed, Rejected, and Duplicate
	 */
	@Test
	public void ListView_20072_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Cases list view
		sugar.cases.navToListView();
		sugar.cases.listView.openFilterDropdown();
		sugar.cases.listView.selectFilterCreateNew();

		// Setting-Up filter in list view of Cases module
		// TODO: VOOD-1478
		FieldSet filterData = testData.get(testName+"_1").get(0);
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("filterField"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(filterData.get("filterOperator"));
		new VoodooSelect("li", "css", ".select2-search-field").set(filterData.get("filterValueClosed"));
		new VoodooSelect("li", "css", ".select2-search-field").set(filterData.get("filterValueRejected"));
		new VoodooSelect("li", "css", ".select2-search-field").set(filterData.get("filterValueDuplicate"));
		VoodooUtils.waitForReady();

		// Verifying Cases appearing in list view are coming as per above filter	
		int listCount = sugar.cases.listView.countRows();
		for (int i = 1; i <= listCount ; i++) {
			sugar.cases.listView.getDetailField(i, "status").assertEquals(caseData.get(2).get("status"), false);
			sugar.cases.listView.getDetailField(i, "status").assertEquals(caseData.get(3).get("status"), false);
			sugar.cases.listView.getDetailField(i, "status").assertEquals(caseData.get(4).get("status"), false);
		}

		// Close the filter in list view of Cases module 
		// TODO: VOOD-1478
		new VoodooControl("a", "css", ".filter-close").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}