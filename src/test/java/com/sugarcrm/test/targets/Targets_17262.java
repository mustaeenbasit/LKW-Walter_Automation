package com.sugarcrm.test.targets;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Targets_17262 extends SugarTest {
	public void setup() throws Exception {
		sugar().targets.api.create();
		sugar().login();
	}

	/**
	 * Preview a Target record from the Targets module's List View
	 *
	 */
	@Test
	public void Targets_17262_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().targets.navToListView();
		sugar().targets.listView.previewRecord(1);

		// Verify that Target record's information displayed accurately in the preview panel
		sugar().previewPane.getPreviewPaneField("fullName").assertContains(sugar().targets.getDefaultData().get("firstName"), true);
		sugar().previewPane.getPreviewPaneField("account_name").assertContains(sugar().targets.getDefaultData().get("account_name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
