package com.sugarcrm.test.cases;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Cases_23401 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName+"_case_data");
		sugar().cases.api.create(ds);
		sugar().login();
	}

	/**
	 * Search case_Verify that case can be searched with custom filter
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23401_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Go to Cases Record view
		sugar().cases.navToListView();
		sugar().cases.listView.openFilterDropdown();
		sugar().cases.listView.selectFilterCreateNew();

		// Create filter => field:status => operator:is any of => search input with given values are defined in CSV
		// TODO: VOOD-1460, VOOD-1478
		new VoodooSelect("div", "css", "div[data-filter='field']").set(customData.get("field"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(customData.get("operator"));
		VoodooSelect searchInput = new VoodooSelect("ul", "css", ".detail.fld_status ul");
		searchInput.set(ds.get(0).get("status"));
		VoodooUtils.waitForReady();

		// Verify only 1 record with status = Pending Input
		int rows = sugar().cases.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", rows == 1);
		sugar().cases.listView.verifyField(1, "name", ds.get(0).get("name"));

		// Verify 2 records with status = Pending Input, Closed
		searchInput.set(ds.get(1).get("status"));
		VoodooUtils.waitForReady();
		rows = sugar().cases.listView.countRows();
		Assert.assertTrue("Number of rows did not equal two.", rows == 2);
		sugar().cases.listView.verifyField(1, "name", ds.get(1).get("name"));
		sugar().cases.listView.verifyField(2, "name", ds.get(0).get("name"));

		// Close filter and verify 3 records back to on listview
		new VoodooControl("a", "css", ".filter-header .filter-close").click();
		VoodooUtils.waitForReady();
		rows = sugar().cases.listView.countRows();
		Assert.assertTrue("Number of rows did not equal three.", rows == 3);

		// TODO: VOOD-1477 - Below Voodoo control is replaced by "All" filter lib control by getting its child element
		// Verify default setting for filter dropdown ('fa-check' class ensures that "All" filter is selected)
		sugar().cases.listView.openFilterDropdown();
		new VoodooControl("i", "css", ".search-filter-dropdown [data-id='all_records'] .fa.fa-check").assertVisible(true);
		sugar().cases.listView.getControl("filterAll").click(); // to close filter dropdown

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
