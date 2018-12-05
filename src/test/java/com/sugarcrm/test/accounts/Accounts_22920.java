package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22920 extends SugarTest {
	StandardSubpanel bugsSubpanel;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		BugRecord myBugRecord = (BugRecord) sugar().bugs.api.create();
		sugar().login();

		// Go to Admin -> Display Modules and SubPanels and drag Bugs from Hidden SubPanel to Displayed SubPanel to show Bugs RLI subpanel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Existing bug record related to an account record needed
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		bugsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanel.scrollIntoView();
		bugsSubpanel.linkExistingRecord(myBugRecord);
	}

	/**
	 * Bugs sub-panel_Verify that bug record related to this account can be viewed by clicking subject link on "BUGS" sub-panel
	 *
	 * @throws Exception
	 */
	@Test
	public void Accounts_22920_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Bugs Default data
		String name = sugar().bugs.getDefaultData().get("name");
		String status = sugar().bugs.getDefaultData().get("status");
		String description = sugar().bugs.getDefaultData().get("description");

		// Click on bug's subject link on "BUGS" sub-panel.
		bugsSubpanel.clickLink(name, 1);

		// Verify that the bug's detail view is displayed.
		sugar().bugs.recordView.assertContains(name, true);
		sugar().bugs.recordView.assertContains(status, true);
		sugar().bugs.recordView.assertContains(description, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}