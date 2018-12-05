package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_23422 extends SugarTest {
	BugRecord myBug;

	public void setup() throws Exception {
		myBug = (BugRecord) sugar().bugs.api.create();
		sugar().cases.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
	}

	/**
	 * Quick Search in pop up window_Verify that bugs can be searched in pop up window when selecting an existing bug in case detail view.
	 * @throws Exception
	 */
	@Test
	public void Cases_23422_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Cases" tab in navigation bar and go to a case record view.
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Click "Link Existing Record" button in "Bugs" sub-panel.
		sugar().cases.recordView.subpanels.get(sugar().bugs.moduleNamePlural).clickLinkExisting();

		// Enter information for search conditions in the pop up window. For example, enter "1" or first characters of any bug's name.
		sugar().bugs.searchSelect.search(myBug.getRecordIdentifier().substring(0, 1));
		VoodooUtils.waitForReady();

		// Verify that the Bugs record that match the search conditions are displayed in the pop up window.
		sugar().bugs.listView.assertContains(myBug.getRecordIdentifier(), true);
		sugar().bugs.searchSelect.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
