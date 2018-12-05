package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20906 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().manufacturers.api.create(ds);
		sugar().login();
	}

	/**
	 * Verify that manufacture can be deleted from list view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20906_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().manufacturers.navToListView();
		sugar().manufacturers.listView.deleteRecord(1);
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();
		sugar().manufacturers.listView.getField(1, "name").assertContains(ds.get(0).get("name"), false);
		sugar().manufacturers.listView.getField(1, "status").assertContains(ds.get(0).get("status"), false);
		sugar().manufacturers.listView.getField(1, "order").assertContains(ds.get(0).get("order"), false);
		for(int i=1;i<ds.size();i++) {
			new VoodooControl("table", "css", ".list.view").assertElementContains(ds.get(i).get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}