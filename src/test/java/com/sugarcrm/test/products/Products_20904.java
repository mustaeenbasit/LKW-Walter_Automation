package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20904 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that manufacturer cannot be created when leaving required fields empty.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20904_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		// create manufacturer
		sugar().manufacturers.navToListView();
		sugar().manufacturers.listView.clickCreate();
		sugar().manufacturers.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", "table.edit.view div.required.validation-message").assertEquals(ds.get(0).get("assert"), true);
		// check no records in list
		new VoodooControl("td", "css", "table.list.view tr.oddListRowS1").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}