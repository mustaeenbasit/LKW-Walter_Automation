package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Bugs_18580 extends SugarTest {
	DataSource bugsDS;	

	public void setup() throws Exception {
		bugsDS = testData.get(testName);
		sugar.bugs.api.create(bugsDS);
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * Sort Bug_Verify that bugs can be sorted
	 * 
	 * @throws Exception
	 */
	@Test
	public void Bugs_18580_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.bugs.navToListView();

		// Verify sorting of records in ascending/descinding order
		// name
		sugar.bugs.listView.sortBy("headerName", false);
		sugar.bugs.listView.verifyField(1, "name", bugsDS.get(1).get("name"));
		sugar.bugs.listView.verifyField(2, "name", bugsDS.get(0).get("name"));

		sugar.bugs.listView.sortBy("headerName", true);
		sugar.bugs.listView.verifyField(1, "name", bugsDS.get(0).get("name"));
		sugar.bugs.listView.verifyField(2, "name", bugsDS.get(1).get("name"));

		// status
		sugar.bugs.listView.sortBy("headerStatus", false);
		sugar.bugs.listView.verifyField(1, "status", bugsDS.get(0).get("status"));
		sugar.bugs.listView.verifyField(2, "status", bugsDS.get(1).get("status"));

		sugar.bugs.listView.sortBy("headerStatus", true);
		sugar.bugs.listView.verifyField(1, "status", bugsDS.get(1).get("status"));
		sugar.bugs.listView.verifyField(2, "status", bugsDS.get(0).get("status"));

		// type
		sugar.bugs.listView.sortBy("headerType", false);
		sugar.bugs.listView.verifyField(1, "type", bugsDS.get(1).get("type"));
		sugar.bugs.listView.verifyField(2, "type", bugsDS.get(0).get("type"));

		sugar.bugs.listView.sortBy("headerType", true);
		sugar.bugs.listView.verifyField(1, "type", bugsDS.get(0).get("type"));
		sugar.bugs.listView.verifyField(2, "type", bugsDS.get(1).get("type"));

		// priority
		sugar.bugs.listView.sortBy("headerPriority", false);
		sugar.bugs.listView.verifyField(1, "priority", bugsDS.get(0).get("priority"));
		sugar.bugs.listView.verifyField(2, "priority", bugsDS.get(1).get("priority"));

		sugar.bugs.listView.sortBy("headerPriority", true);
		sugar.bugs.listView.verifyField(1, "priority", bugsDS.get(1).get("priority"));
		sugar.bugs.listView.verifyField(2, "priority", bugsDS.get(0).get("priority"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}