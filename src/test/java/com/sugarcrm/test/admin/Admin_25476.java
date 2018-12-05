package com.sugarcrm.test.admin;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_25476 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
	}

	/** Verify that 2 buttons in quick create UI for sidecar modules.
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore("SC-3506 - Unsaved Changes Warning appears in Quick Create upon Navigating Away" + 
	        "SC-4802 - Quick Create pops up unsaved alert even when the previous view has no data entered or the record has been created successfully")
	public void Admin_25476_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on quick create to select sidecar module, Tasks.
		sugar().navbar.quickCreateAction(sugar().tasks.moduleNamePlural);
		
		// Verify, in Create Task UI, Cancel and Save buttons are visible.
		sugar().tasks.createDrawer.getControl("saveButton").assertVisible(true);
		sugar().tasks.createDrawer.getControl("cancelButton").assertVisible(true);

		// Fill in all required field and save.
		sugar().tasks.createDrawer.getEditField("subject").set(sugar().tasks.getDefaultData().get("subject"));
		sugar().tasks.createDrawer.save();
		
		sugar().alerts.getAlert().closeAlert();

		// Click on quick create to select sidecar module, Opportunity.
		sugar().navbar.quickCreateAction(sugar().opportunities.moduleNamePlural);
		
		// Verify, in Create Opportunity UI, Cancel and Save buttons are visible.
		sugar().opportunities.createDrawer.getControl("saveButton").assertVisible(true);
		sugar().opportunities.createDrawer.getControl("cancelButton").assertVisible(true);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String d = sdf.format(date);

		// Fill in all required field and save.
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().opportunities.getDefaultData().get("relAccountName"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set("rli1");
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(d);
		sugar().opportunities.createDrawer.getEditField("rli_likely").set("2500");
		sugar().opportunities.createDrawer.save();

		sugar().alerts.getAlert().closeAlert();

		// Click on quick create to select "Create Call".
		sugar().navbar.quickCreateAction(sugar().calls.moduleNamePlural);
		
		// Verify visibility of Cancel, Save, Save and Send Invites and Close and create new buttons in Create Call UI.
		sugar().calls.createDrawer.getControl("saveButton").assertVisible(true);
		sugar().calls.createDrawer.getControl("cancelButton").assertVisible(true);

		// Fill in all required field and save.
		sugar().calls.createDrawer.getEditField("name").set(sugar().calls.getDefaultData().get("name"));
		sugar().calls.createDrawer.save();

		sugar().alerts.getAlert().closeAlert();

		// Click on quick create to select "Schedule Meeting".
		sugar().navbar.quickCreateAction(sugar().meetings.moduleNamePlural);
		
		// Verify visibility of Cancel, Save, Save and Send Invites and Close and create new buttons in Create Meetings UI.
		sugar().meetings.createDrawer.getControl("saveButton").assertVisible(true);
		sugar().meetings.createDrawer.getControl("cancelButton").assertVisible(true);

		// Fill in all required field and save.
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.save();

		sugar().alerts.getAlert().closeAlert();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}