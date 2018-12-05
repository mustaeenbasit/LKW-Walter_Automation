package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class ListView_17026 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();		
	}

	/**
	 *  Verify close action on preview panel.
	 */
	@Test
	public void ListView_17026_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.previewRecord(1);
		VoodooUtils.waitForReady();

		// Close the preview pane by click the "X" button on upper right corner of preview panel.
		sugar().previewPane.closePreview();

		// Verify the Account name does not exist in RHS panel.
		sugar().previewPane.getPreviewPaneField("name").assertVisible(false);

		// Verify the Previous state exist in RHS panel.
		if(sugar().accounts.dashboard.getControl("dashboardTitle").queryContains("My Dashboard", true)) {
			sugar().accounts.dashboard.getControl("dashboardTitle").assertEquals("My Dashboard", true);
		} else {
			sugar().accounts.dashboard.getControl("dashboardTitle").assertEquals("Help Dashboard", true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}