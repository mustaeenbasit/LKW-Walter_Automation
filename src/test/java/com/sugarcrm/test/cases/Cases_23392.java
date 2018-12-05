package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.test.SugarTest;

public class Cases_23392 extends SugarTest {
	CaseRecord myCase;
	FieldSet defaultBugData, defaultCaseData;

	public void setup() throws Exception {
		sugar().login();
		defaultBugData = sugar().bugs.getDefaultData();
		defaultCaseData = sugar().cases.getDefaultData();
		sugar().accounts.api.create();
		myCase = (CaseRecord)sugar().cases.api.create(defaultCaseData);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
	}

	/**
	 * Test Case 23392: Verify that bug for case is not created in "Bugs" sub-panel when using "Cancel" function .
	 */
	@Test
	public void Cases_23392_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myCase.navToRecord();

		// Click "Create new record" in Bugs subpanel
		StandardSubpanel subBugs = sugar().cases.recordView.subpanels.get("Bugs");
		subBugs.expandSubpanel();
		subBugs.addRecord();

		// Put default data and cancel
		sugar().bugs.createDrawer.showMore();
		sugar().bugs.createDrawer.setFields(defaultBugData);
		sugar().bugs.createDrawer.showLess();
		sugar().bugs.createDrawer.cancel();

		// Verify the bug related to the case hasn't been created
		subBugs.assertContains(defaultBugData.get("name"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
