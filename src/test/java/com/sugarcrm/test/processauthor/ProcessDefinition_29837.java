package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessDefinition_29837 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that hidden module can still be used in process author
	 * @throws Exception
	 */
	@Test
	public void ProcessDefinition_29837_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Import Process Definition (Go to Process Definitions module and create a definition.  eg.  Account start event (new record)> Activity (static assignment to record owner) > End event)
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		
		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a new account and assign to qauser
		FieldSet fs = new FieldSet();
		fs = sugar().accounts.getDefaultData();
		fs.put("relAssignedTo", sugar().users.qaUser.get("userName"));
		sugar().accounts.create(fs);

		// Go to admin > Display Modules and Subpanels > hide Accounts module
		sugar().admin.disableSubpanelDisplayViaJs(sugar().accounts);

		// Logout as Admin and Login as qauser (regular user) 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to My Processes
		sugar().processes.navToListView();

		// Verify that QAuser can see the account process
		sugar().processes.listView.getDetailField(1, "pr_definition").assertContains(testName, true);

		// Select Show Process
		sugar().processes.listView.openActionDropdown();

		// TODO: VOOD-1706
		// Select Show Process
		new VoodooControl("a", "css", ".list.fld_edit_button a").click();
		VoodooUtils.waitForReady();

		// Show process is displayed correctly (account record is displayed)
		sugar().accounts.recordView.getDetailField("name").assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}