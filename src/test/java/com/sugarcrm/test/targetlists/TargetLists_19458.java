package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;

public class TargetLists_19458 extends SugarTest{

	public void setup() throws Exception {
		sugar.targetlists.api.create();
		sugar.login();
	}

	/**
	 * Target List - Delete Target List_Verify that "Cancel" function in the confirm delete target list dialog box works correc
	 * 
	 */
	@Test
	public void TargetLists_19458_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Target Lists" link in navigation shortcuts.
		sugar.navbar.navToModule(sugar.targetlists.moduleNamePlural);
		sugar.targetlists.listView.clickRecord(1);
		// Click on "Delete" button from drop-down
		sugar.targetlists.recordView.openPrimaryButtonDropdown();
		sugar.targetlists.recordView.getControl("deleteButton").click();
		// Click "Cancel" button
		sugar.alerts.getWarning().cancelAlert();

		// Verify "Target Lists" does not delete.
		sugar.targetlists.recordView.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}