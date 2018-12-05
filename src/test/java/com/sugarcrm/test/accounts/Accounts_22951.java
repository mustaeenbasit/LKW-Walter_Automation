package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22951 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
	}

	/**
	 * Account Detail - Bugs sub-panel - Create_Verify that creating new bug related to the account in full form is 
	 * canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22951_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to an Account Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click "Create" on "Bugs" sub-panel
		StandardSubpanel bugsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanel.addRecord();
		
		// Click "Show More" button and fill all fields
		FieldSet defaultBugData = sugar().bugs.getDefaultData();
		sugar().bugs.createDrawer.showMore();
		sugar().bugs.createDrawer.setFields(defaultBugData);
		sugar().bugs.createDrawer.showLess();
		
		// Click "Cancel" button
		sugar().bugs.createDrawer.cancel();
		
		// Verify no matching bug record is displayed on "BUGS" sub-panel
		bugsSubpanel.expandSubpanel();
		bugsSubpanel.assertContains(sugar().bugs.getDefaultData().get("name"), false);
		Assert.assertEquals("Row count is not equal to 0 in subpanel", 0, bugsSubpanel.countRows());
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}