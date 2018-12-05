package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29484 extends SugarTest {

	public void setup() throws Exception {
		// Log-In as an Admin
		sugar().login();
	}

	/**
	 * Verify the processes list view should display multiple activities with same process ID
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29484_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet processData = testData.get(testName).get(0);

		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + processData.get("version") + ".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition record
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create lead record to trigger the Process
		sugar().navbar.selectMenuItem(sugar().leads, "createLead");
		sugar().leads.createDrawer.showMore();
		sugar().leads.createDrawer.getEditField("lastName").set(sugar().leads.getDefaultData().get("lastName"));
		sugar().leads.createDrawer.getEditField("description").set(processData.get("description"));
		sugar().leads.createDrawer.save();

		// TODO: VOOD-1706 - Need Lib Support for the Process List View and Record view.
		VoodooControl approveButton = new VoodooControl("a", "class", "btn-success");

		// Navigate to Processes module		
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		// Open row action drop down and click showProcess Option
		sugar().processes.listView.editRecord(1);

		// Approve the process
		approveButton.click();
		sugar().alerts.getWarning().confirmAlert();

		// Navigate to Processes module		
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);

		// Need to sort the processes by Process Name because both are created concurrently.Hence, order not consistent
		sugar().processes.myProcessesListView.sortBy("headerTaskname", false);
		VoodooUtils.waitForReady();

		// Assert that both the processes i.e "Record Owner" and "User 5" are displayed and have the same process IDs.
		String processId = sugar().processes.listView.getDetailField(1, "pr_number").getAttribute("data-original-title");
		sugar().processes.listView.getDetailField(2, "pr_number").assertEquals(processId, true);
		sugar().processes.listView.getDetailField(1, "pr_name").assertEquals(processData.get("processName2"), true);
		sugar().processes.listView.getDetailField(2, "pr_name").assertEquals(processData.get("processName1"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}