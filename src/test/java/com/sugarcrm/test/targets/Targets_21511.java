package com.sugarcrm.test.targets;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.test.SugarTest;

public class Targets_21511  extends SugarTest {
	TargetRecord myTargetRecord;

	public void setup() throws Exception {
		sugar().login();
		myTargetRecord = (TargetRecord) sugar().targets.api.create();
	}

	/**
	 *  Target - Delete Target_Verify that the "Cancel" function confirm dialog box works correctly.
	 *
	 * @throws Exception
	 */
	@Test
	public void Targets_21511_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(sugar().targets.moduleNamePlural);
		sugar().navbar.selectMenuItem(sugar().targets, "viewtargets");
		sugar().targets.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().targets.recordView.delete();
		sugar().alerts.getAlert().cancelAlert();
		sugar().targets.recordView.showMore();
		sugar().targets.recordView.getDetailField("title").assertEquals(myTargetRecord.get("title"), true);
		sugar().targets.recordView.getDetailField("phoneWork").assertEquals(myTargetRecord.get("phoneWork"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
