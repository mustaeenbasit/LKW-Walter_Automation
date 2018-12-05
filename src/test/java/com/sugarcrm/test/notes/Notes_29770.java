package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_29770 extends SugarTest {

	public void setup() throws Exception {
		// Creating Note Record
		sugar().notes.api.create();
		sugar().login();
	}

	/**
	 * Verify that 'Find Duplicates' & "View change Log" option is not available in 'edit' action drop-down of Notes module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_29770_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open detailed view of above created record
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);

		// Open "Primary Action Dropdown" 
		sugar().notes.recordView.openPrimaryButtonDropdown();
		FieldSet customData = testData.get(testName).get(0);
		// TODO: VOOD-738
		VoodooControl primaryActionDropdownCtrl = new VoodooControl("ul", "css", ".fld_main_dropdown .dropdown-menu");

		// Verify "Find Duplicates" option should not be available in 'edit' action drop-down of Notes module.
		primaryActionDropdownCtrl.assertContains(customData.get("option1"), false);

		// "View Change Log" option should not be available in 'edit' action drop-down of Notes module.
		primaryActionDropdownCtrl.assertContains(customData.get("option2"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}