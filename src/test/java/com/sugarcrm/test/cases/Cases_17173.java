package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_17173 extends SugarTest {
	CaseRecord myCase;

	public void setup() throws Exception {
	        sugar().accounts.api.create();
	        myCase = (CaseRecord)sugar().cases.api.create();
	        sugar().login();
	}

   /**
	* Verify the header section for cases module in the record view
	* @throws Exception
	*/
	@Test
	public void Cases_17173_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		//Verify the Cases label and Subject field exist
		sugar().cases.recordView.getControl("moduleIDLabel").hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(sugar().cases.moduleNameSingular, true);
		sugar().cases.recordView.getDetailField("name").assertEquals(myCase.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
