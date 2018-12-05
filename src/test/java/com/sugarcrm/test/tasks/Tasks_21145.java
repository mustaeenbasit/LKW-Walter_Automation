package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21145 extends SugarTest {
	DataSource contactDetail,taskDetail;
	String adminUserName;

	public void setup() throws Exception {
		contactDetail = testData.get(testName);
		taskDetail = testData.get(testName + "_createTask");

		// Create 2 Contact records
		sugar.contacts.api.create(contactDetail);

		sugar.login();

		// Create 2 Tasks
		sugar.tasks.navToListView();
		sugar.tasks.listView.create();
		sugar.tasks.createDrawer.showMore();

		// Getting the default Assigned User
		adminUserName = sugar.tasks.createDrawer.getEditField("relAssignedTo").getText().toString().trim();

		// create first task
		sugar.tasks.createDrawer.getEditField("subject").set(taskDetail.get(0).get("taskName"));
		sugar.tasks.createDrawer.getEditField("contactName").set(contactDetail.get(0).get("lastName"));
		sugar.tasks.createDrawer.getEditField("date_due_date").set(taskDetail.get(0).get("taskDueDate"));
		sugar.tasks.createDrawer.save();
		
		// create second task 
		sugar.tasks.listView.create();
		sugar.tasks.createDrawer.getEditField("subject").set(taskDetail.get(1).get("taskName"));
		sugar.tasks.createDrawer.getEditField("contactName").set(contactDetail.get(1).get("lastName"));
		sugar.tasks.createDrawer.getEditField("relAssignedTo").set(sugar.users.getQAUser().get("userName"));
		sugar.tasks.createDrawer.getEditField("date_due_date").set(taskDetail.get(1).get("taskDueDate"));
		sugar.tasks.createDrawer.save();
		sugar.alerts.getAlert().closeAlert();

		// Change filter to "All" to display all the tasks records.
		sugar.tasks.listView.openFilterDropdown();
		sugar.tasks.listView.selectFilterAll();
	}

	/**
	 * Sort Task_Verify that tasks can be sorted in listview
	 * @throws Exception
	 */
	@Test
	public void Tasks_21145_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sort Task by Subject : Ascending Order
		sugar.tasks.listView.sortBy("headerName", true);
		sugar.tasks.listView.verifyField(1, "subject", taskDetail.get(0).get("taskName"));
		sugar.tasks.listView.verifyField(2, "subject", taskDetail.get(1).get("taskName"));

		// Sort Task by Subject : Descending Order
		sugar.tasks.listView.sortBy("headerName", false);
		sugar.tasks.listView.verifyField(1, "subject", taskDetail.get(1).get("taskName"));
		sugar.tasks.listView.verifyField(2, "subject", taskDetail.get(0).get("taskName"));

		// Sort Task by Contact Name : Ascending Order
		sugar.tasks.listView.sortBy("headerContactname", true);
		sugar.tasks.listView.verifyField(1, "contactName", contactDetail.get(0).get("lastName"));
		sugar.tasks.listView.verifyField(2, "contactName", contactDetail.get(1).get("lastName"));

		// Sort Task by Contact Name : Descending Order
		sugar.tasks.listView.sortBy("headerContactname", false);
		sugar.tasks.listView.verifyField(1, "contactName", contactDetail.get(1).get("lastName"));
		sugar.tasks.listView.verifyField(2, "contactName", contactDetail.get(0).get("lastName"));

		// Sort Task by Due Date : Ascending Order
		sugar.tasks.listView.sortBy("headerDatedue", true);
		sugar.tasks.listView.verifyField(1, "date_due_date", taskDetail.get(0).get("taskDueDate"));
		sugar.tasks.listView.verifyField(2, "date_due_date", taskDetail.get(1).get("taskDueDate"));

		// Sort Task by Due Date : Descending Order
		sugar.tasks.listView.sortBy("headerDatedue", false);
		sugar.tasks.listView.verifyField(1, "date_due_date", taskDetail.get(1).get("taskDueDate"));
		sugar.tasks.listView.verifyField(2, "date_due_date", taskDetail.get(0).get("taskDueDate"));

		// Sort Task by Assigned User : Ascending Order
		sugar.tasks.listView.sortBy("headerAssignedusername", true);
		sugar.tasks.listView.verifyField(1, "relAssignedTo", adminUserName);
		sugar.tasks.listView.verifyField(2, "relAssignedTo", sugar.users.getQAUser().get("userName"));

		// Sort Task by Assigned User : Descending Order
		sugar.tasks.listView.sortBy("headerAssignedusername", false);
		sugar.tasks.listView.verifyField(1, "relAssignedTo", sugar.users.getQAUser().get("userName"));
		sugar.tasks.listView.verifyField(2, "relAssignedTo", adminUserName);

		// Sort Task records by "date modified" and "date created"
		// This can be automated once VOOD-1450 would get fixed and merged.

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}