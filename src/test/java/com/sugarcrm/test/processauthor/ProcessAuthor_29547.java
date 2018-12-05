package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29547 extends SugarTest {
	public void setup() throws Exception {
		// Login as admin
		sugar().login();

		// Import a Process with: 
		// Create a process definition on leads module with an activity element and mark the check box for Select New Process User
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
	}

	/**
	 * Verify process definition record link is disabled if login as non-admin user
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29547_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet processData = testData.get(testName).get(0);

		// Create a new leads
		sugar().leads.api.create();

		// Go to Processes -> My Processes page 
		sugar().processes.navToListView();

		// Define Controls for Process list view
		VoodooControl processDefinitionCtrl = sugar().processes.processManagementListView.getDetailField(1, "pr_definition");
		VoodooControl recordNameCtrl = sugar().processes.processManagementListView.getDetailField(1, "rec_name");
		VoodooControl processDefinitionsNameCtrl = sugar().processDefinitions.recordView.getDetailField("name");
		VoodooControl processDefinitionsStatusCtrl = sugar().processDefinitions.recordView.getDetailField("status");
		VoodooControl fullNameCtrl = sugar().leads.recordView.getDetailField("fullName");
		String leadRecordName = sugar().leads.getDefaultData().get("fullName");
		// TODO: VOOD-1843
		VoodooControl processDefinitionLinkCtrl = new VoodooControl("a", "css", processDefinitionCtrl.getHookString() + " a");
		VoodooControl recordNameLinkCtrl = new VoodooControl("a", "css", recordNameCtrl.getHookString() + " a");

		// Verify that the records under "Process Definition" column and "Record Name" column should be shown as link
		processDefinitionLinkCtrl.assertEquals(testName, true);
		recordNameLinkCtrl.assertEquals(leadRecordName, true);

		// Verify that the records under "Process Definition" column should be click-able to redirect to the record view page
		processDefinitionCtrl.click();
		VoodooUtils.waitForReady();
		sugar().processDefinitions.recordView.assertVisible(true);
		processDefinitionsNameCtrl.assertContains(testName, true);
		processDefinitionsStatusCtrl.assertContains(processData.get("status"), true);

		// Navigate Back to Processes -> My Processes page 
		sugar().processes.navToListView();

		// Verify that the records under "Record Name" column should be click-able to redirect to the record view page.
		recordNameCtrl.click();
		VoodooUtils.waitForReady();
		sugar().leads.recordView.assertVisible(true);
		fullNameCtrl.assertContains(leadRecordName, true);

		// Navigate Back to Processes -> My Processes page 
		sugar().processes.navToListView();

		// Click Show Process -> Select New Process User -> Select QAUser 
		sugar().processes.myProcessesListView.showProcess(1);
		// TODO: VOOD-1706
		sugar().processes.recordView.copy(); // control is same for copy and Select New Process User
		VoodooSelect selectUser = new VoodooSelect("span", "id", "s2id_adhoc_user");
		selectUser.click();
		selectUser.clickSearchForMore();
		sugar().users.searchSelect.search(sugar().users.getQAUser().get("userName"));
		sugar().users.searchSelect.selectRecord(1);
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".adam-button.btn-primary").click();
		VoodooUtils.waitForReady();

		// Go to Processes -> Process Management page
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");

		// Verify that the records under "Process Definition" column and "Record Name" column should be shown as link
		processDefinitionLinkCtrl.assertEquals(testName, true);
		recordNameLinkCtrl.assertEquals(leadRecordName, true);

		// Verify that the records under "Process Definition" column should be click-able to redirect to the record view page
		processDefinitionCtrl.click();
		VoodooUtils.waitForReady();
		sugar().processDefinitions.recordView.assertVisible(true);
		processDefinitionsNameCtrl.assertContains(testName, true);
		processDefinitionsStatusCtrl.assertContains(processData.get("status"), true);

		// Navigate Back to Processes -> My Processes page 
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");

		// Verify that the records under "Record Name" column should be click-able to redirect to the record view page.
		recordNameCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1887
		sugar().leads.recordView.assertVisible(true);
		fullNameCtrl.assertContains(leadRecordName, true);

		// Logout and login as Chris
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Navigate Back to Processes -> My Processes page 
		sugar().processes.navToListView();

		// Verify that the Process Definition record link is disabled if user is non-admin and does not have a developer role.
		processDefinitionLinkCtrl.assertExists(false);
		processDefinitionCtrl.assertEquals(testName, true);
		recordNameLinkCtrl.assertEquals(leadRecordName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}