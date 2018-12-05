package com.sugarcrm.test.grimoire;


import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooTag;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class TasksModuleTests extends SugarTest {
	TaskRecord myTask;

	public void setup() throws Exception {
		myTask = (TaskRecord)sugar().tasks.api.create();
		sugar().login();
		sugar().tasks.navToListView();
	}

	@Test
	public void verifyInlineEditAndListFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineEditAndListFieldsInListView()...");

		String fullName = sugar().contacts.getDefaultData().get("fullName");
		// Create dependencies
		// TODO: VOOD-444 - Once resolved dependency should be via API
		LeadRecord myLead = (LeadRecord)sugar().leads.api.create();
		sugar().contacts.api.create();

		// Enable Start date, Team, Status field
		ArrayList<String> headers = new ArrayList<String>(Arrays.asList("date_start", "team_name", "status"));
		sugar().tasks.listView.toggleHeaderColumns(headers);

		// Edit record
		sugar().tasks.listView.editRecord(1);

		// subject
		sugar().tasks.listView.getEditField(1, "subject").assertEquals(myTask.get("subject"), true);

		// contact
		VoodooControl contactName = sugar().tasks.listView.getEditField(1, "contactName");
		contactName.set(sugar().contacts.getDefaultData().get("lastName"));
		contactName.assertContains(fullName, true);

		// parent type
		VoodooControl parentType = sugar().tasks.listView.getEditField(1, "relRelatedToParentType");
		parentType.set(sugar().leads.moduleNameSingular);
		parentType.assertEquals(sugar().leads.moduleNameSingular, true);

		// parent record
		VoodooControl parentRecord = sugar().tasks.listView.getEditField(1, "relRelatedToParent");
		parentRecord.set(myLead.getRecordIdentifier());
		parentRecord.assertContains(myLead.getRecordIdentifier(), true);

		// due date
		sugar().tasks.listView.getEditField(1, "date_due_date").assertContains(myTask.get("date_due_date"), true);

		// due time
		sugar().tasks.listView.getEditField(1, "date_due_time").assertContains(myTask.get("date_due_time"), true);

		// assigned user
		VoodooControl assignedUserEdit = sugar().tasks.listView.getEditField(1, "relAssignedTo");
		assignedUserEdit.scrollIntoViewIfNeeded(false);
		assignedUserEdit.assertEquals("Administrator", true);

		// start date
		sugar().tasks.listView.getEditField(1, "date_start_date").assertContains(myTask.get("date_start_date"), true);

		// start time
		sugar().tasks.listView.getEditField(1, "date_start_time").assertContains(myTask.get("date_start_time"), true);

		// status
		VoodooControl statusEdit = sugar().tasks.listView.getEditField(1, "status");
		statusEdit.scrollIntoViewIfNeeded(false);
		statusEdit.assertEquals(myTask.get("status"), true);

		// save the record with relation
		sugar().tasks.listView.saveRecord(1);

		// verify listview fields
		// subject
		sugar().tasks.listView.getDetailField(1, "subject").assertEquals(myTask.get("subject"), true);

		// contact
		sugar().tasks.listView.getDetailField(1, "contactName").assertContains(fullName, true);


		// parent record
		sugar().tasks.listView.getDetailField(1, "relRelatedToParent").assertContains(myLead.get("lastName"), true);

		// team
		sugar().tasks.listView.getDetailField(1, "relTeam").assertEquals("Global", true);

		// due date
		sugar().tasks.listView.getDetailField(1, "date_due_date").assertContains(myTask.get("date_due_date"), true);

		// due time
		sugar().tasks.listView.getDetailField(1, "date_due_time").assertContains(myTask.get("date_due_time"), true);

		// assigned user
		sugar().tasks.listView.getDetailField(1, "relAssignedTo").assertEquals("Administrator", true);

		// start date
		VoodooControl startDateDetail = sugar().tasks.listView.getDetailField(1, "date_start_date");
		startDateDetail.scrollIntoViewIfNeeded(false);
		startDateDetail.assertContains(myTask.get("date_start_date"), true);

		// start time
		sugar().tasks.listView.getDetailField(1, "date_start_time").assertContains(myTask.get("date_start_time"), true);

		// status
		sugar().tasks.listView.getDetailField(1, "status").assertEquals(myTask.get("status"), true);

		VoodooUtils.voodoo.log.info("verifyInlineEditAndListFieldsInListView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().tasks.listView.previewRecord(1);

		// Verify preview field values
		// subject
		sugar().previewPane.getPreviewPaneField("subject").assertEquals(myTask.get("subject"), true);

		// start date
		sugar().previewPane.getPreviewPaneField("date_start_date").assertContains(myTask.get("date_start_date"), true);

		// start time 
		sugar().previewPane.getPreviewPaneField("date_start_time").assertContains(myTask.get("date_start_time"), true);

		// priority
		sugar().previewPane.getPreviewPaneField("priority").assertEquals(myTask.get("priority"), true);

		// due date
		sugar().previewPane.getPreviewPaneField("date_due_date").assertContains(myTask.get("date_due_date"), true);

		// due time
		sugar().previewPane.getPreviewPaneField("date_due_time").assertContains(myTask.get("date_due_time"), true);

		// status
		sugar().previewPane.getPreviewPaneField("status").assertEquals(myTask.get("status"), true);

		// parent name
		sugar().previewPane.getPreviewPaneField("relRelatedToParent").assertVisible(true);

		// user
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertEquals("Administrator", true);

		// show more
		sugar().previewPane.showMore();

		// description
		sugar().previewPane.getPreviewPaneField("description").assertVisible(true);

		// contact
		sugar().previewPane.getPreviewPaneField("contactName").assertVisible(true);

		// teams
		sugar().previewPane.getPreviewPaneField("relTeam").assertContains("Global", true);

		// tags
		sugar().previewPane.getPreviewPaneField("tags").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		// Create dependencies
		// TODO: VOOD-444 - Once resolved dependency should be via API
		ContactRecord myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().tasks.listView.editRecord(1);
		sugar().tasks.listView.getEditField(1, "contactName").set(myContact.getRecordIdentifier());
		sugar().tasks.listView.getEditField(1, "relRelatedToParentType").set(sugar().contacts.moduleNameSingular);
		sugar().tasks.listView.getEditField(1, "relRelatedToParent").set(myContact.getRecordIdentifier());
		sugar().tasks.listView.saveRecord(1);
		sugar().tasks.listView.clickRecord(1);
		sugar().tasks.recordView.edit();
		sugar().tasks.recordView.showMore();

		// subject
		sugar().tasks.recordView.getEditField("subject").assertEquals(myTask.get("subject"), true);

		// start date
		sugar().tasks.recordView.getEditField("date_start_date").assertContains(myTask.get("date_start_date"), true);

		// start time
		sugar().tasks.recordView.getEditField("date_start_time").assertContains(myTask.get("date_start_time"), true);

		// priority
		sugar().tasks.recordView.getEditField("priority").assertEquals(myTask.get("priority"), true);

		// due date
		sugar().tasks.recordView.getEditField("date_due_date").assertContains(myTask.get("date_due_date"), true);

		// due time
		sugar().tasks.recordView.getEditField("date_due_time").assertContains(myTask.get("date_due_time"), true);

		// status
		sugar().tasks.recordView.getEditField("status").assertEquals(myTask.get("status"), true);

		// assigned
		sugar().tasks.recordView.getEditField("relAssignedTo").assertEquals("Administrator", true);

		// related to
		sugar().tasks.recordView.getEditField("relRelatedToParentType").assertEquals(sugar().contacts.moduleNameSingular, true);
		sugar().tasks.recordView.getEditField("relRelatedToParent").assertContains(myContact.getRecordIdentifier(), true);

		// description
		sugar().tasks.recordView.getEditField("description").assertVisible(true);

		// contactName

		sugar().tasks.recordView.getEditField("contactName").assertContains(myContact.getRecordIdentifier(), true);

		// team Name
		sugar().tasks.recordView.getEditField("relTeam").assertVisible(true);

		// tags
		VoodooTag tag = (VoodooTag)sugar().tasks.recordView.getEditField("tags");
		tag.set(testName);
		tag.assertContains(testName, true);

		// save the record 
		sugar().tasks.recordView.save();

		// Verify detail view field values
		// subject
		sugar().tasks.recordView.getDetailField("subject").assertEquals(myTask.get("subject"), true);

		// start date
		sugar().tasks.recordView.getDetailField("date_start_date").assertContains(myTask.get("date_start_date"), true);

		// start time
		sugar().tasks.recordView.getDetailField("date_start_time").assertContains(myTask.get("date_start_time"), true);

		// priority
		sugar().tasks.recordView.getDetailField("priority").assertEquals(myTask.get("priority"), true);

		// due date
		sugar().tasks.recordView.getDetailField("date_due_date").assertContains(myTask.get("date_due_date"), true);

		// due time
		sugar().tasks.recordView.getDetailField("date_due_time").assertContains(myTask.get("date_due_time"), true);

		// status
		sugar().tasks.recordView.getDetailField("status").assertEquals(myTask.get("status"), true);

		// assigned
		sugar().tasks.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// related to
		sugar().tasks.recordView.getDetailField("relRelatedToParentType").assertEquals(sugar().contacts.moduleNameSingular, true);
		sugar().tasks.recordView.getDetailField("relRelatedToParent").assertContains(myContact.getRecordIdentifier(), true);

		// description
		sugar().tasks.recordView.getDetailField("description").assertVisible(true);

		// contactName
		sugar().tasks.recordView.getDetailField("contactName").assertContains(myContact.getRecordIdentifier(), true);

		// team Name
		sugar().tasks.recordView.getDetailField("relTeam").assertVisible(true);

		// tags
		sugar().tasks.recordView.getDetailField("tags").assertContains(testName, true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void createViaMenu() throws Exception {
		VoodooUtils.voodoo.log.info("Running createViaMenu()...");

		sugar().navbar.clickModuleDropdown(sugar().tasks);
		sugar().tasks.menu.getControl("createTask").click();
		sugar().tasks.createDrawer.getEditField("subject").set(testName);
		sugar().tasks.createDrawer.save();

		// Verify task record is created 
		sugar().tasks.listView.verifyField(1, "subject", testName);

		VoodooUtils.voodoo.log.info("createViaMenu() complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().tasks);
		// Verify menu items
		sugar().tasks.menu.getControl("createTask").assertVisible(true);
		sugar().tasks.menu.getControl("viewTasks").assertVisible(true);
		sugar().tasks.menu.getControl("importTasks").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().tasks); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		ArrayList<String> headers = new ArrayList<String>(Arrays.asList("team_name", "date_start", "status"));
		sugar().tasks.listView.toggleHeaderColumns(headers);

		// TODO: VOOD-1768 - Once resolved "parent name" header check should remove it from for loop
		new VoodooControl("th", "css", "th[data-fieldname=parent_name]").assertVisible(true);

		// Verify all sort headers in listview
		for(String header : sugar().tasks.listView.getHeaders()) {
			// parent_name doesnot having sort option
			if (!header.equals("parent_name")) {		
				sugar().tasks.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
			}
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().tasks.listView.clickRecord(1);

		// Verify subpanels
		sugar().tasks.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}

}