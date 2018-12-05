package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_30859 extends SugarTest {
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
	public void Calls_30859_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to calls list view and create a new call record
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.createDrawer.getEditField("name").set(testName);
		
		// Clicking Add Invitee and navigating on Contact search and select drawer
		sugar().calls.createDrawer.clickAddInvitee();
		sugar().calls.createDrawer.selectInvitee(sugar().contacts.getDefaultData().get("firstName"));
		sugar().calls.createDrawer.save();

		// Navigate to contacts list view and click on first record.
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		
		// Expand calls subpanel, edit "Assigned User" field and save.
		StandardSubpanel callsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.expandSubpanel();
		callsSubpanel.editRecord(1);
		String qaUserName = testData.get("env_role_setup").get(0).get("userName");
		callsSubpanel.getEditField(1, "assignedTo").set(qaUserName);
		callsSubpanel.saveAction(1);
		
		// Verify application should change "Assigned User" without refreshing the page.
		callsSubpanel.getDetailField(1, "assignedTo").assertEquals(qaUserName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}