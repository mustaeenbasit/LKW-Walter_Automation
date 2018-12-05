package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_30032 extends SugarTest {
	DataSource contactData = new DataSource();

	public void setup() throws Exception {
		contactData = testData.get(testName);
		sugar().contacts.api.create(contactData);
		sugar().login();

		// Enabling module QLI
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify that Searching is working for Contact Name field in Quoted Line Item Module.
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_30032_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Quoted Line Items" Module
		sugar().quotedLineItems.navToListView();

		// Click on Create
		for (int i = 0; i < 2; i++) {
			sugar().quotedLineItems.listView.create();

			// Link Contact Name with it
			sugar().quotedLineItems.createDrawer.getEditField("name").set(contactData.get(i).get("firstName"));
			sugar().quotedLineItems.createDrawer.getEditField("relContactName").set(contactData.get(i).get("lastName"));
			sugar().quotedLineItems.createDrawer.save();
		}

		// Create a filter.
		sugar().quotedLineItems.listView.openFilterDropdown();
		sugar().quotedLineItems.listView.selectFilterCreateNew();
		FieldSet filterData = testData.get(testName+ "_filterData").get(0);

		// Setting-Up filter in list view of QuotedLineItems module
		// TODO: VOOD-1879
		VoodooControl filterValue = new VoodooControl("input", "css", ".detail.fld_contact_name .select2-input");
		VoodooControl filterMatch = new VoodooControl("span", "css", ".select2-match");
		new VoodooSelect("span", "css",".fld_filter_row_name").set(filterData.get("filterValue"));
		new VoodooSelect("div", "css",".fld_filter_row_operator div").set(filterData.get("condition"));
		for (int i = 0; i < 2 ; i++) {
			if (i == 1) 
				// Close the first pill in value selected
				new VoodooControl("a", "css", ".select2-search-choice .select2-search-choice-close").click();

			filterValue.set(contactData.get(i).get("lastName"));
			filterMatch.click();
			VoodooUtils.waitForReady();

			// Assert QLI Associated with respective Contact Name is only displayed
			sugar().quotedLineItems.listView.clickRecord(1);
			sugar().quotedLineItems.recordView.getDetailField("relContactName").assertContains(contactData.get(i).get("lastName"), true);
			sugar().quotedLineItems.navToListView();
		}

		sugar().quotedLineItems.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}