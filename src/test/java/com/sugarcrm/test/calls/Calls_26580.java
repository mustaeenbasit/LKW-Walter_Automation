package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_26580 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.calls.api.create(ds);
		sugar.login();
	}

	/**
	 * Verify search by subject and my items return correct results
	 * @throws Exception
	 */
	@Test
	public void Calls_26580_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// search by subject
		sugar.calls.navToListView();
		sugar.calls.listView.setSearchString(ds.get(0).get("name").substring(0, 5));

		// Verify call records with 'subject' search
		sugar.calls.listView.verifyField(1, "name", ds.get(2).get("name"));
		sugar.calls.listView.verifyField(2, "name", ds.get(1).get("name"));
		sugar.calls.listView.verifyField(3, "name", ds.get(0).get("name"));

		// TODO: CB-230 Verify 4th call record is not in list
		sugar.calls.listView.getControl("checkbox04").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}