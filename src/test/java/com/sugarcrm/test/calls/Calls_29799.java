package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_29799 extends SugarTest {
	public void setup() throws Exception {
		// Creating two records of calls
		sugar().calls.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().calls.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify that 'Merge' option is not available in action drop-down for Calls module
	 * @throws Exception
	 */
	@Test
	public void Calls_29799_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Calls list view and OpenActionDropdown
		sugar().calls.navToListView();
		sugar().calls.listView.toggleSelectAll();
		sugar().calls.listView.openActionDropdown();

		// Asserting the existence of Merge option in Action dropdown on Calls list view 
		// TODO: VOOD-689
		new VoodooControl("a", "css", ".fld_merge_button.list").assertVisible(false);
		sugar().calls.listView.getControl("actionDropdown").click(); // to close dropdown

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}