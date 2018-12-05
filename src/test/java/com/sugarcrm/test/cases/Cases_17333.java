package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_17333 extends SugarTest {
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myCase = (CaseRecord)sugar().cases.api.create();
		sugar().login();
	}

	@Test
	//verify the Cases Header row exists
	public void Cases_17333_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit the case using the UI.
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		//Verify the Cases label, Case name and Edit menu exist
		sugar().cases.recordView.getControl("moduleIDLabel").hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(sugar().cases.moduleNameSingular, true);		sugar().cases.recordView.getDetailField("name").assertEquals(myCase.get("name"), true);
		sugar().cases.recordView.getControl("editButton").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
