package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Bugs_18483 extends SugarTest {
	public void setup() throws Exception {
		sugar.bugs.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		sugar.alerts.waitForLoadingExpiration(); // Need more wait on Jenkins
	}

	/**
	 * Verify that the in line Delete works for Bugs List View 
	 * @throws Exception
	 */
	@Test
	public void Bugs_18483_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete record
		sugar.bugs.navToListView();
		sugar.bugs.listView.deleteRecord(1);
		FieldSet customData = new FieldSet();
		customData = testData.get(testName).get(0);
		String warningMsg = String.format("%s %s%s", customData.get("warning_msg"),sugar.bugs.getDefaultData().get("name"),customData.get("question_mark"));

		// Verify pop up message
		sugar.alerts.getWarning().assertExists(true);
		sugar.alerts.getWarning().assertContains(warningMsg, true);
		sugar.alerts.getWarning().confirmAlert();
		sugar.alerts.waitForLoadingExpiration();

		// Verify no record 
		sugar.bugs.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}