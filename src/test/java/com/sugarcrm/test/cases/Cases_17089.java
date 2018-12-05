package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_17089 extends SugarTest {
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
		// Create a Case via the GUI so that an Account is related to the Case
		// TODO VOOD-444 Support creating relationships via API
		myCase = (CaseRecord) sugar().cases.create();
	}

	@Test
	// verify the user can preview a record on list view
	public void Cases_17089_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		sugar().cases.listView.previewRecord(1);
		myCase.verifyPreview();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
