package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Tasks_21015 extends SugarTest {
	DataSource customData = new DataSource();

	public void setup() throws Exception {	
		DataSource customData = testData.get(testName);
		// Creating tasks records
		sugar().tasks.api.create(customData);  

		// Log in as admin.
		sugar().login();
	}

	/**
	 * Mass Update Task_Verify that task can be modified by "Mass Update" function.
	 * @throws Exception
	 */	
	@Test
	public void Tasks_21015_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Navigate to Tasks List View
		sugar().tasks.navToListView();
		sugar().tasks.listView.toggleSelectAll();

		// Mass update task records for "Assigned User", Priority & Status
		String qauser = sugar().users.getQAUser().get("userName");
		FieldSet massUpdateData = testData.get(testName + "_massUpdateData").get(0);
		sugar().tasks.massUpdate.performMassUpdate(massUpdateData);

		// Verify the updated fields on list view
		VoodooControl assignedToListCtrl = sugar().tasks.listView.getDetailField(1, "relAssignedTo");
		for (int i = 0 ; i < customData.size() ; i++) {
			assignedToListCtrl.assertEquals(qauser, true);
		}

		// Verify the updated fields on record view of both the records
		VoodooControl assignedToDetailCtrl = sugar().tasks.recordView.getDetailField("relAssignedTo");
		sugar().tasks.listView.clickRecord(1);

		// Verify "Assigned to" field
		assignedToDetailCtrl.assertEquals(qauser, true);

		// Verify "Status" field
		VoodooControl statusFieldCtrl = sugar().tasks.recordView.getDetailField("status");
		statusFieldCtrl.assertEquals(massUpdateData.get("Status"), true);

		// Verify "Priority" field
		VoodooControl priorityFieldCtrl = sugar().tasks.recordView.getDetailField("priority");
		priorityFieldCtrl.assertEquals(massUpdateData.get("Priority"), true);

		// Click on "next" chevron icon to verify second record.
		sugar().tasks.recordView.gotoNextRecord();

		// Verify "Assigned to" field
		assignedToDetailCtrl.assertEquals(qauser, true);

		// Verify "Status" field
		statusFieldCtrl.assertEquals(massUpdateData.get("Status"), true);

		// Verify "Priority" field
		priorityFieldCtrl.assertEquals(massUpdateData.get("Priority"), true);
	}

	public void cleanup() throws Exception {}
}