package com.sugarcrm.test.products;

import com.sugarcrm.candybean.datasource.FieldSet;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20923 extends SugarTest {
	DataSource ds1 = new DataSource();
	DataSource ds2 = new DataSource();

	public void setup() throws Exception {
		sugar.login();
		ds1 = testData.get(testName);
		ds2 = testData.get(testName + "_1");

		FieldSet systemSettingsData = new FieldSet();
		// change display number on list view
		systemSettingsData.put("maxEntriesPerPage", ds2.get(0).get("num1"));
		// change system settings
		sugar().admin.setSystemSettings(systemSettingsData);

		// create product category
		sugar.productCategories.api.create(ds1);
	}

	/**
	 * Verify clicking more will display more product category.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20923_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCategories.navToListView();

		// click More product categories link and verify it only load the number of records that set in system setting
		while (sugar.productCategories.listView.getControl("showMore").queryExists()) {
			for (int i = 3; i <= ds1.size(); i += 3) {
				sugar.productCategories.listView.getControl("checkbox0" + i).assertVisible(true);
				sugar.productCategories.listView.getControl("checkbox0" + (i + 1)).assertVisible(false);
				sugar.productCategories.listView.showMore();
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {
	}
}