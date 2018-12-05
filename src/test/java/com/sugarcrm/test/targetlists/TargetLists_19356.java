package com.sugarcrm.test.targetlists;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19356 extends SugarTest {
	DataSource recordsDS;
	
	public void setup() throws Exception {
		recordsDS = testData.get(testName+"_rec");
		sugar.targetlists.api.create(recordsDS);
		sugar.login();
	}

	/**
	 * Verify that search function in the "Target Lists Search" sub-panel works correctly.
	 *
	 *@throws Exception
	 */
	@Test
	public void TargetLists_19356_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Targets > List View > Enter 'b' in filter > Sort ascending
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.setSearchString(testData.get(testName).get(0).get("searchString"));
		sugar.targetlists.listView.sortBy("headerName", true);
		
		// Verify result and clear filter
		// VOOD-1397
		new VoodooControl("a", "css", ".main-content tr:nth-child(1) .fld_name.list div").assertEquals(recordsDS.get(1).get("targetlistName"), true);
		new VoodooControl("a", "css", ".main-content tr:nth-child(2) .fld_name.list div").assertEquals(recordsDS.get(2).get("targetlistName"), true);
		sugar.targetlists.listView.clearSearch();	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}