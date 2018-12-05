package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22986 extends SugarTest {
	BugRecord myBug;
	AccountRecord myAccount;

	public void setup() throws Exception {
		myBug = (BugRecord) sugar().bugs.api.create();
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
		sugar().alerts.waitForLoadingExpiration(35000); // Required more wait to complete enableSubpanel action
	}

	/**
	 * Verify that corresponding bug records are displayed using search function on the "Search and Select Bugs" pop-up box.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22986_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel bugsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanel.clickLinkExisting();

		// Searching the Bug in the toggle drawer and asserting the bug by its subject.
		sugar().bugs.searchSelect.search(myBug.getRecordIdentifier());
		sugar().bugs.listView.assertContains(myBug.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}