package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22968 extends SugarTest {
	StandardSubpanel bugsSubpanel;
	BugRecord myBug;

	public void setup() throws Exception {
		// Create two Account records with different name
		sugar().accounts.api.create();
		myBug = (BugRecord) sugar().bugs.api.create();
		sugar().login();

		// Enable bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Go to an account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Account(s) record(s) exist(s) with related Bugs records in subpanel
		bugsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanel.scrollIntoViewIfNeeded(false);
		bugsSubpanel.linkExistingRecord(myBug);
	}

	/**
	 * Verify that only the relationship between the bug and the account is removed by clicking "rem" icon.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22968_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Already in Account > recordView
		// Click "Unlink" button on the right edge of a bug record on "BUGS" sub-panel. 
		bugsSubpanel.unlinkRecord(1);

		// TODO: VOOD-1424
		// Verify that this related bug record is removed from "BUGS" sub-panel. 
		new VoodooControl("tr", "css", "[data-subpanel-link='bugs'] tbody tr:nth-of-type(1)").assertExists(false);
		Assert.assertTrue("The subpanel is empty", bugsSubpanel.isEmpty());
		
		// Go go Bugs listView
		sugar().bugs.navToListView();
		
		// Verify that the bug record is still displayed on the bugs list view under bug module.
		sugar().bugs.listView.verifyField(1, "name", myBug.getRecordIdentifier());
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}