package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20903 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that newly created manufacturer can be saved by clicking "Save & Create New" button.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20903_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ds = testData.get(testName);

		// create manufacturer
		sugar().manufacturers.navToListView();
		sugar().manufacturers.listView.clickCreate();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().manufacturers.editView.getEditField("name").set(ds.get(0).get("name"));
		sugar().manufacturers.editView.getEditField("status").set(ds.get(0).get("status"));
		sugar().manufacturers.editView.getEditField("order").set(ds.get(0).get("order"));
		VoodooUtils.focusDefault();
		sugar().manufacturers.editView.saveAndCreateNew();
		VoodooUtils.focusFrame("bwc-frame");

		// check record in list
		new VoodooControl("td", "css", "table.list.view tr:nth-child(3) td:nth-of-type(1)").assertElementContains(ds.get(0).get("name"), true);
		new VoodooControl("td", "css", "table.list.view tr:nth-child(3) td:nth-of-type(2)").assertElementContains(ds.get(0).get("status"), true);
		new VoodooControl("td", "css", "table.list.view tr:nth-child(3) td:nth-of-type(3)").assertElementContains(ds.get(0).get("order"), true);

		// check the new create view
		sugar().manufacturers.editView.getEditField("name").assertEquals("", true);
		sugar().manufacturers.editView.getEditField("name").set(ds.get(1).get("name"));
		sugar().manufacturers.editView.getEditField("status").set(ds.get(1).get("status"));
		sugar().manufacturers.editView.getEditField("order").assertEquals(ds.get(1).get("order"), true);
		VoodooUtils.focusDefault();
		sugar().manufacturers.editView.save();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}