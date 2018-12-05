package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22728 extends SugarTest {

	public void setup() throws Exception {
		// Create multiple Leads records 
		DataSource leadsData = testData.get(testName + "_leadsData");
		sugar().leads.api.create(leadsData);
		sugar().login();
	}

	/**
	 * Merge Duplicate_Verify that merging record can be removed when more than one leads are
	 *  selected as merging records.
	 * @throws Exception
	 */
	@Test
	public void Leads_22728_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to leads list view
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Click on Action - Edit button and select "Find Duplicates"
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-695, VOOD-738, VOOD-578, VOOD-568
		new VoodooControl("a", "css", ".fld_find_duplicates_button a").click();
		FieldSet customData = testData.get(testName).get(0);

		// Create a filter with available fields by clicking on "create"
		// TODO: VOOD-1899
		new VoodooControl("div", "css", ".choice-filter-clickable").click();
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(customData.get("filterKey"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(customData.get("filterOperator"));
		new VoodooControl("input", "css", ".detail.fld_last_name [name='last_name']").set(sugar().leads.getDefaultData().get("lastName"));

		// Select duplicate Record and click on "Merge Duplicates button"
		new VoodooControl("div", "css", ".btn.checkall").click();
		
		// TODO: VOOD-681
		new VoodooControl("input", "css", "[name='merge_duplicates_button']").click();

		// Click on first remove link
		new VoodooControl("input", "css", ".primary-edit-mode [data-action='delete']").click();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();

		// Verify the merging record disappears in the page and "remove" button of another merging record disappears.
		new VoodooControl("button", "css", "[data-action='delete'] button").assertVisible(false);
		new VoodooControl("span", "css", ".fld_first_name.edit").assertEquals("leadsData", false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
