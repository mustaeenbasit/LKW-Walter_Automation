package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22963 extends SugarTest {
	StandardSubpanel bugsSubpanelCtrl;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		BugRecord bugRecord = (BugRecord) sugar().bugs.api.create(); 
		sugar().login();
		
		// Enable bugs module for subPanel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Link Existing bug record related to an account record needed
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		bugsSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		bugsSubpanelCtrl.linkExistingRecord(bugRecord);
	}

	/**
	 * Verify that bug related to the account can be edited on "BUGS" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22963_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Already in Account > recordView
		bugsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		// Click "Edit" in-line button and update value on "BUGS" sub-panel
		bugsSubpanelCtrl.editRecord(1, fs);
		
		// TODO: VOOD-1424
		// Verify that the modified bug information is displayed on "BUGS" sub-panel.
		new VoodooControl("tr", "css", "[data-subpanel-link='bugs'] tbody tr:nth-of-type(1)").assertContains(testName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}