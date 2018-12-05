package com.sugarcrm.test.bugs;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Bugs_18598 extends SugarTest {
	ArrayList<Record> myCalls;
	DataSource callsRecords;
	StandardSubpanel callsSubpanel;

	public void setup() throws Exception {
		callsRecords = testData.get(testName);
		sugar.bugs.api.create();
		myCalls = sugar.calls.api.create(callsRecords);
		
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		
		// Go to a bug tracker record's detail view and link 8 call's record 
		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);
		callsSubpanel = sugar.bugs.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		callsSubpanel.linkExistingRecords(myCalls);
	}

	/**
	 * Verify that records in sub-panel can be paginated
	 * 
	 * @throws Exception
	 */
	@Test
	public void Bugs_18598_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that Records in sub-panel can be paginated
		int rowsInFirstView = callsSubpanel.countRows();
		Assert.assertTrue("Row count in subpanel is not equal to 5", rowsInFirstView == 5);
		
		// Click on show more link
		callsSubpanel.showMore();
		VoodooUtils.waitForReady();
		
		// Verify that rest 3 records display after clicking "show more" link
		int rowsInSecondView = callsSubpanel.countRows();
		Assert.assertTrue("Row count in subpanel is not equal to 8", rowsInSecondView == 8);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}