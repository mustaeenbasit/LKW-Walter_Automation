package com.sugarcrm.test.targetlists;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19476 extends SugarTest {
	ArrayList<Record> targetRecords;

	public void setup() throws Exception {
		DataSource myTargets = testData.get(testName);
		sugar.targetlists.api.create();
		targetRecords = sugar.targets.api.create(myTargets);
		sugar.login();
	}
	
	/**
	 * Verify that the "More" functionality in the "Targets" sub-panel works correctly in TargetLIst record view
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19476_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);

		// Linking TargetRecords to TargetList
		StandardSubpanel targetsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural);
		targetsSubpanel.linkExistingRecords(targetRecords);

		// Verifying total No. of Rows in Targets Subpanel is equal to 5.
		int targetRows = targetsSubpanel.countRows();
		Assert.assertTrue("Total No. of Rows not Equal to 5", targetRows == 5);
		
		// Viewing more Target Records in the the Target Subpanel
		targetsSubpanel.showMore();
		VoodooUtils.waitForReady();
		targetRows = targetsSubpanel.countRows();
		Assert.assertTrue("Total No. of Rows not Equal to 7", targetRows == 7);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}