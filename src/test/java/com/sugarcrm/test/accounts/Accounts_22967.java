package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22967 extends SugarTest {
	StandardSubpanel bugsSubpanel;
	BugRecord myBug;

	public void setup() throws Exception {
		// Create two Account records with different name
		sugar().accounts.api.create();
		myBug = (BugRecord) sugar().bugs.api.create();
		sugar().login();

		// Enable bugs module for subpanel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Go to an account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Account(s) record(s) exist(s) with related Bugs records in subpanel
		bugsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanel.linkExistingRecord(myBug);
	}

	/**
	 * Verify that removing bug record related to this account is canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22967_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Already in Account > recordView
		// Click "Unlink" button on the right edge of a bug record on "BUGS" sub-panel. 
		bugsSubpanel.scrollIntoViewIfNeeded(false);
		bugsSubpanel.expandSubpanelRowActions(1);
		bugsSubpanel.getControl("unlinkActionRow01").click();

		// Click "Cancel" button on the pop-up dialog.
		sugar().alerts.getAlert().cancelAlert();

		// TODO: VOOD-1424
		// Verify that the bug record is still displayed on "BUGS" sub-panel.
		new VoodooControl("tr", "css", "[data-subpanel-link='bugs'] tbody tr:nth-of-type(1)").assertContains(myBug.getRecordIdentifier(), true);
		
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}