package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21059 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.calls.api.create(ds);			
		sugar.login();		
	}
	/**
	 * Verify that corresponding record is displayed in call detail view when clicking the pagination control link in call detail view.
	 * @throws Exception
	 */
	@Test
	public void Calls_21059_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);		
		sugar.calls.recordView.getDetailField("name").assertEquals(ds.get(1).get("name"), true);

		// Verify next record
		sugar.calls.recordView.gotoNextRecord();
		sugar.calls.recordView.getDetailField("name").assertEquals(ds.get(0).get("name"), true);

		// Verify prev record
		sugar.calls.recordView.gotoPreviousRecord();
		sugar.calls.recordView.getDetailField("name").assertEquals(ds.get(1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}