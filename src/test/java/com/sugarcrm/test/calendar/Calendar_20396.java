package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calendar_20396 extends SugarTest {

	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Relationship with other module_Verify that meeting record is displayed in "Activities" sub-panel of "Account" detail view page when scheduling a meeting.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20396_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab & Schedule Meeting
		sugar.navbar.selectMenuItem(sugar.calendar, "scheduleMeeting");
		// Enter all the Mandatory fields
		sugar.meetings.createDrawer.getEditField("name").set(testName);
		// Select "Account" from drop down list,
		sugar.meetings.createDrawer.getEditField("relatedToParentName").set(sugar.accounts.getDefaultData().get("name"));
		sugar.meetings.createDrawer.save();

		// Go to "Account" detail view page.
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// Verify the scheduled meeting record is displayed in "Meetings" sub-panel.
		StandardSubpanel meetingsSubpanel = sugar.accounts.recordView.subpanels.get(sugar.meetings.moduleNamePlural);
		meetingsSubpanel.expandSubpanel();
		meetingsSubpanel.getDetailField(1, "name").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}