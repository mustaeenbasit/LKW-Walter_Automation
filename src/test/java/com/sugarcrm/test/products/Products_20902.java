package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20902 extends SugarTest {
	DataSource ds;
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that manufacturer can be created.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20902_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ds = testData.get(testName);
		// create manufacturer
		for(int i = 0; i < ds.size(); i++) {
			sugar().manufacturers.create(ds.get(i));
			int j = i + 3;
			// check record in list
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("td", "css", "table.list.view tr:nth-child(" + j + ") td:nth-of-type(1)").assertElementContains(ds.get(i).get("name"), true);
			new VoodooControl("td", "css", "table.list.view tr:nth-child(" + j + ") td:nth-of-type(2)").assertElementContains(ds.get(i).get("status"), true);
			new VoodooControl("td", "css", "table.list.view tr:nth-child(" + j + ") td:nth-of-type(3)").assertElementContains(ds.get(i).get("order"), true);
			VoodooUtils.focusDefault();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}