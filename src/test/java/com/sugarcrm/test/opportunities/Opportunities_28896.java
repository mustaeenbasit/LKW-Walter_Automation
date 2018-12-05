package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_28896 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}
	/**
	 * Verify that Status field is NOT displaying in the drop down list of "Create filter" section
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28896_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to opportunities list view
		sugar().opportunities.navToListView();

		// Create a filter.
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterCreateNew();
		FieldSet fs = testData.get(testName).get(0);

		// Setting-Up filter in list view of Opportunities module
		// TODO: VOOD-1463
		VoodooSelect searchBox = new VoodooSelect("span", "css", ".detail.fld_filter_row_name");
		searchBox.click();
		VoodooUtils.waitForReady();
		searchBox.selectWidget.getControl("searchBox").set(fs.get("filterValue"));
		VoodooControl dropdownList = new VoodooControl("div", "css", "#select2-drop");
		dropdownList.assertContains(fs.get("salesStage"), true);
		dropdownList.assertContains(fs.get("status"), false);

		// TODO: VOOD-806
		// selecting the sales stage in filter
		searchBox.selectWidget.getControl("searchBox").set(fs.get("salesStage"));
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-629
		// Workaround to click on Voodooselect fields in filter
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + fs.get("salesStage") + "')]]").click();

		// Close the filter in list view of Opportunities module 
		// TODO: VOOD-1478
		new VoodooControl("a", "css", ".filter-close").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}