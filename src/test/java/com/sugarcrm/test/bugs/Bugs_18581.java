package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Bugs_18581 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.login();

		// Enable bugs module
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		sugar.bugs.api.create(ds);
	}

	/**
	 * Search Bug - Basic Search_Verify that bugs matching the search conditions can be displayed in bug list.
	 * @throws Exception
	 */
	@Test
	public void Bugs_18581_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.bugs.navToListView();
		sugar.bugs.listView.setSearchString(ds.get(0).get("name"));
		sugar.alerts.waitForLoadingExpiration(); // wait needed for to search complete

		// TODO: VOOD-959
		//sugar.bugs.listView.verifyField(1, "name", ds.get(0).get("name"));
		new VoodooControl("a", "css", ".fld_name.list div a").assertEquals(ds.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}