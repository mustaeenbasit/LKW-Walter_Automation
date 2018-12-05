package com.sugarcrm.test.calls;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

public class Calls_29959 extends SugarTest {
	DataSource customData = new DataSource();
	ArrayList<VoodooControl> studioModuleCtrls;
	VoodooControl fieldsCtrl, requiredCheckboxCtrl, saveBtnCtrl, relatedToErrorFieldCtrl;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		customData = testData.get(testName);
		// Login
		sugar().login();

		// Array List of modules
		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().calls);
		modules.add(sugar().meetings);
		modules.add(sugar().tasks);
		modules.add(sugar().notes);

		// Array List of Studio module controls
		// TODO: VOOD-542 and VOOD-1504
		studioModuleCtrls = new ArrayList<VoodooControl>();
		studioModuleCtrls.add(new VoodooControl("a", "id", "studiolink_Calls"));
		studioModuleCtrls.add(new VoodooControl("a", "id", "studiolink_Meetings"));
		studioModuleCtrls.add(new VoodooControl("a", "id", "studiolink_Tasks"));
		studioModuleCtrls.add(new VoodooControl("a", "id", "studiolink_Notes"));

		// Define Controls
		// TODO: VOOD-542 and VOOD-1504
		fieldsCtrl = new VoodooControl("td", "id", "fieldsBtn");
		requiredCheckboxCtrl = new VoodooControl("input", "css", "input[name='required']");
		saveBtnCtrl = new VoodooControl("input", "css", "input[name='fsavebtn']");

		// Go to Admin -> Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		for(int i = 0; i < customData.size(); i++) {
			// Calls/Meetings/Tasks/Notes -> Fields -> Click 'Related to' field to open its properties
			studioModuleCtrls.get(i).click();
			VoodooUtils.waitForReady();
			fieldsCtrl.click();
			VoodooUtils.waitForReady();
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(.,'" + customData.get(i).get("relatedToField") + "')]/td").click();
			VoodooUtils.waitForReady();

			// Check 'Required Field' check box and click Save button
			requiredCheckboxCtrl.set("true");
			saveBtnCtrl.click();
			VoodooUtils.waitForReady(30000);
			sugar().admin.studio.clickStudio();
			VoodooUtils.waitForReady(); // needed extra wait
		}
		VoodooUtils.focusDefault();

		// Controls for related to field
		// TODOD: VOOD-1445
		relatedToErrorFieldCtrl = new VoodooControl("span", "css", ".fld_parent_name.edit");
	}

	// Define private methods for all four modules
	// For Calls
	private void relatedToFieldVerificationForCalls() throws Exception {
		// Go to Calls -> Log Call
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.createDrawer.showMore();

		// Enter 'Subject' and click Save button
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.getControl("saveButton").click();

		// User must find 'Related to' field in red color and required
		sugar().alerts.getError().closeAlert();
		relatedToErrorFieldCtrl.assertAttribute("class", "error", true);

		// Click to select any existing Account in Related to field
		sugar().calls.createDrawer.getEditField("relatedToParentType").set(sugar().accounts.moduleNameSingular);
		VoodooControl relatedToParentNameCtrl = sugar().calls.createDrawer.getEditField("relatedToParentName");
		String accountName = sugar().accounts.getDefaultData().get("name");
		relatedToParentNameCtrl.set(accountName);

		// User must find selected 'Account' from the list showing up
		relatedToParentNameCtrl.assertEquals(accountName, true);

		// Save the record and click on the record from the list view to view the record view
		sugar().calls.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();
		sugar().calls.listView.clickRecord(1);

		// Verify the record
		sugar().calls.recordView.getDetailField("name").assertEquals(testName, true);
		sugar().calls.recordView.getDetailField("relatedToParentName").assertEquals(accountName, true);
	}

	// For Meetings
	private void relatedToFieldVerificationForMeetings() throws Exception {
		// Go to Meetings -> Log Call
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.showMore();

		// Enter 'Subject' and click Save button
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getControl("saveButton").click();

		// User must find 'Related to' field in red color and required
		sugar().alerts.getError().closeAlert();
		relatedToErrorFieldCtrl.assertAttribute("class", "error", true);

		// Click to select any existing Account in Related to field
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().accounts.moduleNameSingular);
		VoodooControl relatedToParentNameCtrl = sugar().meetings.createDrawer.getEditField("relatedToParentName");
		String accountName = sugar().accounts.getDefaultData().get("name");
		relatedToParentNameCtrl.set(accountName);

		// User must find selected 'Account' from the list showing up
		relatedToParentNameCtrl.assertEquals(accountName, true);

		// Save the record and click on the record from the list view to view the record view
		sugar().meetings.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();
		sugar().meetings.listView.clickRecord(1);

		// Verify the record
		sugar().meetings.recordView.getDetailField("name").assertEquals(testName, true);
		sugar().meetings.recordView.getDetailField("relatedToParentName").assertEquals(accountName, true);

	}

	// For Tasks
	private void relatedToFieldVerificationForTasks() throws Exception {
		// Go to Tasks -> Log Call
		sugar().tasks.navToListView();
		sugar().tasks.listView.create();
		sugar().tasks.createDrawer.showMore();

		// Enter 'Subject' and click Save button
		sugar().tasks.createDrawer.getEditField("subject").set(testName);
		sugar().tasks.createDrawer.getControl("saveButton").click();

		// User must find 'Related to' field in red color and required
		sugar().alerts.getError().closeAlert();
		relatedToErrorFieldCtrl.assertAttribute("class", "error", true);

		// Click to select any existing Account in Related to field
		sugar().tasks.createDrawer.getEditField("relRelatedToParentType").set(sugar().accounts.moduleNameSingular);
		VoodooControl relatedToParentNameCtrl = sugar().tasks.createDrawer.getEditField("relRelatedToParent");
		String accountName = sugar().accounts.getDefaultData().get("name");
		relatedToParentNameCtrl.set(accountName);

		// User must find selected 'Account' from the list showing up
		relatedToParentNameCtrl.assertEquals(accountName, true);

		// Save the record and click on the record from the list view to view the record view
		sugar().tasks.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();
		sugar().tasks.listView.clickRecord(1);

		// Verify the record
		sugar().tasks.recordView.getDetailField("subject").assertEquals(testName, true);
		sugar().tasks.recordView.getDetailField("relRelatedToParent").assertEquals(accountName, true);
	}

	// For Notes
	private void relatedToFieldVerificationForNotes() throws Exception {
		// Go to Notes -> Log Call
		sugar().notes.navToListView();
		sugar().notes.listView.create();
		sugar().notes.createDrawer.showMore();

		// Enter 'Subject' and click Save button
		sugar().notes.createDrawer.getEditField("subject").set(testName);
		sugar().notes.createDrawer.getControl("saveButton").click();

		// User must find 'Related to' field in red color and required
		sugar().alerts.getError().closeAlert();
		relatedToErrorFieldCtrl.assertAttribute("class", "error", true);

		// Click to select any existing Account in Related to field
		sugar().notes.createDrawer.getEditField("relRelatedToModule").set(sugar().accounts.moduleNameSingular);
		VoodooControl relatedToParentNameCtrl = sugar().notes.createDrawer.getEditField("relRelatedToValue");
		String accountName = sugar().accounts.getDefaultData().get("name");
		relatedToParentNameCtrl.set(accountName);

		// User must find selected 'Account' from the list showing up
		relatedToParentNameCtrl.assertEquals(accountName, true);

		// Save the record and click on the record from the list view to view the record view
		sugar().notes.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();
		sugar().notes.listView.clickRecord(1);

		// Verify the record
		sugar().notes.recordView.getDetailField("subject").assertEquals(testName, true);
		sugar().notes.recordView.getDetailField("relRelatedToValue").assertEquals(accountName, true);
	}

	/**
	 * Verify that 'Related To' field of Calls/Meetings/Tasks/Notes works properly if set 'Required' in Studio
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_29959_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// For Calls Module
		relatedToFieldVerificationForCalls();

		// For Meetings Module
		relatedToFieldVerificationForMeetings();

		// For Tasks Module
		relatedToFieldVerificationForTasks();

		// For Notes Module
		relatedToFieldVerificationForNotes();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}