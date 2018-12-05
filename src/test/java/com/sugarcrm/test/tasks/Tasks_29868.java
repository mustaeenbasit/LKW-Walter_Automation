package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_29868 extends SugarTest {

	public void setup() throws Exception {
		sugar().tasks.api.create();
		sugar().login();
	}

	/**
	 * Verify that "View Change Log" option is not available in 'edit action dropdown' for Tasks module
	 * @throws Exception
	 */
	@Test
	public void Tasks_29868_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Navigate to Tasks Record View
		sugar().tasks.navToListView();
		sugar().tasks.listView.clickRecord(1);

		// Click the Action Dropdown available alongside Edit button
		sugar().tasks.recordView.openPrimaryButtonDropdown();

		// Verify that View Change Log option is not available in the Action DropDown near Edit button.
		// TODO: VOOD-738
		new VoodooControl("ul", "css", ".fld_main_dropdown .dropdown-menu").assertContains(fs.get("viewChangeLog"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}