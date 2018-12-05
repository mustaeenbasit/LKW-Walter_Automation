package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26085 extends SugarTest {

	public void setup() throws Exception {
		sugar().login(sugar().users.getQAUser());
	}
	/**
	 * [Pro/Ent Edition]-[Dynamic team]-Create record with only one team-auto filled search
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26085_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Navigate to Documents > Create Document
		sugar().navbar.selectMenuItem(sugar().documents, "createDocument");
		VoodooUtils.focusFrame("bwc-frame");
		FieldSet customData = testData.get(testName).get(0);
		// TODO: VOOD-1980, VOOD-518
		VoodooControl teamEditCtrl = new VoodooControl("input", "css", "input[title='Team Selected ']");
		// Enter partial data "E" in team field
		teamEditCtrl.set(customData.get("team").substring(0, 1));
		VoodooUtils.pause(1000); // TODO: Investigate why waitForReady() does not work here at CI.

		// Verify that existed team (eg.Chris East) is auto filled in "team" field.
		teamEditCtrl.assertEquals(customData.get("team"), true);

		VoodooUtils.focusDefault();
		// Cancel
		sugar().documents.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}