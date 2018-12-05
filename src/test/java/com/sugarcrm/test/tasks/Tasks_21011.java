package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Tasks_21011 extends SugarTest {
	public void setup() throws Exception {
		sugar().tasks.api.create();
		sugar().login();	
	}

	/**
	 * Search Task_Verify that search conditions can be cleared.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21011_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		String subject = sugar().tasks.getDefaultData().get("subject");

		// Tasks -> create filter -> remove row
		sugar().tasks.navToListView();
		sugar().tasks.listView.openFilterDropdown();

		try {
			sugar().tasks.listView.selectFilterCreateNew();

			// TODO: VOOD-1797 - Once resolved below commented line will work
			// sugar().tasks.listView.filterCreate.setFilterFields(customData.get("sugar_field"), customData.get("display_name"), customData.get("operator"), sugar().tasks.getDefaultData().get("subject"), 1);
			VoodooSelect displayName = new VoodooSelect("span", "css", "[data-filter='row'] .fld_filter_row_name.detail");
			VoodooSelect operator = new VoodooSelect("span", "css", "[data-filter='row'] .fld_filter_row_operator.detail");
			VoodooControl filterValue = new VoodooControl("input", "css", "[data-filter='row'] .fld_name.detail input");
			displayName.set(customData.get("display_name"));
			operator.set(customData.get("operator"));
			filterValue.set(subject);
			VoodooUtils.waitForReady();
			sugar().tasks.listView.filterCreate.clickRemoveRow(1);

			// TODO: VOOD-1477, VOOD-1879
			// Once VOOD-1477 resolved we need to update hook value should be access via getChildElement method
			// Once VOOD-1879 resolved we need to update with individual lib controls for displayName, operator and value
			// Verify search condition is cleared
			displayName.getChildElement("span", "css", displayName.getHookString()+" a span.select2-chosen").assertEquals(customData.get("reset_display_name"), true);
			operator.assertVisible(false);
			filterValue.assertVisible(false);

			// Verify record remains on listview
			sugar().tasks.listView.verifyField(1, "subject", subject);
		} finally {
			// To cancel/close filter view
			sugar().tasks.listView.filterCreate.cancel();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
