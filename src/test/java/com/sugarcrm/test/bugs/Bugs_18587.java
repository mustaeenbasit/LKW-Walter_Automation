package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Bugs_18587 extends SugarTest {
	public void setup() throws Exception {
		// 2 duplicate bug record
		sugar().bugs.api.create();
		sugar().bugs.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
	} 

	/**
	 * Detail View_Verify that the page transfers to "MERGE RECORDS WITH" page after performing merge.
	 *
	 * @throws Exception
	 */
	@Test
	public void Bugs_18587_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Bugs list view
		sugar().bugs.navToListView();
		sugar().bugs.listView.clickRecord(1);
		sugar().bugs.recordView.openPrimaryButtonDropdown();

		// Click on Find Duplicate -> Check the check box in front of the found duplicate records. 
		// TODO: VOOD-568, VOOD-578, VOOD-691, VOOD-738
		new VoodooControl("a", "css", ".fld_find_duplicates_button a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-513, VOOD-1899
		new VoodooControl("input", "css", "tr.single td input[name=check]").click();
		new VoodooControl("a", "css", ".find-duplicates-headerpane.fld_merge_duplicates_button a").click();
		VoodooUtils.waitForReady();

		// Verify page transfers to "MERGE RECORDS WITH" "Merging 2 Records"
		new VoodooControl("div", "css", ".merge-duplicates-headerpane.fld_title div").assertEquals(testData.get(testName).get(0).get("merge_records_lbl"), true);
		sugar().alerts.getWarning().cancelAlert();
		new VoodooControl("a", "css", ".merge-duplicates-headerpane.fld_cancel_button a").click();
		new VoodooControl("a", "css", ".find-duplicates-headerpane.fld_cancel_button a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}