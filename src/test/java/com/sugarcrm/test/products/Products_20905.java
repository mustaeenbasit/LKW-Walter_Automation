package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20905 extends SugarTest {
	DataSource ds;
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// create manufacturer
		// TODO VOOD-1034
		new VoodooControl("a", "id", "manufacturers").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "btn_create").click();
		new VoodooControl("input", "css", "input[name='name']").set(ds.get(0).get("name"));
		new VoodooControl("select", "css", "select[name='status']").set(ds.get(0).get("status"));
		new VoodooControl("input", "css", "input[name='list_order']").set(ds.get(0).get("order"));
		new VoodooControl("input", "id", "btn_save").click();
	}

	/**
	 * Verify that manufacture can be edited.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20905_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		for(int i=1;i<ds.size();i++) {
			new VoodooControl("a", "css", "table.list.view tr:nth-child(3) td a").click();
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("input", "css", "input[name='name']").set(ds.get(i).get("name"));
			new VoodooControl("select", "css", "select[name='status']").set(ds.get(i).get("status"));
			new VoodooControl("input", "css", "input[name='list_order']").set(ds.get(i).get("order"));
			new VoodooControl("input", "id", "btn_save").click();

			// check updated record in list
			new VoodooControl("td", "css", "table.list.view tr:nth-child(3) td:nth-of-type(1)").assertElementContains(ds.get(i).get("name"), true);
			new VoodooControl("td", "css", "table.list.view tr:nth-child(3) td:nth-of-type(2)").assertElementContains(ds.get(i).get("status"), true);
			new VoodooControl("td", "css", "table.list.view tr:nth-child(3) td:nth-of-type(3)").assertElementContains(ds.get(i).get("order"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}