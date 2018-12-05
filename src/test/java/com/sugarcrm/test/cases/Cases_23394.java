package com.sugarcrm.test.cases;

import com.sugarcrm.sugar.records.BugRecord;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23394 extends SugarTest {
	CaseRecord myCase;
	BugRecord relBug;

	public void setup() throws Exception {
		myCase = (CaseRecord)sugar().cases.api.create();
		relBug = (BugRecord)sugar().bugs.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
	}

	/**
	 * Report Bug_Verify that bug for case is not reported when closing the pop up window.
	 * @throws Exception
	 */
	@Test
	public void Cases_23394_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myCase.navToRecord();

		// Select a bug to a case and click on Cancel button
		StandardSubpanel subBugs = sugar().cases.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		subBugs.scrollIntoViewIfNeeded(false);
		subBugs.clickLinkExisting();
		sugar().bugs.searchSelect.selectRecord(relBug);
		sugar().bugs.searchSelect.cancel();

		// Verify bug didn't appear in subpanel
		subBugs.scrollIntoViewIfNeeded(false);
		Assert.assertTrue("The subpanel is not empty", subBugs.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
