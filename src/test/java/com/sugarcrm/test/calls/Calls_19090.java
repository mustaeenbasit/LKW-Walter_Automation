package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_19090 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.calls.api.create(ds);
		sugar.login();
	}

	/**
	 * Verify that selected calls are deleted by Mass delete.
	 * @throws Exception
	 */
	@Test
	public void Calls_19090_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();

		// Verify call records, initially
		sugar.calls.listView.verifyField(1, "name", ds.get(2).get("name"));
		sugar.calls.listView.verifyField(2, "name", ds.get(1).get("name"));
		sugar.calls.listView.verifyField(3, "name", ds.get(0).get("name"));

		// Delete very first and second call record
		sugar.calls.listView.toggleRecordCheckbox(1);
		sugar.calls.listView.toggleRecordCheckbox(2);
		sugar.calls.listView.openActionDropdown();
		sugar.calls.listView.delete();
		sugar.alerts.getWarning().confirmAlert();

		// Verifying only delete records is deleted from listview
		sugar.calls.listView.verifyField(1, "name", ds.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}