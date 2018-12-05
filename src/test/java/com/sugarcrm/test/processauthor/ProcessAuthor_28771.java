package com.sugarcrm.test.processauthor;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_28771 extends SugarTest {
	FieldSet devRole = new FieldSet();

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * [PA] Verify Process Management lists processes corresponding to target modules for which user has
	 * Developer access
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_28771_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		devRole = testData.get(testName).get(0);
		FieldSet accountName = sugar().accounts.getDefaultData();

		// TODO: VOOD-856
		// Create a Role : "Access type" = "Developer" for Accounts module.
		AdminModule.createRole(devRole);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "id", "ACLEditView_Access_Accounts_admin").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_admin select").
		set(devRole.get("devPermission"));

		// Save the Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();;
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign devRole to qaUser
		AdminModule.assignUserToRole(sugar().users.getQAUser());

		String contactsProDef = String.format("%s%s", testName, devRole.get("fileSuffixContacts"));

		// Import process Definition for Contacts module
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + contactsProDef + ".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition rec.
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.verifyField(1, "name", contactsProDef);
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a new contact to trigger the process
		sugar().navbar.navToModule(sugar().contacts.moduleNamePlural);
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.getEditField("lastName").set(sugar().contacts.getDefaultData().get("lastName"));
		sugar().contacts.createDrawer.save();

		// Log-out from Admin user
		sugar().logout();

		// Log-in as a qaUser who has Access Type = Developer only for Accounts Module
		sugar().login(sugar().users.qaUser);
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");

		// TODO: VOOD-1698 - Need Lib support for Process Management and Unattended Processes list view.
		// Verify the Process for Contact is not listed.
		VoodooControl firstRowProcessDefName = new VoodooControl("div", "css", "[data-voodoo-name='casesList-list'] .single .fld_pro_title div");
		if (new VoodooControl("tr", "css", "[data-voodoo-name='casesList-list'] .single").queryExists())
			firstRowProcessDefName.assertEquals(contactsProDef, false);

		// Log-out from qaUser
		sugar().logout();

		// Log-In as Admin
		sugar().login();

		String accountsProDef = String.format("%s%s", testName, devRole.get("fileSuffixAccounts"));

		// Import process Definition for Accounts module
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + accountsProDef + ".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition rec.
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.verifyField(1, "name", accountsProDef);
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a new Account to trigger the process
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(accountName.get("name"));
		sugar().accounts.createDrawer.save();

		// Log-out from Admin
		sugar().logout();

		// Log-in as a qaUser
		sugar().login(sugar().users.qaUser);
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");

		// TODO: VOOD-1698 - Need Lib support for Process Management and Unattended Processes list view
		// Verify the Process for Account is listed.
		Assert.assertTrue(firstRowProcessDefName.queryContains(accountsProDef, true) &&
				new VoodooControl("div", "css", "[data-voodoo-name='casesList-list'] .single .fld_cas_title div a").
				queryContains(accountName.get("name"), true));

		// Log-out from qaUser because, as qaUser has developer permission hence Voodoo would try to perform
		// cleanup from qaUser itself
		sugar().logout();

		// Log-in as Admin
		sugar().login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}