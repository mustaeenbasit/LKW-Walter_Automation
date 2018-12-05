package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Leads_22213 extends SugarTest {

	public void setup() throws Exception {
		// Create two lead records
		FieldSet fs = new FieldSet();
		fs.put("lastName", testName);
		sugar().leads.api.create(fs);
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Merge Duplicate_Verify that the merging record can be set as primary record.
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_22213_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		// Sorting in order to track the merging record.
		sugar().leads.listView.sortBy("headerFullname", false);
		sugar().leads.listView.clickRecord(1);

		// Click "Find Duplicate" button in the lead detail view.
		sugar().leads.recordView.openPrimaryButtonDropdown();
		// TODO: VOOD-695
		new VoodooControl("a", "css", "[data-voodoo-name='find_duplicates_button']").click();
		VoodooUtils.waitForReady();

		// Create new filter
		// For filter
		// TODO: VOOD-1899
		VoodooControl filterCtrl = new VoodooControl("i", "css", "[data-voodoo-name='dupecheck-filter-dropdown'] .select2-choice-type .fa.fa-caret-down");
		filterCtrl.click();
		FieldSet customData = testData.get(testName).get(0);
		new VoodooControl("li", "css", ".search-filter-dropdown .select2-results li:nth-child(1)").click(); // click create
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(customData.get("field"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(customData.get("operator"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='last_name']").set(customData.get("value"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".main-pane div .layout_Leads .layout_Leads .span6 input").set(testName);

		// Save filter
		new VoodooControl("a", "css", ".main-pane div .layout_Leads .layout_Leads .save_button").click();

		// Select duplicate Record
		new VoodooControl("input", "css", "[data-check='one']").click();

		// Click Merge Duplicates
		// TODO: VOOD-681
		new VoodooControl("a", "css", "[name='merge_duplicates_button']").click();

		new VoodooControl("span", "css", "[data-container='primary-label-span']").dragNDrop(new VoodooControl("div", "css", ".num-cols-2 div div:nth-child(2) .primary-lbl" ));
		sugar().alerts.getWarning().confirmAlert();

		// Save
		new VoodooControl("a", "css", ".merge-duplicates-headerpane.fld_save_button [name='save_button']").click();
		VoodooUtils.waitForReady();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();

		// Verify user is navigated to merged record, Merged record is having data same as merging record(set as primary).
		FieldSet leadData = sugar().leads.getDefaultData();
		sugar().leads.recordView.assertVisible(true);
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.getDetailField("fullName").assertEquals(leadData.get("fullName"), true);
		sugar().leads.recordView.getDetailField("phoneWork").assertEquals(leadData.get("phoneWork"), true);
		sugar().leads.recordView.getDetailField("title").assertEquals(leadData.get("title"), true);
		sugar().leads.recordView.getDetailField("phoneMobile").assertEquals(leadData.get("phoneMobile"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}