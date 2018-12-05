package com.sugarcrm.test.tags;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28575 extends SugarTest {	
	public void setup() throws Exception {
		sugar.productCategories.api.create();
		sugar.tags.api.create();
		sugar.login();
	}

	/**
	 * Verify Tags field has been removed from Product Categories module and 
	 * Product Categories subpanel is removed from Tags module  
	 * @throws Exception
	 */
	@Test
	public void Tags_28575_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Navigating to product categories record view
		sugar.productCategories.navToListView();
		sugar.productCategories.listView.clickRecord(1);

		// Asserting Tags is removed from Product Categories module
		// TODO:VOOD-1772
		new VoodooControl("div", "css", "[data-name='tag']").assertVisible(false);

		// Navigating to Tags record view 
		sugar.tags.navToListView();
		sugar.tags.listView.clickRecord(1);

		// Clicking Related Subpanel filter
		sugar.tags.recordView.getControl("relatedSubpanelFilter").click();

		// Verifying that Product Categories is not listed in Related Dropdown of Tags
		// TODO:VOOD-1463
		new VoodooControl("div", "id", "select2-drop").assertContains(fs.get("productCategories"), false);

		// Closing Related Subpanel filter
		// Need to select option from Related Subpanel dropdown because unable to click any element
		// outside until the dropdown is open
		// TODO:VOOD-629
		new VoodooControl("div", "css", ".select2-result-label").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}