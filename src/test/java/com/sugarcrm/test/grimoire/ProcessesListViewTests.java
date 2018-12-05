package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for menu dropdown items, listview headers, row action dropdown, detail hook values
 * for process Management list view.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessesListViewTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().processes.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().processes);

		// Verify menu items
		sugar().processes.menu.getControl("viewProcesses").assertVisible(true);
		sugar().processes.menu.getControl("processManagement").assertVisible(true);
		sugar().processes.menu.getControl("unattendedProcesses").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().processes); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyMenuItemListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItemListView()...");

		sugar().navbar.selectMenuItem(sugar().processes, "viewProcesses");
		sugar().processes.myProcessesListView.verifyModuleTitle("My Processes");
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");
		sugar().processes.processManagementListView.verifyModuleTitle("Process Management");
		sugar().navbar.selectMenuItem(sugar().processes, "unattendedProcesses");
		sugar().processes.unattendedProcessesListView.verifyModuleTitle("Unattended Processes");

		VoodooUtils.voodoo.log.info("verifyMenuItemListView() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		sugar().processes.navToListView();
		for(String header : sugar().processes.myProcessesListView.getHeaders()){
			sugar().processes.myProcessesListView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");
		for(String header : sugar().processes.processManagementListView.getHeaders()){
			sugar().processes.processManagementListView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		sugar().navbar.selectMenuItem(sugar().processes, "unattendedProcesses");
		for(String header : sugar().processes.unattendedProcessesListView.getHeaders()){
			sugar().processes.unattendedProcessesListView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() test complete.");
	}

	@Test
	public void verifyProcessManagement() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyProcessManagement()...");

		// Import PD, trigger event
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/Lead_Process_Definition.bpm");

		// We need to enable this definition, after that trigger action is performed
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		sugar().leads.create();
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");

		// verify hook values
		sugar().processes.processManagementListView.getDetailField(1, "pr_number").assertExists(true);
		sugar().processes.processManagementListView.getDetailField(1, "pr_definition").assertExists(true);
		sugar().processes.processManagementListView.getDetailField(1, "rec_name").assertExists(true);
		sugar().processes.processManagementListView.getDetailField(1, "pr_user").assertExists(true);
		sugar().processes.processManagementListView.getDetailField(1, "pr_owner").assertExists(true);
		sugar().processes.processManagementListView.getDetailField(1, "status").assertExists(true);
		sugar().processes.processManagementListView.getDetailField(1, "assigned_to").assertExists(true);
		sugar().processes.processManagementListView.getDetailField(1, "date_created").assertExists(true);

		// Verify 'equals' & 'contains' methods
		sugar().processes.processManagementListView.verifyField(1, "rec_name", sugar().leads.getDefaultData().get("fullName"));
		sugar().processes.processManagementListView.verifyFieldContains(1, "rec_name", sugar().leads.getDefaultData().get("lastName"));

		// Verify actions dropdown items
		sugar().processes.processManagementListView.openRowActionDropdown(1);
		sugar().processes.processManagementListView.getControl("history01").assertVisible(true);
		sugar().processes.processManagementListView.getControl("showNotes01").assertVisible(true);
		sugar().processes.processManagementListView.getControl("reassign01").assertVisible(false);
		sugar().processes.processManagementListView.getControl("cancel01").assertVisible(false);
		sugar().processes.processManagementListView.getControl("dropdown01").click(); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyProcessManagement() complete.");
	}

	public void cleanup() throws Exception {}
}