package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;

public class Bugs_17024 extends SugarTest {
	BugRecord testBug1, testBug2;
	DataSource bugDS = new DataSource();
	DataSource bugFilterData = new DataSource();

	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		bugDS = testData.get(testName);
		bugFilterData = testData.get(testName + "_1");
		testBug1 = (BugRecord) sugar.bugs.api.create(bugDS.get(0));
		testBug2 = (BugRecord) sugar.bugs.api.create(bugDS.get(1));
	}

	/**
	 * Verify new filter return correct result for bugs module
	 *
	 * @throws Exception
	 */
	@Test
	public void Bugs_17024_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to bugs list view and apply the filter by Subject and Status
		sugar.bugs.navToListView();
		sugar.bugs.listView.openFilterDropdown();
		sugar.bugs.listView.selectFilterCreateNew();

		// Set first condition: Subject - start with - test
		sugar().bugs.listView.filterCreate.setFilterFields("name", bugFilterData.get(0).get("field"), bugFilterData.get(0).get("operator"), bugFilterData.get(0).get("value"), 1);
		sugar().bugs.listView.filterCreate.clickAddRow(1);

		// Set second condition: Status - is not any of - Closed
		sugar().bugs.listView.filterCreate.setFilterFields("status", bugFilterData.get(1).get("field"), bugFilterData.get(1).get("operator"), bugFilterData.get(1).get("value"), 2);
		sugar().bugs.listView.filterCreate.getControl("filterName").set(bugFilterData.get(0).get("filter_name"));
		VoodooUtils.waitForReady();
		sugar().bugs.listView.filterCreate.save();

		//  Verify that the matched bug "testBug1" that match with the Subject matches using "Start With" Operator AND Status is not Closed" condition
		sugar.bugs.listView.verifyField(1, "name", bugDS.get(0).get("name"));

		// Remove custom filter
		// TODO: VOOD-998
		new VoodooControl("span", "css", ".choice-filter-label").click();
		sugar().bugs.listView.filterCreate.delete();
		sugar().alerts.getWarning().confirmAlert();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}