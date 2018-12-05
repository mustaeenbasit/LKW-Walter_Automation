package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_26234 extends SugarTest {
	FieldSet dashboardData;
	NoteRecord myNotes;

	public void setup() throws Exception {
		sugar.leads.api.create();
		myNotes = (NoteRecord) sugar.notes.api.create();
		dashboardData = testData.get(testName).get(0);
		sugar.login();

		// The "Notes & Attachments" Dashlet is added in "My Dashboard" in the Lead's record
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		VoodooControl dashboardTitle = sugar.leads.dashboard.getControl("dashboardTitle");
		if (!dashboardTitle.getText().contains(dashboardData.get("dashboardName")))
			sugar.leads.dashboard.chooseDashboard(dashboardData.get("dashboardName"));
		sugar.leads.dashboard.edit();
		sugar.leads.dashboard.addRow();
		sugar.leads.dashboard.addDashlet(3, 1);

		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("a", "css", "div[data-original-title='"+dashboardData.get("notesAndAttachments")+"'] a").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Drag and drop the "Notes & Attachments" dashlet to the top
		new VoodooControl("i", "css", ".dashlet-row li:nth-child(3) .fa-arrows-v").dragNDrop(new VoodooControl("i", "css", ".dashlet-row li:nth-child(1) .fa-arrows-v"));
		VoodooUtils.waitForReady();

		// TODO: VOOD-963 - CSS selector for save button not available for the dashboard edit view
		new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Linked Activity Stream is generated when Link an existing Note or an attachment
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_26234_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-960
		// Go to "Notes & Attachments" Dashlet -> Click on Actions -> Create Related Record
		new VoodooControl("span", "css", ".dashlet-row li:nth-child(1) .fa-plus").click();
		new VoodooControl("a", "css", ".open a[data-dashletaction='openCreateDrawer']").click();
		VoodooUtils.waitForReady();

		// Create a Note record by filling in the required field. 
		sugar.notes.createDrawer.getEditField("subject").set(testName);
		sugar.notes.createDrawer.save();

		// Look at Activity Stream messages in the Lead record view 
		sugar.leads.recordView.showActivityStream();

		// Verify a 'Nt' badge AS message about "Linked [NoteName] to [recordName]" in recordView
		sugar.leads.recordView.activityStream.assertCommentContains(dashboardData.get("assertMessageLinked") + " " + testName + " " + dashboardData.get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), 1, true);

		// TODO: VOOD-977 need defined control for the module icon on activity stream
		VoodooControl labelCtrl = new VoodooControl("div", "css", ".activitystream-list.results li:nth-of-type(1) div.label-module");
		labelCtrl.assertEquals(dashboardData.get("moduleIconNotes"), true);
		sugar.leads.recordView.showDataView(); // Click on Data view

		// Look at Activity Stream messages in the Lead List view 
		sugar.leads.navToListView();
		sugar.leads.listView.showActivityStream();

		// Verify a 'Nt' badge AS message about "Linked [NoteName] to [recordName]" in List View
		sugar.leads.listView.activityStream.assertCommentContains(dashboardData.get("assertMessageLinked") + " " + testName + " " + dashboardData.get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), 1, true);
		labelCtrl.assertEquals(dashboardData.get("moduleIconNotes"), true);
		sugar.leads.listView.showListView(); // Click on Data view

		// Look at Activity Stream messages in the Home -> Activity Stream list view.
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		VoodooUtils.waitForReady();

		// Verify a 'Le' badge AS message about "Linked [NoteName] to [recordName]" in Home AS listView. 
		ActivityStream stream = new ActivityStream();
		stream.assertCommentContains(dashboardData.get("assertMessageLinked") + " " + testName + " " + dashboardData.get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), 2, true);
		// TODO: VOOD-977 need defined control for the module icon on activity stream
		VoodooControl labelSecondRowCtrl = new VoodooControl("div", "css", ".activitystream-list.results li:nth-of-type(2) div.label-module");
		labelSecondRowCtrl.assertEquals(dashboardData.get("moduleIconLeads"), true);

		// Look at Activity Stream messages in the Notes record view 
		sugar.notes.navToListView();
		sugar.notes.listView.clickRecord(1);

		// verify a 'Le' badge AS message about "Linked [NoteName] to [recordName]" in recordView
		sugar.notes.recordView.activityStream.assertCommentContains(dashboardData.get("assertMessageLinked") + " " + testName + " " + dashboardData.get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), 2, true);
		labelSecondRowCtrl.assertEquals(dashboardData.get("moduleIconLeads"), true);

		// In the Lead's record view, in Notes sub panel, "Link Existing Record" to link a Note record
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		sugar.leads.recordView.subpanels.get(sugar.notes.moduleNamePlural).linkExistingRecord(myNotes);

		// Look at Activity Stream messages in the Lead record view 
		sugar.leads.recordView.showActivityStream();

		// Verify a 'Nt' badge AS message about "Linked [NoteName] to [recordName]" in recordView
		sugar.leads.recordView.activityStream.assertCommentContains(dashboardData.get("assertMessageLinked") + " " + myNotes.getRecordIdentifier() + " " + dashboardData.get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), 1, true);
		labelCtrl.assertEquals(dashboardData.get("moduleIconNotes"), true);
		sugar.leads.recordView.showDataView(); // Click on Data view

		// Look at Activity Stream messages in the Lead List view 
		sugar.leads.navToListView();
		sugar.leads.listView.showActivityStream();

		// Verify a 'Nt' badge AS message about "Linked [NoteName] to [recordName]" in List View
		sugar.leads.listView.activityStream.assertCommentContains(dashboardData.get("assertMessageLinked") + " " + myNotes.getRecordIdentifier() + " " + dashboardData.get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), 1, true);

		// TODO: VOOD-977 need defined control for the module icon on activity stream
		labelCtrl.assertEquals(dashboardData.get("moduleIconNotes"), true);
		sugar.leads.listView.showListView(); // Click on Data view

		// Look at Activity Stream messages in the Home -> Activity Stream list view.
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		VoodooUtils.waitForReady();

		// Verify a 'Le' badge AS message about "Linked [NoteName] to [recordName]" in Home AS listView. 
		stream.assertCommentContains(dashboardData.get("assertMessageLinked") + " " + myNotes.getRecordIdentifier() + " " + dashboardData.get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), 1, true);
		labelCtrl.assertEquals(dashboardData.get("moduleIconLeads"), true);

		// TODO VOOD-954 the clean up will fail if not change back to Home dashboard page
		sugar.navbar.clickModuleDropdown(sugar.home);
		// TODO VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	
		VoodooUtils.waitForReady();

		// Look at Activity Stream messages in the Notes record view 
		sugar.notes.navToListView();
		sugar.notes.listView.clickRecord(1);

		// verify a 'Le' badge AS message about "Linked [NoteName] to [recordName]" in recordView
		sugar.notes.recordView.activityStream.assertCommentContains(dashboardData.get("assertMessageLinked") + " " + myNotes.getRecordIdentifier() + " " + dashboardData.get("assertMessageTo") + " " + sugar.leads.getDefaultData().get("fullName"), 1, true);
		labelCtrl.assertEquals(dashboardData.get("moduleIconLeads"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}