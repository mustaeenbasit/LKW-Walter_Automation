package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20907 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().manufacturers.api.create(ds);
		sugar().login();
		sugar().manufacturers.navToListView();
	}

	/**
	 * Verify that deleting manufacture can be canceled.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20907_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO VOOD-1034
		sugar().manufacturers.listView.deleteRecord(1);
		VoodooUtils.dismissDialog();
		VoodooUtils.focusDefault();
		sugar().manufacturers.listView.verifyField(1, "name", ds.get(0).get("name"));
		sugar().manufacturers.listView.verifyField(1, "status", ds.get(0).get("status"));
		sugar().manufacturers.listView.verifyField(1, "order", ds.get(0).get("order"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}