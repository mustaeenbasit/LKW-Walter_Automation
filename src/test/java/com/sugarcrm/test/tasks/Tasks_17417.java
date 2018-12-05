package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Tasks_17417 extends SugarTest {	
	ContactRecord myCon;

	public void setup() throws Exception {	
		myCon = (ContactRecord) sugar.contacts.api.create();
		FieldSet tasksName = new FieldSet();
		tasksName.put("subject", testName);
		sugar.tasks.api.create(tasksName); 
		sugar.login();	
	}

	/**
	 * Verify user can specify date and time for the Tasks module.
	 * @throws Exception
	 */	
	@Test
	public void Tasks_17417_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		FieldSet firstTasks = testData.get(testName).get(0);

		// Navigate to task record 
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);
		sugar.tasks.recordView.edit();
		sugar.tasks.recordView.showMore();

		// Relate contact to the task
		sugar.tasks.recordView.getEditField("relRelatedToParentType").set(sugar.contacts.moduleNameSingular);
		sugar.tasks.recordView.getEditField("relRelatedToParent").set(sugar.contacts.getDefaultData().get("lastName"));
		sugar.tasks.recordView.getEditField("contactName").scrollIntoViewIfNeeded(false);
		sugar.tasks.recordView.getEditField("contactName").set(myCon.getRecordIdentifier());
		sugar.tasks.recordView.save();

		// Select Tasks module to display list view
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);

		// Verify that the default action is set to "Edit" on the top right of the record view
		sugar.tasks.recordView.getControl("editButton").assertVisible(true);

		// Click action drop down button > Select "Copy".
		sugar.tasks.recordView.copy();
		sugar.tasks.createDrawer.save();
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);
		sugar.tasks.recordView.showMore();

		//verify the copied record has kept values from original record. 
		sugar.tasks.recordView.getDetailField("subject").assertEquals(testName, true);
		sugar.tasks.recordView.getDetailField("priority").assertEquals(sugar.tasks.getDefaultData().get("priority"), true);
		sugar.tasks.recordView.getDetailField("relAssignedTo").assertElementContains(firstTasks.get("assignedTo"), true);
		sugar.tasks.recordView.getDetailField("contactName").assertContains(sugar.contacts.getDefaultData().get("fullName"), true);
		sugar.tasks.recordView.getDetailField("relTeam").assertContains(firstTasks.get("teamName"), true);
		sugar.tasks.recordView.getDetailField("relRelatedToParentType").assertContains(sugar.contacts.moduleNameSingular, true);
		sugar.tasks.recordView.getDetailField("relRelatedToParent").assertContains(sugar.contacts.getDefaultData().get("firstName") + " " + sugar.contacts.getDefaultData().get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}