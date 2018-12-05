package com.sugarcrm.test.targetlists;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19470 extends SugarTest {
	StandardSubpanel targetsSubpanel;
	DataSource ds;

	public void setup() throws Exception {
		// Creating TargetList and 5 Targets records
		sugar.targetlists.api.create();
		ds = testData.get(testName);
		ArrayList<Record> tarRec = sugar.targets.api.create(ds);
		sugar.login();

		// Linking the Targets records into Target subpanel in TargetLists
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1); 
		targetsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural);
		targetsSubpanel.linkExistingRecords(tarRec);
	}

	/**
	 * TC 19470: Target List - Verify that target records can be sorted by column headers in "Targets" sub-panel.
	 *  @throws Exception
	 */
	@Test
	public void TargetLists_19470_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify sort Desc functionality for Name field in Target subpanel
		targetsSubpanel.sortBy("headerFullname", false);
		VoodooUtils.waitForReady();
		for (int i = ds.size() - 1 ; i >= 0; i--) {
			String name = ds.get(i).get("firstName")+" "+ds.get(i).get("lastName");
			targetsSubpanel.getDetailField(ds.size() - i, "fullName").assertContains(name, true);
		}

		// Verify sort Asc functionality for Name field in Target subpanel
		targetsSubpanel.sortBy("headerFullname", true);
		VoodooUtils.waitForReady();
		for (int i = 0; i <= ds.size() - 1; i++) {
			String name = ds.get(i).get("firstName")+" "+ds.get(i).get("lastName");
			targetsSubpanel.getDetailField(i+1, "fullName").assertContains(name, true);
		}

		// Verify sort Desc functionality for Title field in Target subpanel
		targetsSubpanel.sortBy("headerTitle", false);
		VoodooUtils.waitForReady();
		for (int i = ds.size() - 1; i >= 0; i--) {
			targetsSubpanel.getDetailField(ds.size() - i, "title").assertContains(ds.get(i).get("title"), true);
		}

		// Verify sort Asc functionality for Title field in Target subpanel
		targetsSubpanel.sortBy("headerTitle", true);
		VoodooUtils.waitForReady();
		for (int i = 0; i <= ds.size() - 1; i++) {
			targetsSubpanel.getDetailField(i+1, "title").assertContains(ds.get(i).get("title"), true);
		}

		// Verify sort Desc functionality for Work Phone field in Target subpanel
		targetsSubpanel.sortBy("headerPhonework", false);
		VoodooUtils.waitForReady();
		for (int i = ds.size() - 1; i >= 0; i--) {
			targetsSubpanel.getDetailField(ds.size() - i, "phoneWork").assertContains(ds.get(i).get("phoneWork"), true);
		}

		// Verify sort Asc functionality for Work Phone field in Target subpanel
		targetsSubpanel.sortBy("headerPhonework", true);
		VoodooUtils.waitForReady();
		for (int i = 0; i <= ds.size() - 1; i++) {
			targetsSubpanel.getDetailField(i+1, "phoneWork").assertContains(ds.get(i).get("phoneWork"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}