package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20890 extends SugarTest {
	DataSource ds;
	FieldSet fs;
	public void setup() throws Exception {
		ds = testData.get(testName);
		fs = testData.get(testName+"_filter").get(0);
		
		// Ensure correct order of creation
		// TODO: VOOD-2139 - Need to ensure all moduleCSV files (both Sidecar and BWC) have date and time fields for both dateCreation and dateModified values
		for (int i = 0; i < ds.size(); i++) {
			sugar().productCatalog.api.create(ds.get(i));
			VoodooUtils.pause(1000); // Insert deliberate delay
		}
		
		sugar().login();	
	}

	/**
	 * Verify you can create filter for product catalog search.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20890_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().productCatalog.navToListView();		
		// click create filter link
		sugar().productCatalog.listView.openFilterDropdown();
		sugar().productCatalog.listView.selectFilterCreateNew();
		// set filters
		// TODO: VOOD-1478
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name .select2-chosen").set(fs.get("field1"));
		new VoodooSelect("div", "css", "div.filter-body div[data-filter='operator'] .select2-chosen").set(fs.get("operator"));
		new VoodooControl("span", "css", "div.filter-definition-container > div:nth-child(1)  input[name='name']").set(fs.get("filter1"));
		sugar().alerts.waitForLoadingExpiration();
		sugar().productCatalog.listView.getDetailField(1, "name").assertElementContains(ds.get(1).get("name"), true);
		sugar().productCatalog.listView.getDetailField(2, "name").assertElementContains(ds.get(0).get("name"), true);
		sugar().productCatalog.listView.getDetailField(3, "name").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 