package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_30202 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that Contact's or Lead's record should appear in the Guest list of a Call/Meeting while creating from Quick Create
	 * @throws Exception
	 */
	@Test
	public void Meetings_30202_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().navbar.quickCreateAction(sugar().meetings.moduleNamePlural);

		// Verifying Lead record exist by default in meeting create drawer
		sugar().meetings.createDrawer.getEditField("relatedToParentType").assertEquals(sugar().leads.moduleNameSingular, true);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").assertEquals(sugar().leads.getDefaultData().get("fullName"), true);

		// Verifying lead record is exist by default in guest list
		sugar().meetings.createDrawer.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("fullName"), true);
		sugar().meetings.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}