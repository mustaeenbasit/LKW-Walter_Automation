package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21917 extends SugarTest {
	DataSource myRecords = new DataSource();

	public void setup() throws Exception {
		// Create three lead record and Login as a valid user
		myRecords = testData.get(testName);
		sugar().leads.api.create(myRecords);
		sugar().login();
	}

	/**
	 * Delete saved Search filter_Verify that the saved search filter can be deleted.
	 * @throws Exception
	 */
	@Test
	public void Leads_21917_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Leads" module.
		sugar().leads.navToListView();
		
		// Click "Filter -> Create filter".
		sugar().leads.listView.openFilterDropdown();
		sugar().leads.listView.selectFilterCreateNew();

		// Enter custom filter name and filter data.
		FieldSet filterData = testData.get(testName + "_customData").get(0);
		sugar().leads.listView.filterCreate.setFilterFields(filterData.get("fieldName"), filterData.get("displayName"), filterData.get("matchType"), myRecords.get(0).get("lastName"), 1);
		sugar().leads.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().leads.listView.filterCreate.save();

		// Reload Leads list view page.
		sugar().leads.navToListView();

		// Click custom search name link next to "Filter" dropdown, Click Delete link in opened panel.
		// TODO: VOOD-1752 Need lib support for verifying the filter label text  in List View
		new VoodooControl("span", "css", ".choice-filter-label").click();
		sugar().leads.listView.filterCreate.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Verify Filter is deleted.
		sugar().leads.listView.openFilterDropdown();
		sugar().leads.listView.getControl("filterAll").assertVisible(true);
		// TODO: VOOD-1843 Improve ChildElement to detect the parent strategy
		new VoodooControl("i", "css", sugar().leads.listView.getControl("filterAll").getHookString() + " .fa.fa-check").assertVisible(true);

		// Verify default All Leads filter is applied.
		sugar().leads.listView.getControl("searchFilterCurrent").assertEquals(filterData.get("currentFilter"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}