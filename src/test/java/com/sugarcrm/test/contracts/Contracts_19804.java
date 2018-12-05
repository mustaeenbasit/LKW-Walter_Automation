package com.sugarcrm.test.contracts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19804 extends SugarTest {
	DataSource contractsData = new DataSource();

	public void setup() throws Exception {
		contractsData = testData.get(testName+"_contractsData");
		// Create 24 Contracts record
		sugar().contracts.api.create(contractsData);

		sugar().login();
		// Enable Contracts module
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);

		// Set number of records to be displayed on list view to 10
		FieldSet customData = testData.get(testName).get(0);
		FieldSet fs = new FieldSet();
		fs.put("maxEntriesPerPage", customData.get("setMaxEntries"));
		sugar().admin.setSystemSettings(fs);
	}

	/**
	 * Verify that pagination in Contract list view works correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19804_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contracts module
		sugar().contracts.navToListView();

		VoodooUtils.focusFrame("bwc-frame");
		// Sorting Contract records to verify records displayed on clicking
		// "Next", "Previous", "Start", "End"
		// TODO: VOOD-1534
		new VoodooControl("a", "css", "#MassUpdate table tbody tr:nth-child(2) th:nth-child(4) div a").click();

		VoodooUtils.focusDefault();
		// Verify that by default there is 10 records in listview
		Assert.assertTrue("Row count is not equal to 10", sugar().contracts.listView.countRows() == 10);

		// Click on 'Next' button and verify corresponding page for Contract list is displayed correctly
		// TODO: VOOD-766
		new VoodooControl("img", "css", "#listViewNextButton_top img").click();
		VoodooUtils.focusDefault();
		Assert.assertTrue("Row count is not equal to 10", sugar().contracts.listView.countRows() == 10);
		VoodooUtils.focusDefault();
		sugar().contracts.listView.verifyField(1, "name", contractsData.get(18).get("name"));
		VoodooUtils.focusFrame("bwc-frame");

		// Click on 'Previous' button and verify corresponding page for Contract list is displayed correctly
		// TODO: VOOD-766
		new VoodooControl("img", "css", "#listViewPrevButton_top img").click();
		VoodooUtils.focusDefault();
		Assert.assertTrue("Row count is not equal to 10", sugar().contracts.listView.countRows() == 10);
		VoodooUtils.focusDefault();
		sugar().contracts.listView.verifyField(1, "name", contractsData.get(0).get("name"));
		VoodooUtils.focusFrame("bwc-frame");

		// Click on 'End' button and verify corresponding page for Contract list is displayed correctly
		// TODO: VOOD-766
		new VoodooControl("img", "css", "#listViewEndButton_top img").click();
		VoodooUtils.focusDefault();
		Assert.assertTrue("Row count is not equal to 4", sugar().contracts.listView.countRows() == 4);
		VoodooUtils.focusDefault();
		sugar().contracts.listView.verifyField(1, "name", contractsData.get(5).get("name"));
		VoodooUtils.focusFrame("bwc-frame");

		// Click on 'Start' button and verify corresponding page for Contract list is displayed correctly
		// TODO: VOOD-766
		new VoodooControl("img", "css", "#listViewStartButton_top img").click();
		VoodooUtils.focusDefault();
		Assert.assertTrue("Row count is not equal to 10", sugar().contracts.listView.countRows() == 10);
		VoodooUtils.focusDefault();
		sugar().contracts.listView.verifyField(1, "name", contractsData.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}