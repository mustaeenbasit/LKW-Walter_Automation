package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_28768 extends SugarTest {
	UserRecord chrisUser;
	DataSource processData = new DataSource();

	public void setup() throws Exception {
		processData = testData.get(testName);

		// Log-In as an Admin
		sugar().login();

		// Create a regular user other than qaUser
		chrisUser = (UserRecord)sugar().users.create();

		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + processData.get(0).get("version") +".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition record
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create an Account record to trigger the Process
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.createDrawer.save();
	}

	/**
	 * Verify the correct form and correct status are shown if you perform one way and then round trip
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_28768_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1706 - Need Lib Support for the Process List View and Record view.
		VoodooControl showProcess = new VoodooControl("a", "css", "span[data-voodoo-name='edit_button'] a");
		VoodooControl approveButton = new VoodooControl("a", "class", "btn-success");
		VoodooControl rejectButton = new VoodooControl("a", "class", "btn-danger");
		VoodooControl editDropDown = new VoodooControl("a", "css", "div[data-voodoo-name='pmse-case'] .actions .dropdown-toggle");
		VoodooControl selectNewProcessUser = new VoodooControl("a", "css", "a[name='duplicate_button']");
		VoodooSelect selectUser = new VoodooSelect("span", "id", "s2id_adhoc_user");
		VoodooControl selectType = new VoodooControl("select", "id", "adhoc_type");
		VoodooControl saveProcessUserSettings = new VoodooControl("a", "css", ".adam-button.btn-primary");

		// Process User = Admin
		// Navigate to Processes module and click rowActionDropdown
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		sugar().processes.listView.openRowActionDropdown(1);

		// Click "Show Process" option from the action drop down
		showProcess.click();
		VoodooUtils.waitForReady();

		// Assert that approve/reject buttons are displayed
		approveButton.assertVisible(true);
		rejectButton.assertVisible(true);

		// Click on Edit drop down button in the Process Record View and select qaUser as a new Process User
		editDropDown.click();
		selectNewProcessUser.click();
		selectUser.set(sugar().users.qaUser.get("userName"));

		// Set the Type to One-Way
		selectType.set(processData.get(0).get("type"));

		// Save the new Process User
		saveProcessUserSettings.click();
		VoodooUtils.waitForReady();

		// Logout from Administrator
		sugar().logout();

		// Login as a qaUser
		sugar().login(sugar().users.getQAUser());

		// Navigate to Processes module and click rowActionDropdown
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		sugar().processes.listView.openRowActionDropdown(1);

		// Click "Show Process" option from the action drop down
		showProcess.click();
		VoodooUtils.waitForReady();

		// Assert that Approve/Reject Buttons are displayed to qaUser as well
		approveButton.assertVisible(true);
		rejectButton.assertVisible(true);

		// Click on Edit drop down button in the Process Record View and select Chris as a New Process User
		editDropDown.click();
		selectNewProcessUser.click();
		selectUser.set(chrisUser.get("userName"));

		// Set the Type to Round Trip
		selectType.set(processData.get(1).get("type"));

		// Save the new process User
		saveProcessUserSettings.click();
		VoodooUtils.waitForReady();

		// Logout from qaUser
		sugar().logout();

		// Login as chrisUser
		sugar().login(chrisUser);

		// Navigate to Processes module and click rowActionDropdown
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		sugar().processes.listView.openRowActionDropdown(1);

		// Click "Show Process" option from the action drop down
		showProcess.click();
		VoodooUtils.waitForReady();

		// Click the Route button on the Process Record View
		new VoodooControl("a", "css", "span[data-voodoo-name='reject_button'] a").click();
		sugar().alerts.getWarning().confirmAlert();

		// logout from chrisUser
		sugar().logout();

		// Login as a qaUser again and verify that the process has been routed
		sugar().login(sugar().users.getQAUser());

		// Navigate to Processes module and click rowActionDropdown
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		sugar().processes.listView.openRowActionDropdown(1);

		// Click "Show Process" option from the action drop down
		showProcess.click();
		VoodooUtils.waitForReady();

		// Assert that Approve / Reject buttons should display for qaUser
		approveButton.assertVisible(true);
		rejectButton.assertVisible(true);

		// Approve the Process
		approveButton.click();
		sugar().alerts.getWarning().confirmAlert();

		// Logout from qaUser
		sugar().logout();

		// login as Admin again :
		sugar().login();
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);

		// Verify that no process record appearing in the Processes list view
		sugar().processes.listView.assertIsEmpty();

		// Navigate to Process Management List View
		sugar().navbar.clickModuleDropdown(sugar().processes);
		sugar().processes.menu.getControl("processManagement").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1698 - Need Lib support for Process Management and Unattended Processes list view
		// Assert that process status = Completed
		new VoodooControl("div", "css", "[data-voodoo-name='casesList-list'] .single .fld_cas_status div").
		assertContains(processData.get(0).get("processStatus"), true);

		// Assert the Record Name
		new VoodooControl("div", "css", "[data-voodoo-name='casesList-list'] .single .fld_cas_title div a").
		assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
