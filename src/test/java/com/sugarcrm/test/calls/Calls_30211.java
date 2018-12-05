package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_30211 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that Contact / Lead name should appear in the Guest list of a Call/Meeting while creating from sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Calls_30211_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural).addRecord();

		// Verifying Lead record exist by default in call create drawer
		sugar().calls.createDrawer.getEditField("relatedToParentType").assertEquals(sugar().leads.moduleNameSingular, true);
		sugar().calls.createDrawer.getEditField("relatedToParentName").assertEquals(sugar().leads.getDefaultData().get("fullName"), true);

		// Verifying lead record is exist by default in guest list
		sugar().calls.createDrawer.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("fullName"), true);
		
		// Closing the create drawer
		sugar().calls.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}