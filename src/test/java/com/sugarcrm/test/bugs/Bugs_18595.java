package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Bugs_18595 extends SugarTest {
	DataSource bugData = new DataSource();
	DataSource updateDataRecord = new DataSource();

	public void setup() throws Exception {
		bugData = testData.get(testName);
		updateDataRecord = testData.get(testName + "_1");
		sugar.bugs.api.create(bugData);

		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * Test Case 18595: Mass Update_Verify that records can be mass updated in the bugs list view after applying a filter
	 *
	 * @throws Exception
	 */
	@Test
	public void Bugs_18595_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to bugs list view and apply the filter by Status
		sugar.bugs.navToListView();
		sugar.accounts.listView.openFilterDropdown();
		sugar.bugs.listView.selectFilterCreateNew();
		sugar().bugs.listView.filterCreate.setFilterFields("status", updateDataRecord.get(0).get("field"), updateDataRecord.get(0).get("operator"), updateDataRecord.get(0).get("value"), 1);
		sugar.alerts.waitForLoadingExpiration();

		// Execute Mass Update and change status of bugs to Closed
		sugar.bugs.listView.toggleSelectAll();
		FieldSet massUpdFS = new FieldSet();
		massUpdFS.put(updateDataRecord.get(1).get("field"), updateDataRecord.get(1).get("value"));
		sugar.bugs.massUpdate.performMassUpdate(massUpdFS);
		sugar.alerts.getSuccess().closeAlert();

		// Verify that the bugs were correctly updated
		sugar.bugs.navToListView();
		sugar.bugs.listView.openFilterDropdown();
		sugar.bugs.listView.selectFilterAll(); // set to default filter
		sugar.bugs.listView.setSearchString(bugData.get(0).get("name"));
		sugar.bugs.listView.verifyField(1, "status", updateDataRecord.get(1).get("value"));
		sugar.bugs.listView.setSearchString(bugData.get(1).get("name"));
		sugar.bugs.listView.verifyField(1, "status", updateDataRecord.get(1).get("value"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}