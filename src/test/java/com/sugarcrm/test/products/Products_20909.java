package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20909 extends SugarTest {
	DataSource manufacturerData;

	public void setup() throws Exception {
		manufacturerData = testData.get(testName);
		sugar().login();

		// Create Manufacturers
		sugar().manufacturers.navToListView();
		sugar().manufacturers.api.create(manufacturerData);
	}

	/**
	 * Manufacturer - Verify that manufactures can be sorted by column titles in list view.
	 * @throws Exception
	 */
	@Test
	public void Products_20909_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1534
		// Controls for first column i.e Name
		VoodooControl firstManufacturerName = new VoodooControl("a", "css", ".list tr:nth-of-type(3) td:nth-of-type(1) a");
		VoodooControl secondManufacturerName = new VoodooControl("a", "css", ".list tr:nth-of-type(4) td:nth-of-type(1) a");
		VoodooControl sortByName = new VoodooControl("a", "css", ".list tr:nth-of-type(2) td:nth-of-type(1) a");

		// Controls for second column i.e Status
		VoodooControl firstManufacturerStatus = new VoodooControl("td", "css", ".list tr:nth-of-type(3) td:nth-of-type(2)");
		VoodooControl secondManufacturerStatus = new VoodooControl("td", "css", ".list tr:nth-of-type(4) td:nth-of-type(2)");
		VoodooControl sortByStatus = new VoodooControl("a", "css", ".list tr:nth-of-type(2) td:nth-of-type(2) a");

		// Controls for third column i.e Order
		VoodooControl firstManufacturerOrder = new VoodooControl("td", "css", ".list tr:nth-of-type(3) td:nth-of-type(3)");
		VoodooControl secondManufacturerOrder = new VoodooControl("td", "css", ".list tr:nth-of-type(4) td:nth-of-type(3)");
		VoodooControl sortByOrder = new VoodooControl("a", "css", ".list tr:nth-of-type(2) td:nth-of-type(3) a");

		// Sort on Manufacturer name - ASC
		sortByName.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		firstManufacturerName.assertEquals(manufacturerData.get(0).get("name"), true);
		secondManufacturerName.assertEquals(manufacturerData.get(1).get("name"), true);

		// Sort on Manufacturer name - DESC
		sortByName.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		firstManufacturerName.assertEquals(manufacturerData.get(1).get("name"), true);
		secondManufacturerName.assertEquals(manufacturerData.get(0).get("name"), true);

		// Sort on Manufacturer Status - ASC
		sortByStatus.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		firstManufacturerStatus.assertEquals(manufacturerData.get(0).get("status"), true);
		secondManufacturerStatus.assertEquals(manufacturerData.get(1).get("status"), true);

		// Sort on Manufacturer Status - DESC
		sortByStatus.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		firstManufacturerStatus.assertEquals(manufacturerData.get(1).get("status"), true);
		secondManufacturerStatus.assertEquals(manufacturerData.get(0).get("status"), true);

		// Sort on Manufacturer Order - ASC
		sortByOrder.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		firstManufacturerOrder.assertEquals(manufacturerData.get(0).get("order"), true);
		secondManufacturerOrder.assertEquals(manufacturerData.get(1).get("order"), true);

		// Sort on Manufacturer Order - DESC
		sortByOrder.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		firstManufacturerOrder.assertEquals(manufacturerData.get(1).get("order"), true);
		secondManufacturerOrder.assertEquals(manufacturerData.get(0).get("order"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}