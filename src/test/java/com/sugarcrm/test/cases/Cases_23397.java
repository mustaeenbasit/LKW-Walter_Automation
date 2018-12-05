package com.sugarcrm.test.cases;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23397 extends SugarTest {
	BugRecord relBug;
	StandardSubpanel subBugs;

	public void setup() throws Exception {
		CaseRecord myCase = (CaseRecord)sugar().cases.api.create();
		relBug = (BugRecord)sugar().bugs.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
		sugar().alerts.waitForLoadingExpiration(20000);

		// Link a bug to a case
		myCase.navToRecord();
		subBugs = sugar().cases.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		subBugs.scrollIntoView();
		subBugs.linkExistingRecord(relBug);
	}

	/**
	 * Edit Bug Report_Verify that detail view of a case related bug is displayed
	 * after clicking the subject of it in Bugs sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Cases_23397_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		subBugs.scrollIntoView();
		// Click the subject of an existing bug in "Bugs" sub-panel.
		subBugs.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();

		// Verify that RecordView of this related bug is displayed.
		sugar().bugs.recordView.assertExists(true);
		sugar().bugs.recordView.getDetailField("name").assertEquals(sugar().bugs.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
