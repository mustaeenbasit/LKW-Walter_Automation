package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class ListView_26532 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
		sugar.accounts.api.create();
	}

	/**
	 * Verify "Date Created" data is shown in Account list view
	 */
	@Test
	public void ListView_26532_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts list view
		sugar.accounts.navToListView();

		// Verify "No Data" is not shown under Date created column
		// TODO VOOD-868: Lib support to verify "Date Created" column in Account list view
		new VoodooControl("div", "css", ".fld_date_entered.list div")
				.assertContains("No Data", false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
