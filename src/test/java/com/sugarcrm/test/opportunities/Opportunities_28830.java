package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28830 extends SugarTest {
	public void setup() throws Exception {

		// Login as Regular user qaUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that Probability field should not be displayed in the Create
	 * filter section of Opportunities, when OPP + RLI selected
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28830_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to opportunities list view
		sugar().opportunities.navToListView();

		// Create a filter.
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterCreateNew();
		FieldSet oppFilterData = testData.get(testName).get(0);

		// Setting-Up filter in list view of Opportunities module
		// TODO: VOOD-629,VOOD-1463
		VoodooSelect searchBox = new VoodooSelect("span", "css",
				".detail.fld_filter_row_name");
		searchBox.click();
		searchBox.selectWidget.getControl("searchBox").set(
				oppFilterData.get("filterValue"));
		VoodooControl dropdownList = new VoodooControl("li", "css",
				".select2-results li");
		dropdownList.assertEquals(oppFilterData.get("result"), true);

		// Selecting the Status in filter
		// TODO: VOOD-806
		searchBox.selectWidget.getControl("searchBox").set(
				oppFilterData.get("status"));
		VoodooUtils.waitForReady();

		// TODO: VOOD-629
		// Workaround to click on Voodooselect fields in filter
		new VoodooControl("span", "XPATH",
				"/html//div[@id='select2-drop']//*[text()[contains(.,'"
						+ oppFilterData.get("status") + "')]]").click();

		// Close the filter in list view of Opportunities module
		// TODO: VOOD-1478
		new VoodooControl("a", "css", ".filter-close").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}