package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23037 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that creating new meeting is canceled under Meetings sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23037_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel meetingsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.addRecord();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.cancel();
		
		// Verify that meeting sub-panel is empty
		Assert.assertTrue("The subpanel is not empty", meetingsSubpanel.isEmpty());
			
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}