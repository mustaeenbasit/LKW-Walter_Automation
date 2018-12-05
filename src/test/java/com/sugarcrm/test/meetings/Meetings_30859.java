package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Meetings_30859 extends SugarTest {
	public void setup() throws Exception {
		// Initialize Test Data
		sugar().contacts.api.create();
		
		// Login using admin user
		sugar().login();
	}

	/**
	 * Verify that application is changing "Assigned User" in Calls/Meetings subpanel without refreshing the page
	 * @throws Exception
	 */
	@Test
	public void Meetings_30859_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to meetings list view and create a new meeting record
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		
		// Clicking Add Invitee and navigating on Contact search and select drawer
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(sugar().contacts.getDefaultData().get("firstName"));
		sugar().meetings.createDrawer.save();
		
		// Navigate to contacts list view and click on first record.
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		
		// Expand meetings subpanel, edit "Assigned User" field and save.
		StandardSubpanel meetingsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.expandSubpanel();
		meetingsSubpanel.editRecord(1);
		String qaUserName = testData.get("env_role_setup").get(0).get("userName");
		meetingsSubpanel.getEditField(1, "assignedTo").set(qaUserName);
		meetingsSubpanel.saveAction(1);
		
		// Verify application should change "Assigned User" without refreshing the page.
		meetingsSubpanel.getDetailField(1, "assignedTo").assertEquals(qaUserName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}