package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Leads_22215 extends SugarTest {
	
	public void setup() throws Exception {
		// Create three Leads records with different name
		FieldSet fs = new FieldSet();
		fs.put("lastName", testName);
		sugar().leads.api.create(fs);
		sugar().leads.api.create(fs);
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that lead can be merged to one of merging records which are selected in leads list searched by "Filter Condition" from the detail view of the lead.
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_22215_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to leads recordView
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		
		// Click "Find Duplicate" button in the leads detail view.
		sugar().leads.recordView.gotoPreviousRecord();
		sugar().leads.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-695
		new VoodooControl("a", "css", "[data-voodoo-name='find_duplicates_button']").click();
		VoodooUtils.waitForReady();
		FieldSet customData = testData.get(testName).get(0);
		
		// TODO: VOOD-568
		// For filter
		new VoodooControl("i", "css", "[data-voodoo-name='dupecheck-filter-dropdown'] .select2-choice-type .fa.fa-caret-down").click();
		new VoodooControl("li", "css", ".search-filter-dropdown .select2-results li:nth-child(1)").click(); // click create
		VoodooUtils.waitForReady();
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(customData.get("filterKey"));
		VoodooUtils.waitForReady();
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(customData.get("filterWith"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".detail.fld_last_name [name='last_name']").set(testName);
		VoodooUtils.waitForReady();
		
		// Verify that more than one lead records are displayed in the result list view.
		for(int i = 1; i < 3; i++) {
			new VoodooControl("div", "css", "[data-voodoo-name='dupecheck-list-multiselect'] table tr:nth-child("+i+") td:nth-of-type(2) span div").assertContains(testName, true);
		}
		
		// Select duplicate Record
		new VoodooControl("input", "css", "[data-voodoo-name='dupecheck-list-multiselect'] table tr:nth-child(1) td:nth-of-type(1) input").click();
		new VoodooControl("input", "css", "[data-voodoo-name='dupecheck-list-multiselect'] table tr:nth-child(2) td:nth-of-type(1) input").click();
		new VoodooControl("input", "css", "[name='merge_duplicates_button']").click();
		VoodooUtils.waitForReady();
		
		// Verify that the merge lead detail view is displayed
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(1) [name='last_name']").assertContains(sugar().leads.getDefaultData().get("lastName"), true);
		new VoodooControl("div", "css", ".row-div-cnt > div:nth-child(2) .list.fld_last_name div").assertContains(testName, true);
		new VoodooControl("div", "css", ".row-div-cnt > div:nth-child(3) .list.fld_last_name div").assertContains(testName, true);
		
		// Click on save button for merge two records
		new VoodooControl("a", "css", "span.merge-duplicates-headerpane.fld_save_button a").click();
		sugar().alerts.confirmAllWarning();
		VoodooUtils.waitForReady();

		sugar().leads.navToListView();
		
		// Verify that the original records are not displayed.
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("fullName"));
		sugar().leads.listView.assertContains(testName, false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}