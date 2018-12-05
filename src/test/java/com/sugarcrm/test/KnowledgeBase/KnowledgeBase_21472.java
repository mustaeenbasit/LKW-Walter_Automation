package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21472 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Search_Article_with_invalid_value
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21472_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB list view
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.setSearchString(" ");

		// Verifying that no Error appears
		sugar().alerts.getError().assertVisible(false);

		// Verifying that no record is listed
		int rows = sugar().knowledgeBase.listView.countRows();
		Assert.assertTrue("No. of Rows is not equal to Zero", rows == 0);

		// Create a filter.
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();
		FieldSet fs = testData.get(testName).get(0);

		// Setting-Up filter in list view of Knowledge module
		// TODO: VOOD-629,VOOD-1463
		new VoodooSelect("div", "css", ".fld_filter_row_name .select2-container").set(fs.get("filterValue"));

		// Setting the condition in the filter
		// TODO: VOOD-629,VOOD-1463
		new VoodooSelect("div", "css", ".fld_filter_row_operator .select2-container").set(fs.get("condition"));
		new VoodooControl("input", "css", "[name='name']").set(" ");

		// Verifying that no Error appears
		sugar().alerts.getError().assertVisible(false);

		// Verifying that no record is listed
		Assert.assertTrue("No. of Rows is not equal to Zero", rows == 0);

		// Close the filter in list view of KnowledgeBase module 
		sugar().knowledgeBase.listView.filterCreate.getControl("cancelButton").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}